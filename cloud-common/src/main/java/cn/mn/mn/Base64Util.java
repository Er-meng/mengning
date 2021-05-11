package cn.mn.mn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64Util {
    private final static Logger logger = LoggerFactory.getLogger(Base64Util.class);

    public static String ImageToBase64(String imgPath) {
        InputStream inputStream = null;
        String result = null;
        try {
            if (StringUtil.isEmpty(imgPath)) {
                return result;
            }

            inputStream = new FileInputStream(imgPath);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            result = base64Encoder.encode(data);
            if (StringUtil.isNotEmpty(result)) {
                //RFC2045规定Base64一行不能超过76字符,超过则添加回车换行符,所以这里要去除多余换行符
                result = result.replaceAll("\r|\n", "");
            }
        } catch (IOException e) {
            logger.error("ImageToBase64 异常", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("ImageToBase64 异常", e);
            }
        }
        return result;
    }

    public static boolean Base64ToImage(String imgBase64Data, String imgFilePath) {
        boolean result = false;
        if (imgBase64Data == null || StringUtil.isEmpty(imgFilePath)) {
            return result;
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imgFilePath);
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] b = base64Decoder.decodeBuffer(imgBase64Data);
            for (int i = 0; i < b.length; ++i) {
                //调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            outputStream.write(b);
            result = true;
        } catch (FileNotFoundException e) {
            logger.error("Base64ToImage 异常", e);
        } catch (IOException e) {
            logger.error("Base64ToImage 异常", e);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                logger.error("Base64ToImage 异常", e);
            }
        }
        return result;
    }
}
