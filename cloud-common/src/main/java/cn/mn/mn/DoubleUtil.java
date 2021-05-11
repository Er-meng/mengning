package cn.mn.mn;

import java.math.BigDecimal;
import java.util.Arrays;

public class DoubleUtil {

    /**
     * 四舍五入保留num位小数
     * @param num 需要保留的小数点位数
     * @return 返回四舍五入后的结果
     */
    public static double formatData(double f,int num)
    {
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(num,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    public static void main(String[] args)
    {
        double d1=98.0;
        double d2=99.1;
        System.out.println("d1-d2 = " +  String.valueOf(formatData((d1-d2),1)));
    }
}
