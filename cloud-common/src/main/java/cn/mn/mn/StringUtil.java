package cn.mn.mn;

/**
 * 字符串工具
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 去掉首位的空格,判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBank(String str) {
        return !isNotBank(str);
    }

    /**
     * 判断字符串是否不为空,且去掉首位的空格
     *
     * @param str
     * @return
     */
    public static boolean isNotBank(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static String getUrl(String ip, int port) {
        return String.format("http://%s:%s", ip, port);
    }

    public static String replaceAllBySpace(String content) {
        if (StringUtil.isNotEmpty(content)) {
            return content.replaceAll(" |　", "");
        }
        return content;
    }
}
