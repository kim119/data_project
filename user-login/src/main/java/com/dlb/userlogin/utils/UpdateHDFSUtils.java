package com.dlb.userlogin.utils;

import com.dlb.userlogin.Constant;
import com.dlb.userlogin.domain.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传操作
 */
public class UpdateHDFSUtils {

    /**
     *
     * @param dataType
     * @param user
     * @param config
     * @param url
     * @param file
     */
    public static void copyfileToHdfs(String dataType, User user, Configuration config, String url, File file) {
        try {
            System.out.println("config"+config);
            FileSystem fs = FileSystem.get(new URI(url), config,"hdfs");
            Path src = new Path(file.getPath());
            // 要上传到hdfs的目标路径
            Path dst = new Path(url + "/"+dataType+"/"+user.getUsername());
            if (!fs.exists(dst)) {
                // 创建目录
                fs.mkdirs(dst);
            }

            fs.copyFromLocalFile(src, dst);
            deleteFile(src.toString());
            fs.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 写入文件
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  File MultipartFileToFile(MultipartFile file) {
        File f = null;
        try {

            if (file.equals("") || file.getSize() <= 0) {
                file = null;
            } else {
                InputStream ins = file.getInputStream();
                f = new File(file.getOriginalFilename());
                inputStreamToFile(ins, f);
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        }
    }


    /**
     * 删除文件
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }


}
