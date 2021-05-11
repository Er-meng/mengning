package cn.mn.mn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 12066
 */
public class ZipUtil {
    /**
     * 压缩文件
     * @param files 键值对-》（文件名：文件链接）
     * @param outputStream
     * @throws Exception
     * @throws IOException
     */
    public static void zipFile(Map<String,String> files, ZipOutputStream outputStream) {
        try {
            Set<Map.Entry<String, String>> entrySet = files.entrySet();
            for (Map.Entry<String, String> file:entrySet) {
                try {
                    zipFile(getFileIs(file.getValue()),file.getKey(), outputStream);
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将文件写入到zip文件中
     * @param is
     * @param zos
     * @throws Exception
     */
    public static void zipFile(InputStream is, String fileName, ZipOutputStream zos) throws IOException, ServletException {
        try {
            if (is != null) {
                BufferedInputStream bInStream = new BufferedInputStream(is);
                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                int len = 0 ;
                byte[] buffer = new byte[10 * 1024];
                while ((len = is.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                    zos.flush();
                }
                zos.closeEntry();//Closes the current ZIP entry and positions the stream for writing the next entry
                bInStream.close();//关闭
                is.close();
            } else {
                throw new ServletException("文件不存在！");
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取文件流
     */
    public static InputStream getFileIs(String fileUrl) throws IOException{
        //new一个URL对象
        URL url = new URL(fileUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        return conn.getInputStream();
    }

    /**
     * 下载打包的文件
     *
     * @param file
     * @param response
     */
    public static void downloadZip(File file, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            file.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
