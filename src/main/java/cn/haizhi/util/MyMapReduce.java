package cn.haizhi.util;


import cn.haizhi.enums.DataNameEnum;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class MyMapReduce {
    public static class MyMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split("\t");
            context.write(new Text(DataNameEnum.TEMPERATURE.getName()), new DoubleWritable(Double.parseDouble(words[1])));
            context.write(new Text(DataNameEnum.WEIGHT.getName()), new DoubleWritable(Double.parseDouble(words[2])));
            context.write(new Text(DataNameEnum.HEARTBEAT.getName()), new DoubleWritable(Double.parseDouble(words[3])));
            context.write(new Text(DataNameEnum.SYSTOLICPRESSURE.getName()), new DoubleWritable(Double.parseDouble(words[4])));
            context.write(new Text(DataNameEnum.DIASTOLICPRESSURE.getName()), new DoubleWritable(Double.parseDouble(words[5])));
            context.write(new Text(DataNameEnum.BLOODFAT.getName()), new DoubleWritable(Double.parseDouble(words[6])));
            context.write(new Text(DataNameEnum.COUNT.getName()), new DoubleWritable(1));
        }
    }

    public static class MyReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable>{
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            double sum = 0;
            for (DoubleWritable value: values){
                sum += value.get();
            }
            context.write(key, new DoubleWritable(sum));
        }
    }


    public static FileSystem fileSystem;

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/*.xml");
        fileSystem = (FileSystem) context.getBean("fileSystem");
    }



    public static void mapreduceTask(String inputPathStr, String outputPathStr, String resultPathStr) throws IOException, ClassNotFoundException, InterruptedException {
        //创建Configuration
//        Configuration configuration = new Configuration();
//
//        //准备清理已存在的输出目录
        Path outputPath = new Path(outputPathStr);
//        FileSystem fileSystem = FileSystem.get(configuration);
        if(fileSystem.exists(outputPath)){
            fileSystem.delete(outputPath, true);
            System.out.println("output file exists, but has deleted");
        }

//        Job job = Job.getInstance(configuration, "wordcount");
        Job job = Job.getInstance(fileSystem.getConf(), "wordcount");
        //设置job的处理类
        job.setJarByClass(MyMapReduce.class);
        //设置作业处理的输入路径
        FileInputFormat.setInputPaths(job, new Path(inputPathStr));
        //设置map相关的参数
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        //设置reducer相关的参数
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileOutputFormat.setOutputPath(job, outputPath);
        boolean flag = job.waitForCompletion(true);

        Path resultPath = new Path(resultPathStr);

        fileSystem.copyToLocalFile(new Path( outputPath +  "/part-r-00000"), resultPath);

    }
}
