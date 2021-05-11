package cn.mn.mn;

import java.util.Collection;


/**
 * @author crj
 */
public class StringsUtil {
    /**
     * 字符串拼接
     * @param strArr
     * @return
     */
    public static String join(Collection strArr) {
        return join(strArr, ",");
    }
    /**
     * 字符串拼接
     * @param strArr
     * @param splitter
     * @return
     */
    public static String join(Collection strArr, String splitter) {
        StringBuffer result = new StringBuffer();
        for(Object temp : strArr) {
            if (result.length() != 0) {
                result.append(splitter);
            }
            result.append(String.valueOf(temp));
        }
        return result.toString();
    }

    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大
     * compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[.]");
        String[] version2Array = v2.split("[.]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
