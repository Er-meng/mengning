package cn.mn.mn;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

public class FileUtil {
    /**
     * 转换文件大小
     */
    public static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String result = "0B";
        if (fileSize == 0) {
            return result;
        }
        if (fileSize < 1024) {
            result = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            result = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            result = df.format((double) fileSize / 1048576) + "MB";
        } else {
            result = df.format((double) fileSize / 1073741824) + "GB";
        }
        return result;
    }
    /**
     * 获取文件类型
     */
    public static String getFileType(String filePath) {
        String result = null;
        String[] videoSuffix = {"ogg","mp4","webm"};//只判断能直接用video标签播放的格式
        String[] imgSuffix = {"jpg","jpeg","bmp","png","gif"};//只判断几种常用图片格式类型
        String fileSuffix = filePath.substring(filePath.lastIndexOf(".") + 1);
        if(Arrays.asList(videoSuffix).contains(fileSuffix.toLowerCase())){
            result = "video";
        }else if(Arrays.asList(imgSuffix).contains(fileSuffix.toLowerCase())){
            result = "image";
        }else {
            result = "other";
        }
        return result;
    }
    /**
     * 压缩静态图片文件
     */
    public static File compressPic(File file) throws IOException {
        String path = file.getAbsolutePath();
        if(getFileType(path) == "image" && !path.substring(path.lastIndexOf(".") + 1).toLowerCase().equals("gif")){
            Thumbnails.of(file).scale(1f).outputQuality(0.5f).outputFormat("jpg").toFile(path.substring(0,path.lastIndexOf(".")));
            return new File(path.substring(0,path.lastIndexOf("."))+".jpg");
        }else{
            return file;
        }
    }
}
