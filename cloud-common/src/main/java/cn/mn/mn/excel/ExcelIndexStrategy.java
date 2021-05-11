package cn.mn.mn.excel;

public interface ExcelIndexStrategy {
    /**
     * excel strategy,use to modify value just like if(value==1){value="male"}else{value="female"}
     *
     * @param index
     * @param value
     * @return newValue
     */
    public Strategy get(int index,String value);
}
