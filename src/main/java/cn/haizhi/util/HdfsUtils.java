package cn.haizhi.util;


import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;


public class HdfsUtils {
    private  FileSystem fileSystem;
    public static void mkdir(FileSystem fileSystem, String path) throws IOException {
        fileSystem.mkdirs(new Path(path));
    }

    public static void create(FileSystem fileSystem, String path) throws IOException {
        fileSystem.create(new Path(path));
    }

    public static void append(FileSystem fileSystem, String path, String content) throws IOException {
        FSDataOutputStream outputStream = fileSystem.append(new Path(path));
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();


    }

    public static void cat(FileSystem fileSystem, String path) throws IOException {
        FSDataInputStream in = fileSystem.open(new Path(path));
        IOUtils.copyBytes(in, System.out, 1024);
        in.close();
    }
}
