package cn.mn.mn;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class SmsServiceUtil {
    private final static String SpCode = "258784";
    private final static String LoginName = "hz_yhrd";
    private final static String Password = "TNRVPEGvqztteU";
    private final static String[] BanArray = {"13600532323","18868143000","13516825335","18268119333","13805779829","15558013888","13805746792","13968003699","13605702508","18969018155","13805779718","13516852888","13506810226","13588840129","13656662189","18758238666","13600512368","18367177000"};

    /**
     * 调用一信通短信接口发送短信验证码
     * @param msgContent 短信发送内容
     * @param phone 需要发送验证的手机号（多个号码用”,”分隔）
     * @param timing_time 预约发送时间，格式:yyyyMMddHHmmss
     * @return 接口返回信息
     */
    public static String sendSms(String msgContent,String phone,String timing_time){
        String info = null;
        try{
            //过滤不能发短信的人员号码
            if(Arrays.asList(BanArray).contains(phone)){
                return info;
            }

            //时间戳
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String strDate = df.format(new Date());

            CloseableHttpClient httpclient = HttpClients.createDefault();
            URI uri = new URIBuilder("http://ums.zj165.com:8888/sms/Api/Send.do")
                    .setCharset(Charset.forName("GBK"))
                    .addParameter("SpCode", SpCode) //企业编号
                    .addParameter("LoginName", LoginName)  //用户名称
                    .addParameter("Password", Password) //用户密码
                    .addParameter("MessageContent", msgContent) //短信内容, 最大480个字符（短信内容要求的编码为gbk）
                    .addParameter("SerialNumber", getRandom(3)+strDate) //流水号，20位数字，唯一 （规则自定义,建议时间格式精确到毫秒）
                    //.addParameter("MessageType", "2")  //短信类型，1=验证码、2=通知、3=广告 （没传或者非1、2、3系统会默认为4=其他）
                    //.addParameter("ExtendAccessNum", "0304") //扩展号
                    .addParameter("UserNumber", phone) //手机号码(多个号码用”,”分隔)，最多1000个号码
                    .addParameter("ScheduleTime", timing_time) //预约发送时间，格式:yyyyMMddhhmmss,如‘20090901010101’，立即发送请填空（预约时间要写当前时间5分钟之后的时间，若预约时间少于5分钟，则即时发送）
                    .addParameter("f", "1")
                    .build();
            HttpPost httpPost = new HttpPost(uri);

            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpPost);
                info = EntityUtils.toString(response.getEntity(),"GBK");
                System.out.println(info);
            }finally{
                if(response!=null){
                    response.close();
                }
                httpclient.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }



    /**
     * 获取短信回执信息
     * @return 接口返回信息
     */
    public static String reportSmsCode(){
        String info = null;
        try{
            CloseableHttpClient httpclient = HttpClients.createDefault();
            URI uri = new URIBuilder("http://ums.zj165.com:8888/sms/Api/report.do")
                    .setCharset(Charset.forName("GBK"))
                    .addParameter("SpCode", SpCode) //企业编号
                    .addParameter("LoginName", LoginName)  //用户名称
                    .addParameter("Password", Password) //用户密码
                    .build();
            HttpPost httpPost = new HttpPost(uri);
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpPost);
                info = EntityUtils.toString(response.getEntity(),"GBK");
                System.out.println(info);
            }finally{
                if(response!=null){
                    response.close();
                }
                httpclient.close();
            }
        }catch (Exception e) {
            System.out.println("短信回执信息请求错误！");
        }
        return info;
    }



    /**
     * 获取随机位数数字
     * @param length 想要生成的长度
     * @return result
     */
    public static String getRandom(Integer length) {
        String result = "";
        Random rand = new Random();
        int n = 20;
        if (null != length && length > 0) {
            n = length;
        }
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);
            result += randInt;
        }
        return result;
    }


    public static void main(String[] args) {
        String info =  sendSms("你有一项编号为123456789的事务需要处理。","15958049019","");
        System.out.println(info);
    }
}
