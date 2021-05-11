package cn.mn.mn;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFTPUtil {
    private String ip;
    private int port;
    private String userName;
    private String password;
    private final Logger logger = LoggerFactory.getLogger(FileFTPUtil.class);
    private final int CONN_TIMEOUT = 1000 * 30;
    private final String CONN_ENCODING = "utf-8";

    public FileFTPUtil(String ip, int port, String userName, String password) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    /***
     * 通过ftp协议上传单个文件
     * @param filePath  上传文件的本地路径
     * @return
     */
    public boolean upload(String filePath) {
        return this.upload(filePath, "");
    }

    /***
     * 通过ftp协议上传单个文件
     * @param filePath  上传文件的本地路径
     * @param remoteDir ftp服务端的目录
     * @return
     */
    public boolean upload(String filePath, String remoteDir) {
        boolean result = false;
        FTPClient ftpClient = null;
        try {
            ftpClient = this.connectFtpServer();
            if (ftpClient == null) {
                return result;
            }

            ftpClient.makeDirectory(remoteDir);
            ftpClient.changeWorkingDirectory(remoteDir);
            Path path = Paths.get(filePath);
            String fileName = path.getFileName().toString();
            result = ftpClient.storeFile(fileName, Files.newInputStream(path));
            if (!result) {
                this.logger.warn("上传 {} 文件失败", fileName);
            } else {
                this.logger.info("上传 {} 文件成功", fileName);
            }
            ftpClient.logout();
        } catch (IOException e) {
            this.logger.error("uploadFile 异常", e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    this.logger.error("uploadFile 异常", e);
                }
            }
        }
        return result;
    }

    public boolean download(String remoteFileName, String remoteDir, String filePath) {
        boolean result = false;
        FTPClient ftpClient = null;
        OutputStream outputStream = null;
        try {
            ftpClient = this.connectFtpServer();
            if (ftpClient == null) {
                return result;
            }

            String tempDir = String.format("/%s", remoteDir);
            ftpClient.changeWorkingDirectory(tempDir);
            FTPFile[] ftpFiles = ftpClient.listFiles(tempDir);
            Boolean flag = false;
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getName().equals(remoteFileName)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.logger.warn("未找到 {} 文件", remoteFileName);
                return result;
            }
            outputStream = new FileOutputStream(filePath);
            result = ftpClient.retrieveFile(remoteFileName, outputStream);
            if (!result) {
                this.logger.warn("下载 {} 文件失败", remoteFileName);
            } else {
                this.logger.info("下载 {} 文件成功", remoteFileName);
            }
            ftpClient.logout();
        } catch (IOException e) {
            this.logger.error("download 异常", e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                this.logger.error("download 异常", e);
            }
        }
        return result;
    }

    private FTPClient connectFtpServer() {
        FTPClient result = new FTPClient();
        result.setConnectTimeout(this.CONN_TIMEOUT);
        result.setControlEncoding(this.CONN_ENCODING);
        //设置被动模式，文件传输端口设置
        result.enterLocalPassiveMode();
        try {
            result.connect(this.ip, this.port);
            result.login(this.userName, this.password);
            //设置文件传输模式为二进制，可以保证传输的内容不会被改变
            result.setFileType(FTP.BINARY_FILE_TYPE);
            int replyCode = result.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                this.logger.error("connect ftp {}:{} failed", ip, port);
                result.disconnect();
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        return result;
    }
}
