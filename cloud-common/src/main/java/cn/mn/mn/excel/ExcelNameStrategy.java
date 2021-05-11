package cn.mn.mn.excel;

public interface ExcelNameStrategy {
    /**
     * excel strategy,use to modify value just like if(value==1){value="male"}else{value="female"}
     *
     * @param name
     * @param value
     * @return isok
     */
    public Strategy get(String name,String value);
}
