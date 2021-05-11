package cn.mn.mn;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class HTTPSHostnameVerifier implements HostnameVerifier {
    //重写验证方法
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        //所有都正确
        return true;
    }
}
