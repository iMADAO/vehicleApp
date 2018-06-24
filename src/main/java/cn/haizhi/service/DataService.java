package cn.haizhi.service;

import cn.haizhi.bean.*;
import cn.haizhi.enums.DataNameEnum;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.enums.GenderEnum;
import cn.haizhi.enums.TypeEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.form.Dataform;
import cn.haizhi.mapper.DataMapper;
import cn.haizhi.mapper.DeviceMapper;
import cn.haizhi.mapper.JedisClient;
import cn.haizhi.mapper.UserMapper;
import cn.haizhi.util.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.lib.input.InvalidInputException;
import org.hibernate.validator.internal.engine.groups.Group;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {

    @Autowired
    JedisClient jedisClient;

    @Autowired
    private FileSystem fileSystem;

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserMapper userMapper;

    private GroupDataDTO groupDataDTO;


    //接收数据，存入临时目录，并返回进七天每天平均信 息和同条件下所有人的平均信息
    //存入的临时目录结构为 /data/tmp_data/日期/用户id/日期-用户id 和 userinfo存入用户id 年龄 性别
    // /data/tmp_data/20180609/1528532161842133002/20180610-1528532161842133002
    //1528532161842133002     18      0
    public void receiveData(Dataform form, HttpServletRequest request, HttpSession session) {
        String redisStr = Const.DEVICE + "-" + form.getDeviceCode();
        String result = jedisClient.get(redisStr);
        Device device = null;
        String userId = null;
        if(result==null){
            device = deviceMapper.selectByPrimaryKey(form.getDeviceCode());
            if(device==null) {
                device = new Device(form.getDeviceCode());
                deviceService.register(device);
            }
            if(device.getUserId()==null || device.getUserId()==""){
                jedisClient.set(redisStr, Const.NOT_DATA);
                throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
            }

            userId = device.getUserId();
            jedisClient.set(redisStr, userId);

        }else{
            userId = jedisClient.get(redisStr);
            if(userId.equals(Const.NOT_DATA)){
                throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
            }
        }

        User user = userMapper.selectByPrimaryKey(userId);
        if (user==null){
             throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
        }

        groupDataDTO = new GroupDataDTO();
        BeanUtils.copyProperties(form, groupDataDTO);
        groupDataDTO.setLocalDateTime(LocalDateTime.now());
        Const.data.put(user.getUserId(), groupDataDTO);


        String dataStr = form.getTemperature() + "\t" + form.getWeight() + "\t" + form.getHeartbeat() + "\t" + form.getSystolicPressure() + "\t" + form.getDiastolicPressure() + "\t" + form.getBloodFat();
        Instant instant = Instant.now();
        dataStr = instant.toEpochMilli() + "\t" + dataStr;
        String pathStr =  Const.TEMP_DIR;
        String dateStr = DateFormatUtil.getDateStrNow();
        pathStr+="/" + dateStr + "/" + user.getUserId();
        String userInfoStr = pathStr + "/" + Const.USER_INFO;
        Path path = Paths.get(pathStr);
        BufferedWriter writer = null;
        BufferedWriter writer1 = null;
        try {
            if(!Files.exists(path)){
                Files.createDirectories(path);
                System.out.println("---------------Create Directory: " +  pathStr);
            }

            pathStr+= "/" + dateStr + "-" + user.getUserId();
            path = Paths.get(pathStr);
            if(!Files.exists(path)){
                Files.createFile(path);
                System.out.println("----------------Create File: " +  pathStr);
            }
            System.out.println("----------------: " +  pathStr);
            System.out.println("Write:---------------" + dataStr);
            writer = new BufferedWriter(new FileWriter(pathStr, true));
            writer.write(dataStr + "\n");
            writer.flush();

            Path userInfoPath = Paths.get(userInfoStr);
            if(!Files.exists(userInfoPath)){
                Files.createFile(userInfoPath);
                writer1 = new BufferedWriter(new FileWriter(userInfoStr));
                writer1.write(user.getUserId() + "\t" + user.getAge() + "\t" + user.getGender());
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer1!=null){
                try {
                    writer1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        //检查是否有异常
//        String redisTPName = Const.REDIS_PREFIX + "-" + user.getGender() + "-" + user.getAge() + "-" + Const.TEMPERATURE;
//        String redisBPName = Const.REDIS_PREFIX + "-" + user.getGender() + "-" + user.getAge() + "-" + Const.BLOODPRESSURE;
//        String temperatureString = jedisClient.get(redisTPName);
//        String bloodPressureString = jedisClient.get(redisBPName);
//        Data temperatureData = null;
//        Data bloodPressureData = null;
//        if(temperatureData==null){
//            DataExample example = new DataExample();
//            DataExample.Criteria criteria = example.createCriteria();
//            criteria.andAgeEqualTo(user.getAge());
//            criteria.andGenderEqualTo(user.getGender());
//            criteria.andTypeEqualTo(TypeEnum.TEMPERATURE.getType());
//            List<Data> dataList = dataMapper.selectByExample(example);
//            if (dataList.size()==0){
//                throw new MadaoException(ErrorEnum.DATA_NOT_SET);
//            }
//            temperatureData = dataList.get(0);
//            try {
//                jedisClient.set(redisTPName, JsonUtils.objectToJson(temperatureData));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }else{
//            temperatureData = JsonUtils.jsonToPojo(temperatureString, Data.class);
//        }
//
//
//        if(bloodPressureString==null){
//            DataExample example = new DataExample();
//            DataExample.Criteria criteria = example.createCriteria();
//            criteria.andAgeEqualTo(user.getAge());
//            criteria.andGenderEqualTo(user.getGender());
//            criteria.andTypeEqualTo(TypeEnum.BLOODPRESSURE.getType());
//            List<Data> dataList = dataMapper.selectByExample(example);
//            if (dataList.size()==0){
//                throw new MadaoException(ErrorEnum.DATA_NOT_SET);
//            }
//            bloodPressureData = dataList.get(0);
//            try {
//                jedisClient.set(redisBPName, JsonUtils.objectToJson(bloodPressureData));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }else{
//            try {
//                jedisClient.set(redisBPName, JsonUtils.objectToJson(bloodPressureData));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            bloodPressureData = JsonUtils.jsonToPojo(temperatureString, Data.class);
//        }

//        Map<ErrorEnum, String> errorMap = null;
//        if(form.getTemperature()>temperatureData.getUpperData()){
//            errorMap = new HashMap<>();
//            errorMap.put(ErrorEnum.TP_EXCEPTION, form.getTemperature()+"");
//        }
//
//        if (form.getBloodPressure()>bloodPressureData.getUpperData()){
//            if (errorMap==null)
//                errorMap = new HashMap<>();
//            errorMap.put(ErrorEnum.BP_EXCEPTION, form.getBloodPressure()+"");
//        }
//
//        if (errorMap!=null){
//            throw new MadaoException(ErrorEnum.DATA_EXCTPTION, ErrorResultUtil.getResultViewList(errorMap));
//        }
    }

    //将本地的临时存放的数据文件传入hdfs，为定时任务
    public void writeFromLocalToHdfs() throws IOException {
        String tempPathStr =  Const.TEMP_DIR;
        Path tempPath = Paths.get(tempPathStr);
        String hdfsDir = Const.HDFS_DIR;
        String dateStrNow = DateFormatUtil.getDateStrNow();
        final boolean[] deleteFlag = new boolean[1];
        try {
            Files.list(tempPath).forEach(p->{
                System.out.println(p);
                String dateStr = p.getFileName().toString();
                String pathToRemove = tempPathStr + "/" + dateStr;
                System.out.println(dateStr + "----------------");
                //只上传今天之前的数据
                Integer i1 = 0;
                Integer i2 = 0;
                deleteFlag[0] = false;
                try {
                    i1 = Integer.parseInt(dateStr);
                    i2 = Integer.parseInt(dateStrNow);
                }catch (NumberFormatException e){
                    System.out.println("转换不成功，跳过该文件");
                }
                if(i1 - i2 < 0) {
                    deleteFlag[0] = true;
                    try {
                        Files.list(p).forEach(p2 -> {
                            System.out.println("p2-----" + p2);
                            String userinfoPathStr = p2.toString() + "/" + Const.USER_INFO;
                            System.out.println(userinfoPathStr);
                            Path userinfoPath = Paths.get(userinfoPathStr);

                            boolean flag = false;
                            String[] list = null;
                            User user = new User();
                            if (Files.exists(userinfoPath)) {
                                try {
                                    BufferedReader reader = new BufferedReader(new FileReader(userinfoPathStr));
                                    String userInfo = reader.readLine();
                                    list = userInfo.split("\t");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (list != null && list.length == 3) {
                                flag = true;
                                user.setUserId(list[0]);
                                user.setAge(Byte.parseByte(list[1]));
                                user.setGender(Byte.parseByte(list[2]));
                            }
                            System.out.println(user);

                            if (flag) {
                                try {
                                    Files.list(p2).forEach(p3 -> {
                                        //    //上传文件到hdfs
                                        if (!p3.getFileName().toString().equals(Const.USER_INFO)) {

                                            String hdfsPathStr = hdfsDir + "/" + (user.getGender().equals(GenderEnum.FEMALE.getCode()) ? GenderEnum.FEMALE.getFlagStr() : GenderEnum.MALE.getFlagStr());
                                            hdfsPathStr = hdfsPathStr + "/" + user.getAge() + "/" + user.getUserId() + "/" + dateStr;
                                            System.out.println(hdfsPathStr);
                                            org.apache.hadoop.fs.Path srcPath = new org.apache.hadoop.fs.Path(p3.toString());
                                            org.apache.hadoop.fs.Path targetPath = new org.apache.hadoop.fs.Path(hdfsPathStr + "/" + srcPath.getName());
                                            System.out.println("src------" + srcPath);
                                            System.out.println("target-----" + targetPath);
                                            try {
                                                fileSystem.copyFromLocalFile(srcPath, targetPath);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    try {
                                        throw e;
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }

                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //todo 删除已上传的数据
//                try {
//                    if(deleteFlag[0])
//                        FileUtils.delete(pathToRemove, true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DataDTO getHistoryDataByDay(String dateStartStr, String dateEndStr, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        List<String> dateStrList = null;
        try {
            dateStrList = DateFormatUtil.getDateStrInRange(dateStartStr, dateEndStr);
        }catch (Exception e){
            throw new MadaoException(ErrorEnum.PARAM_ERROR);
        }

        if (dateStrList==null)
            throw new MadaoException(ErrorEnum.PARAM_ERROR);

        Map<String, GroupData> resultMap = new HashMap<>();
        for(String str: dateStrList){
            try {
                resultMap.put(str, getDailyData(str, user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DataDTO dataDTO = new DataDTO();
        dataDTO.setGroupDataList(resultMap);
        return dataDTO;
    }

    //获取用户过去某天的平均数据
    public GroupData getDailyData(String dateStr, User user) throws IOException {
        //  /vehicle/M/18/1528532161842133002/20180609/20180609-1528532161842133002
        if(dateStr.compareTo(Const.lastDateStr)<0 || dateStr.compareTo(LocalDate.now().format(Const.dateTimeFormatter))>=0){
            System.out.println("error date-----------" + dateStr);
            return null;
        }
        GroupData groupData = new GroupData();
        String redisFileName = Const.REDIS_PREFIX + "-" + user.getUserId() + "-" + dateStr;
        String data = jedisClient.get(redisFileName);
        if(data!=null){
            if (data.equals(Const.NOT_DATA)){
                return null;
            }
            return JsonUtils.jsonToPojo(data, GroupData.class);
        }else{
            String inputPathStr = Const.HDFS_DIR + "/" + (user.getGender().equals(GenderEnum.FEMALE.getCode()) ? GenderEnum.FEMALE.getFlagStr() : GenderEnum.MALE.getFlagStr());
            inputPathStr = inputPathStr + "/" + user.getAge() + "/" + user.getUserId() + "/" + dateStr;
            String outputPathStr = Const.HDFS_DIR + "/result/" + user.getUserId() + "/"+dateStr;
            String resultPath = Const.TEMP_DIR + "/" +  KeyUtil.genUniquKey();

            if (!fileSystem.exists(new org.apache.hadoop.fs.Path(inputPathStr))){
                System.out.println("inputPath1---------------------------" + inputPathStr);
                try {
                    jedisClient.set(redisFileName, Const.NOT_DATA);
                    jedisClient.expire(redisFileName, 86400);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            try {
                System.out.println("inputPath2---------------------------" + inputPathStr);
                inputPathStr = inputPathStr + "/*";
                MyMapReduce.mapreduceTask(inputPathStr, outputPathStr, resultPath);

                Path path = Paths.get(resultPath);
                if(Files.exists(path)){
                    BufferedReader reader = new BufferedReader(new FileReader(resultPath));
                    String line = null;
                    String[] strList;
                    Map<String, String> resultMap = new HashMap<>();
                    while((line = reader.readLine())!=null){
                        strList = line.split("\t");
                        resultMap.put(strList[0], strList[1]);
                    }

                    double count = Double.parseDouble(resultMap.get(Const.paramMap.get("count")));
                    groupData.setBloodFat(Double.parseDouble(resultMap.get(Const.paramMap.get("bloodFat"))) / count);
                    BloodPressure bloodPressure = new BloodPressure();
                    bloodPressure.setDiastolicPressure(Double.parseDouble(resultMap.get(Const.paramMap.get("diastolicPressure"))) / count);
                    bloodPressure.setSystolicPressure(Double.parseDouble(resultMap.get(Const.paramMap.get("systolicPressure"))) /count);
                    groupData.setBloodPressure(bloodPressure);
                    groupData.setHeartbeat(Double.parseDouble(resultMap.get(Const.paramMap.get("heartbeat"))) / count);
                    groupData.setTemperature(Double.parseDouble(resultMap.get(Const.paramMap.get("temperature")))/ count);
                    groupData.setWeight(Double.parseDouble(resultMap.get(Const.paramMap.get("weight"))) / count);
                    try {
                        jedisClient.set(redisFileName, JsonUtils.objectToJson(groupData));
                        jedisClient.expire(redisFileName, 604800);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return groupData;
    }



    public DataDTO getHistoryDataByMonth(String monthStartStr, String monthEndStr, HttpSession session){
        String monthStr = LocalDate.now().format(Const.monthFormatter);
        List<String> monthStrList = null;
        try {
            monthStrList = DateFormatUtil.getMonthStrInRange(monthStartStr, monthEndStr);
        }catch (Exception e){
            throw new MadaoException(ErrorEnum.PARAM_ERROR);
        }

        if (monthStrList==null)
            throw new MadaoException(ErrorEnum.PARAM_ERROR);


        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map<String, GroupData> resultMap = new HashMap<>();
        for(String str: monthStrList){
            try {
                if (str.compareTo(Const.lastMonthStr)<0 || str.compareTo(monthStr)>0){
                    resultMap.put(str, null);
                }else {
                    resultMap.put(str, getMonthlyData(str, user));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DataDTO dataDTO = new DataDTO();
        dataDTO.setGroupDataList(resultMap);
        return dataDTO;
    }

    //获取用户过去某月的平均数据
    public GroupData getMonthlyData(String monthStr, User user) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");

        try{
            dateTimeFormatter.parse(monthStr);
//            LocalDate localDate = LocalDate.parse(monthStr, dateTimeFormatter);

        }catch (Exception e){
            e.printStackTrace();
            throw new MadaoException(ErrorEnum.PARAM_ERROR);
        }

        String now = LocalDate.now().format(dateTimeFormatter);
        if(now.compareTo(monthStr)<0) {
            System.out.println(now + "----" + monthStr);
            return null;
        }
        //  /vehicle/M/18/1528532161842133002/201806*/*
        GroupData groupData = new GroupData();
        String redisFileName = Const.REDIS_PREFIX + "-" + user.getUserId() + "-" + monthStr;
        String data = jedisClient.get(redisFileName);
        if(data!=null){
            if (data.equals(Const.NOT_DATA))
                return null;
            return JsonUtils.jsonToPojo(data, GroupData.class);
        }else{
            String inputPathStr = Const.HDFS_DIR + "/" + (user.getGender().equals(GenderEnum.FEMALE.getCode()) ? GenderEnum.FEMALE.getFlagStr() : GenderEnum.MALE.getFlagStr());
            inputPathStr = inputPathStr + "/" + user.getAge() + "/" + user.getUserId() + "/" + monthStr + "*";
            String outputPathStr = Const.HDFS_DIR + "/result/" + user.getUserId() + "/"+ monthStr;
            String resultPath = Const.TEMP_DIR + "/" +  KeyUtil.genUniquKey();

            try {
                System.out.println("inputPath2---------------------------" + inputPathStr);
                inputPathStr = inputPathStr + "/*";
                try {
                    MyMapReduce.mapreduceTask(inputPathStr, outputPathStr, resultPath);
                }catch (InvalidInputException e){
                    try {
                        jedisClient.set(redisFileName, Const.NOT_DATA);
                        jedisClient.expire(redisFileName, 86400);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                    return null;
                }

                Path path = Paths.get(resultPath);
                if(Files.exists(path)){
                    BufferedReader reader = new BufferedReader(new FileReader(resultPath));
                    String line = null;
                    String[] strList;
                    Map<String, String> resultMap = new HashMap<>();
                    while((line = reader.readLine())!=null){
                        strList = line.split("\t");
                        resultMap.put(strList[0], strList[1]);
                    }

                    double count = Double.parseDouble(resultMap.get(Const.paramMap.get("count")));
                    groupData.setBloodFat(Double.parseDouble(resultMap.get(Const.paramMap.get("bloodFat"))) / count);
                    BloodPressure bloodPressure = new BloodPressure();
                    bloodPressure.setDiastolicPressure(Double.parseDouble(resultMap.get(Const.paramMap.get("diastolicPressure"))) / count);
                    bloodPressure.setSystolicPressure(Double.parseDouble(resultMap.get(Const.paramMap.get("systolicPressure"))) /count);
                    groupData.setBloodPressure(bloodPressure);
                    groupData.setHeartbeat(Double.parseDouble(resultMap.get(Const.paramMap.get("heartbeat"))) / count);
                    groupData.setTemperature(Double.parseDouble(resultMap.get(Const.paramMap.get("temperature")))/ count);
                    groupData.setWeight(Double.parseDouble(resultMap.get(Const.paramMap.get("weight"))) / count);
                    try {
                        jedisClient.set(redisFileName, JsonUtils.objectToJson(groupData));
                        jedisClient.expire(redisFileName, 604800);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return groupData;
    }

    //获取同一年龄，同性别的人的平均数据
    public GroupData getAverageData(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        GroupData groupData = new GroupData();
        String genderFlagStr = user.getGender().equals(GenderEnum.FEMALE.getCode()) ? GenderEnum.FEMALE.getFlagStr() : GenderEnum.MALE.getFlagStr();
        String redisName = Const.REDIS_PREFIX + "-" + genderFlagStr + "-" + user.getAge();
        String data = jedisClient.get(redisName);
        if(data!=null){
            return JsonUtils.jsonToPojo(data, GroupData.class);
        }else{
            String inputPathStr = Const.HDFS_DIR + "/" + genderFlagStr + "/" + user.getAge();
            String outputPathStr = Const.HDFS_DIR + "/result/" + genderFlagStr + "/" + user.getAge();
            String resultPath = Const.TEMP_DIR + "/" +  KeyUtil.genUniquKey();
            System.out.println("inputPath---------------------------" + inputPathStr);

            try {
                if (!fileSystem.exists(new org.apache.hadoop.fs.Path(inputPathStr))){
                    return null;
                }
                inputPathStr = inputPathStr + "/*/*";
                MyMapReduce.mapreduceTask(inputPathStr, outputPathStr, resultPath);

                Path path = Paths.get(resultPath);
                if(Files.exists(path)){
                    BufferedReader reader = new BufferedReader(new FileReader(resultPath));
                    String line = null;
                    String[] strList;
                    Map<String, String> resultMap = new HashMap<>();
                    while((line = reader.readLine())!=null){
                        strList = line.split("\t");
                        resultMap.put(strList[0], strList[1]);
                    }

                    double count = Double.parseDouble(resultMap.get(Const.paramMap.get("count")));
                    groupData.setBloodFat(Double.parseDouble(resultMap.get(Const.paramMap.get("bloodFat"))) / count);
                    BloodPressure bloodPressure = new BloodPressure();
                    bloodPressure.setDiastolicPressure(Double.parseDouble(resultMap.get(Const.paramMap.get("diastolicPressure"))) / count);
                    bloodPressure.setSystolicPressure(Double.parseDouble(resultMap.get(Const.paramMap.get("systolicPressure"))) /count);
                    groupData.setBloodPressure(bloodPressure);
                    groupData.setHeartbeat(Double.parseDouble(resultMap.get(Const.paramMap.get("heartbeat"))) / count);
                    groupData.setTemperature(Double.parseDouble(resultMap.get(Const.paramMap.get("temperature")))/ count);
                    groupData.setWeight(Double.parseDouble(resultMap.get(Const.paramMap.get("weight"))) / count);
                    try {
                        jedisClient.set(redisName, JsonUtils.objectToJson(groupData));
                        jedisClient.expire(redisName, 604800);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return groupData;

                }else{
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public DataWeekly getWeeklyData(int n, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
        }
        WeekPair weekPair = new WeekPair();
        List<String> list = weekPair.getWeekPairStr(n);
        if(list.get(1).compareTo(Const.lastDateStr)<0){
            return null;
        }

        String redisName = "week-" +list.get(0) + "-" + list.get(1) + "-" + user.getUserId();
        String result = null;
        try {
            result = jedisClient.get(redisName);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result!=null){
            return JsonUtils.jsonToPojo(result, DataWeekly.class);
        }

        String dateStr = LocalDate.now().format(Const.dateTimeFormatter);
        if(list.get(0).compareTo(dateStr)>0)
            return null;

        Map<String, GroupData> groupDataList = new HashMap<>();
        List<String> strList = DateFormatUtil.getDateStrInRange(list.get(0), list.get(1));
        strList.stream().forEach(System.out::println);

        int[] count = {0};
        double[] temperature = {0};
//        double[] bloodPressure = {0};
        double[] systolicPressure = {0};
        double[] diastolicPressure = {0};


        double[] bloodFat = {0};
        double[] weight = {0};
        double[] heartbeat = {0};

        strList.stream().filter(x->x!=null).forEach(x-> {
            System.out.println(x + "---");
            try {
                GroupData groupData = getDailyData(x, user);
                if (groupData!=null) {
                    temperature[0] += groupData.getTemperature();
                    systolicPressure[0] += groupData.getBloodPressure().getSystolicPressure();
                    diastolicPressure[0] += groupData.getBloodPressure().getDiastolicPressure();
                    bloodFat[0] += groupData.getBloodFat();
                    weight[0] += groupData.getWeight();
                    heartbeat[0] = groupData.getHeartbeat();
                    count[0]++;
                }

                groupDataList.put(x, groupData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        GroupData averageData = new GroupData();
        averageData.setBloodPressure(new BloodPressure());
        if (weight[0]!=0) {
            averageData.setWeight(weight[0] / count[0]);
        }
        if (temperature[0]!=0) {
            averageData.setTemperature(temperature[0] / count[0]);
        }
        if (heartbeat[0]!=0) {
            averageData.setHeartbeat(heartbeat[0] / count[0]);
        }

        if (systolicPressure[0]!=0) {
            averageData.getBloodPressure().setSystolicPressure(systolicPressure[0] / count[0]);
        }

        if (diastolicPressure[0]!=0) {
            averageData.getBloodPressure().setDiastolicPressure(diastolicPressure[0] / count[0]);
        }

        if (bloodFat[0]!=0) {
            averageData.setBloodFat(bloodFat[0] / count[0]);
        }

        DataWeekly dataWeekly = new DataWeekly();
        dataWeekly.setAverageData(averageData);
        dataWeekly.setGroupDataList(groupDataList);

        try {
            jedisClient.set(redisName, JsonUtils.objectToJson(dataWeekly));
            //如果选择的是当前周，则有可能会继续更新值，把redis过期的值设为半天
            if (n < 0) {
                jedisClient.expire(redisName, 43200);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataWeekly;
    }

    public DataResult getData(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null)
            throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
        GroupDataDTO groupDataDTO =  Const.data.get(user.getUserId());
        if (groupDataDTO==null){
            return null;
        }


//        if(groupDataDTO.getTemperature()>Const.errorTP){
//            tpReport += "数据异常";
//        }else if(groupDataDTO.getTemperature()>Const.warningThreeTP){
//            tpReport += " 体温过高，请停止开车";
//        }else if(groupDataDTO.getTemperature()>Const.warningTwoTp){
//            tpReport += "体温较高，请注意身体";
//        }else if(groupDataDTO.getTemperature()>Const.warningOneTp){
//            tpReport += "体温稍高";
//        }
        String tpReport = AccessUtil.accessTemp(groupDataDTO.getTemperature());
        String bpReport = AccessUtil.accessBloodPressure(new BloodPressure(groupDataDTO.getSystolicPressure(), groupDataDTO.getSystolicPressure()));
        String bfReport = "";
        String wReport = "";
        String hbReport = "";


        GroupData recentData = getPastData(user);
        if (recentData!=null){
            Double w = groupDataDTO.getWeight() - recentData.getWeight();
            wReport = wReport +  "近期" + (w>0 ? "增加" + w + UnitEnum.WEIGHT.getUnit() : (w<0 ? "减少"  + w + UnitEnum.WEIGHT.getUnit(): "基本不变"));
        }

        //TODO check bloodPressure and other parameter

        DataResult dataResult = new DataResult();
        dataResult.setGroupDataDTO(groupDataDTO);
        Map<String, String> report = new HashMap<>();
        report.put(DataNameEnum.TEMPERATURE.getName(), tpReport);
        report.put(DataNameEnum.BLOODPRESSUER.getName(), bpReport);
        report.put(DataNameEnum.HEARTBEAT.getName(), hbReport);
        report.put(DataNameEnum.WEIGHT.getName(), wReport);
        report.put(DataNameEnum.BLOODFAT.getName(), bfReport);
        dataResult.setReport(report);
        return dataResult;
    }

    public GroupData getPastData(User user){
        if (user==null){
            throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
        }

        List<String> dateStrList = DateFormatUtil.getWeekDateStrList();

        String redisName = "recent-" + user.getUserId();
        String result = null;
        try {
            result = jedisClient.get(redisName);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result!=null){
            return JsonUtils.jsonToPojo(result, GroupData.class);
        }

        int[] count = {0};
        double[] temperature = {0};
//        double[] bloodPressure = {0};
        double[] systolicPressure = {0};
        double[] diastolicPressure = {0};


        double[] bloodFat = {0};
        double[] weight = {0};
        double[] heartbeat = {0};

        dateStrList.stream().filter(x->x!=null).forEach(x-> {
            System.out.println(x + "---");
            try {
                GroupData groupData = getDailyData(x, user);
                System.out.println("--------------------------" + groupData);
                if (groupData!=null) {
                    temperature[0] += groupData.getTemperature();
                    if(groupData.getBloodPressure()!=null) {
                        systolicPressure[0] += groupData.getBloodPressure().getSystolicPressure();
                        diastolicPressure[0] += groupData.getBloodPressure().getDiastolicPressure();
                    }
                    bloodFat[0] += groupData.getBloodFat();
                    weight[0] += groupData.getWeight();
                    heartbeat[0] = groupData.getHeartbeat();
                    count[0]++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        GroupData averageData = new GroupData();
        averageData.setBloodPressure(new BloodPressure());
        if (weight[0]!=0) {
            averageData.setWeight(weight[0] / count[0]);
        }
        if (temperature[0]!=0) {
            averageData.setTemperature(temperature[0] / count[0]);
        }
        if (heartbeat[0]!=0) {
            averageData.setHeartbeat(heartbeat[0] / count[0]);
        }

        if (systolicPressure[0]!=0) {
            averageData.getBloodPressure().setSystolicPressure(systolicPressure[0] / count[0]);
        }

        if (diastolicPressure[0]!=0) {
            averageData.getBloodPressure().setDiastolicPressure(diastolicPressure[0] / count[0]);
        }

        if (bloodFat[0]!=0) {
            averageData.setBloodFat(bloodFat[0] / count[0]);
        }

        try {
            jedisClient.set(redisName, JsonUtils.objectToJson(averageData));
            jedisClient.expire(redisName, 86400);
        }catch (Exception e){
            e.printStackTrace();
        }
        return averageData;
    }
}
