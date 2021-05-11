package cn.mn.mn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author crj
 */
public class SplitUtil {
    /**
     *
     * @param source 目标字符串
     * @param regex 分割规则
     * @param isAllowEmpty 是否允许返回值为空
     * @return
     */
    public static String[] split(String source,String regex,Boolean isAllowEmpty){
        if(StringUtil.isBank(source)){
            return isAllowEmpty? null : new String[]{};
        }
        return source.split(regex);
    }

    public static String[] split(String source){
        return split(source,",",false);
    }

    public static String[] split(String source,String regex){
        return split(source,regex,false);
    }

    public static List<String> getList(String source, String regex){
       return new ArrayList<String>(Arrays.asList((split(source, regex,false))));
    }

    public static List<String> getList(String source){
        return getList(source,",");
    }

    public static List<Integer> getIntegerList(String source, String regex){
        return new ArrayList<Integer>(Arrays.asList((getIntegerArr(source, regex,false))));
    }

    public static List<Integer> getIntegerList(String source){
        return getIntegerList(source, ",");
    }
    /**
     * 分割字符串，并将分割的字符串转为Integer数组
     * @param source
     * @return
     */
    public static Integer[] getIntegerArr(String source){
        return getIntegerArr(source,",",false);
    }

    public static Integer[] getIntegerArr(String source,String regex,Boolean isAllowEmpty){
        String[] objs = split(source,regex,isAllowEmpty);
        Integer[] intObjs = new Integer[objs.length];
        for (int i =0; i<objs.length;i++) {
            intObjs[i] =  Integer.parseInt(objs[i]);
        }
        return intObjs;
    }
}
