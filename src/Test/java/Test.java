import cn.haizhi.bean.GroupData;
import cn.haizhi.bean.User;
import cn.haizhi.service.DataService;
import cn.haizhi.util.DateFormatUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/spring/*")
public class Test {
    @Autowired
    private DataService dataService;

    @org.junit.Test
    public  void test(){
        System.out.println(DateFormatUtil.getDateStrNow());
    }

    @org.junit.Test
    public  void test2(){
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
//        List<String> dateStrList = DateFormatUtil.getDateStrInRange("20151219", "20151212", dtf);
//        dateStrList.forEach(System.out::println);

        System.out.println(DateFormatUtil.getDateStrNow());
        System.out.println();
        String[] list = DateFormatUtil.getWeekDateString();
        System.out.println();
        for(String str: list)
            System.out.println(str);

    }

    @org.junit.Test
    public void test3() throws IOException {
        User user = new User();
        user.setUserId("1528532161842133002");
        user.setGender((byte)0);
        user.setAge((byte)18);
        GroupData groupData = dataService.getMonthlyData("201804", user);
        System.out.println("groupData----" + groupData);
    }

    @org.junit.Test
    public void test4(){
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
//        LocalDate.parse("199901", dateTimeFormatter);

    }

}
