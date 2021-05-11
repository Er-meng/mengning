package cn.mn.mn;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;

public class EasyPoiUtil {
    public static void createSelect(HSSFSheet sheet, String[] options, int firstRow, int lastRow, int firstCol, int lastCol) {
        //  只对(x，x)单元格有效
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        //  生成下拉框内容
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(options);
        HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
        //  对sheet页生效
        sheet.addValidationData(dataValidation);
    }

    public static void createDateCheck(HSSFSheet sheet, String msg, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, firstRow, firstCol, lastCol);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //DVConstraint constrain1 = DVConstraint.CreateDateConstraint(条件,"最小时间","最大时间","时间格式"); //这是检查时间的方法
        DataValidationConstraint constraint = helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "1900/01/01", "2099/12/31", "yyyy/MM/dd");
        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
        // 输入无效值时是否显示错误框
        dataValidation.setShowErrorBox(true);
        // 验证输入数据是否真确
        dataValidation.setSuppressDropDownArrow(true);
        // 设置无效值时 是否弹出提示框
        dataValidation.setShowPromptBox(true);
        // 设置提示框内容 createPromptBox
        // 设置无效值时的提示框内容 createErrorBox
        dataValidation.createPromptBox("温馨提示", msg);
        sheet.addValidationData(dataValidation);
    }

}
