package cn.mn.mn.excel;

public class Strategy {
    private String value;
    private boolean available ;

    public static Strategy ignore(){
        Strategy s = new Strategy();
        s.setAvailable(false);
        return s;
    }

    public static Strategy val(String value){
        Strategy s = new Strategy();
        s.setAvailable(true);
        s.setValue(value);
        return s;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
