package cn.mn.mn;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileHTTPUtil {
    private final Logger logger = LoggerFactory.getLogger(FileHTTPUtil.class);
    private final String NAME_FILE = "file";
    private final int TIMEOUT_REQ = 10000;
    private final int TIMEOUT_CONN = 5000;
    private final int CACHE = 10 * 1024;

    /**
     * 通过http协议上传单个文件
     *
     * @param url
     * @param filePath
     * @param paramsMap
     * @return
     */
    public boolean upload(String url, String filePath, Map<String, String> paramsMap) {
        List<String> filePaths = new ArrayList<String>();
        filePaths.add(filePath);
        return upload(url, filePaths, paramsMap);
    }

    /**
     * 通过http协议上传多个文件
     *
     * @param url
     * @param paramsMap
     * @param filePaths
     * @return
     * @throws IOException
     */
    public boolean upload(String url, List<String> filePaths, Map<String, String> paramsMap) {
        boolean result = false;
        try {
            if (StringUtil.isEmpty(url) || ListUtil.isEmpty(filePaths)) {
                return result;
            }

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(TIMEOUT_REQ)
                    .setConnectTimeout(TIMEOUT_CONN)
                    .build();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 解决中文文件名乱码问题
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.setCharset(Consts.UTF_8);
            ContentType contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), Consts.UTF_8);

            for (String filePath : filePaths) {
                Path path = Paths.get(filePath);
                String fileName = path.getFileName().toString();
                multipartEntityBuilder.addBinaryBody(NAME_FILE, Files.newInputStream(path),
                        ContentType.DEFAULT_BINARY, fileName);
            }

            if (paramsMap != null) {
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue(), contentType);
                }
            }

            httpPost.setEntity(multipartEntityBuilder.build());
            httpPost.setConfig(requestConfig);
            HttpClient client = HttpClients.createDefault();
            HttpResponse httpResponse = null;
            httpResponse = client.execute(httpPost);
            result = httpResponse.getStatusLine().getStatusCode() == 200 ? true : false;
            logger.info(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            logger.error("upload 异常", e);
        }
        return result;
    }

    /**
     * 通过http协议下载文件
     *
     * @param url
     * @param filepath
     * @return
     */
    public boolean download(String url, String filepath) {
        boolean result = false;
        try {
            if (StringUtil.isEmpty(url) || StringUtil.isEmpty(filepath)) {
                return result;
            }

            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[CACHE];
            int ch = 0;
            while ((ch = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, ch);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            result = true;
        } catch (Exception e) {
            logger.error("download 异常", e);
        }
        return result;
    }
}
