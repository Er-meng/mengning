package cn.mn.mn.excel;

import cn.mn.mn.DateUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

public class ExcelUtil {
    /**
     * 读取excel
     *
     * @param excelFile
     *            excel文件
     * @param beanMap
     *            封装对象
     * @param sheetNum
     *            第几个sheet
     * @param rowNum
     *            起始行数，从0开始
     * @param cellNum
     *            起始列数，从0开始
     * @param names
     *            对应数据库的字段名
     * @return List<T extends HashMap>
     *
     * @throws Exception
     */
    public static <T extends HashMap> List<T> read(File excelFile, T beanMap, int sheetNum, int rowNum, int cellNum,
                                                     String... names) throws Exception {
        ExcelNameStrategy excelStrategy = new ExcelNameStrategy() {
            public Strategy get(String name, String value) {
                return Strategy.val(value);
            }
        };
        return read(excelFile, beanMap, sheetNum, rowNum, cellNum, excelStrategy, names);
    }

    /**
     * 读取excel
     *
     * @param excelFile
     *            excel文件
     * @param beanMap
     *            封装对象
     * @param sheetNum
     *            第几个sheet
     * @param rowNum
     *            起始行数，从0开始
     * @param cellNum
     *            起始列数，从0开始
     * @param excelStrategy
     *            单元格值的策略，用来定制结果
     * @param names
     *            对应数据库的字段名
     * @return List<T extends HashMap>
     *
     * @throws Exception
     */
    public static <T extends HashMap> List<T> read(File excelFile, T beanMap, int sheetNum, int rowNum, int cellNum,
                                               ExcelNameStrategy excelStrategy, String... names) throws Exception {
        List<T> list = new ArrayList<T>();
        int nameLen = names.length;
        String filename = excelFile.getName();
        if (filename.endsWith(".xls")) {
            HSSFWorkbook hssfWorkbook = getHSSFWorkbook(excelFile);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(sheetNum);
            operHSSFWorkBook(excelFile, beanMap, sheet, rowNum, cellNum, list, nameLen, excelStrategy, names);
            hssfWorkbook.close();

        } else if (filename.endsWith(".xlsx")) {
            XSSFWorkbook xssfWorkbook = getXSSFWorkbook(excelFile);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetNum);
            operXSSFWorkBook(excelFile, beanMap, sheet, rowNum, cellNum, list, nameLen, excelStrategy, names);
            xssfWorkbook.close();

        } else {
            throw new RuntimeException("文件格式错误");
        }

        return list;
    }

    /**
     * 读取excel
     *
     * @param excelFile
     *            excel文件
     * @param beanMap
     *            封装对象
     * @param rowNum
     *            起始行数，从0开始
     * @param cellNum
     *            起始列数，从0开始
     * @param excelStrategy
     *            单元格值的策略，用来定制结果
     * @param names
     *            对应数据库的字段名
     * @return List<T extends HashMap>
     *
     * @throws Exception
     */
    public static <T extends HashMap> List<T> read(File excelFile, T beanMap, int rowNum, int cellNum,
                                                     ExcelNameStrategy excelStrategy, String... names) throws Exception {
        List<T> list = new ArrayList<T>();
        int nameLen = names.length;
        String filename = excelFile.getName();
        if (filename.endsWith(".xls")) {
            HSSFWorkbook hssfWorkbook = getHSSFWorkbook(excelFile);
            int sheetCnt = hssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetCnt; i++) {
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                operHSSFWorkBook(excelFile, beanMap, sheet, rowNum, cellNum, list, nameLen, excelStrategy, names);
            }
            hssfWorkbook.close();

        } else if (filename.endsWith(".xlsx")) {
            XSSFWorkbook xssfWorkbook = getXSSFWorkbook(excelFile);
            int sheetCnt = xssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetCnt; i++) {
                XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
                operXSSFWorkBook(excelFile, beanMap, sheet, rowNum, cellNum, list, nameLen, excelStrategy, names);
            }
            xssfWorkbook.close();

        } else {
            throw new RuntimeException("文件格式错误");
        }

        return list;
    }

    /**
     * 读取excel
     *
     * @param excelFile
     *            excel文件
     * @param sheetNum
     *            第几个sheet
     * @param rowNum
     *            起始行数，从0开始
     * @param cellNum
     *            起始列数，从0开始
     * @param excelStrategy
     *            单元格值的策略，用来定制结果
     * @return 返回List<String[]>
     * @throws Exception
     */
    public static List<String[]> read(File excelFile, int sheetNum, int rowNum, int cellNum,
                                      ExcelIndexStrategy excelStrategy) throws Exception {
        List<String[]> list = new ArrayList<String[]>();
        String filename = excelFile.getName();
        if (filename.endsWith(".xls")) {
            HSSFWorkbook hssfWorkbook = getHSSFWorkbook(excelFile);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(sheetNum);
            operHSSFWorkBookList(excelFile, sheet, rowNum, cellNum, list, excelStrategy);
            hssfWorkbook.close();
        } else if (filename.endsWith(".xlsx")) {
            XSSFWorkbook xssfWorkbook = getXSSFWorkbook(excelFile);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetNum);
            operXSSFWorkBookList(excelFile, sheet, rowNum, cellNum, list, excelStrategy);
            xssfWorkbook.close();
        } else {
            throw new RuntimeException("文件格式错误");
        }

        return list;
    }

    /**
     * 读取excel
     *
     * @param excelFile
     *            excel文件
     * @param rowNum
     *            起始行数，从0开始
     * @param cellNum
     *            起始列数，从0开始
     * @param excelStrategy
     *            单元格值的策略，用来定制结果
     * @return 返回List<String[]>
     * @throws Exception
     */
    public static List<String[]> read(File excelFile, int rowNum, int cellNum,
                                      ExcelIndexStrategy excelStrategy) throws Exception {
        List<String[]> list = new ArrayList<String[]>();
        String filename = excelFile.getName();
        if (filename.endsWith(".xls")) {
            HSSFWorkbook hssfWorkbook = getHSSFWorkbook(excelFile);
            int sheetCnt = hssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetCnt; i++) {
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                operHSSFWorkBookList(excelFile, sheet, rowNum, cellNum, list, excelStrategy);
            }
            hssfWorkbook.close();
        } else if (filename.endsWith(".xlsx")) {
            XSSFWorkbook xssfWorkbook = getXSSFWorkbook(excelFile);
            int sheetCnt = xssfWorkbook.getNumberOfSheets();
            for (int i = 0; i < sheetCnt; i++) {
                XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
                operXSSFWorkBookList(excelFile, sheet, rowNum, cellNum, list, excelStrategy);
            }
            xssfWorkbook.close();
        } else {
            throw new RuntimeException("文件格式错误");
        }

        return list;
    }

    /**
     * 读取excel
     *
     * @param excelFile
     *            excel文件
     * @param sheetNum
     *            第几个sheet
     * @param rowNum
     *            起始行数，从0开始
     * @param cellNum
     *            起始列数，从0开始
     * @return 返回List<String[]>
     * @throws Exception
     */
    public static List<String[]> read(File excelFile, int sheetNum, int rowNum, int cellNum) throws Exception {
        return read(excelFile, sheetNum, rowNum, cellNum, new ExcelIndexStrategy() {
            public Strategy get(int index, String value) {
                return Strategy.val(value);
            }
        });
    }

    private static void operHSSFWorkBookList(File excelFile, HSSFSheet sheet, int rowNum, int cellNum, List<String[]> list,
                                             ExcelIndexStrategy excelStrategy) throws Exception {
        int lastRowNum = sheet.getLastRowNum();
        for (int i = rowNum; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            if (isEmptyRow(row)) {//遇到空行 跳过
                continue;
            }
            short lastCellNum = row.getLastCellNum();
            String[] strs = new String[lastCellNum - cellNum];
            int index = 0;
            boolean isAvailable = true;
            for (int j = cellNum; j <= lastCellNum; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    String cellValue = null;
                    switch (cellType) {
                        case NUMERIC:
                            boolean cellDateFormatted = HSSFDateUtil.isCellDateFormatted(cell);
                            if (!cellDateFormatted) {
                                BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                cellValue = bd.toString();
                            }else {
                                Date dateCellValue = cell.getDateCellValue();
                                cellValue = DateUtil.dateTimeToString(DateUtil.dateToLocalDateTime(dateCellValue));
                            }
                            break;
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        default:
                            cellValue = "";
                            break;
                    }
                    Strategy strategy = excelStrategy.get(j, cellValue);
                    if (strategy.isAvailable()) {
                        strs[index++] = strategy.getValue();
                    }else{
                        isAvailable = false;
                    }
                }
            }
            if(isAvailable  ){
                list.add(strs);
            }
        }
    }

    private static void operXSSFWorkBookList(File excelFile, XSSFSheet sheet, int rowNum, int cellNum, List<String[]> list,
                                             ExcelIndexStrategy excelStrategy) throws Exception {
        int lastRowNum = sheet.getLastRowNum();
        for (int i = rowNum; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            if (isEmptyRow(row)) {
                continue;
            }
            short lastCellNum = row.getLastCellNum();
            String[] strs = new String[lastCellNum - cellNum];
            int index = 0;
            boolean isAvailable = true;
            for (int j = cellNum; j <= lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    String cellValue = null;
                    switch (cellType) {
                        case NUMERIC:
                            boolean cellDateFormatted = HSSFDateUtil.isCellDateFormatted(cell);
                            if (!cellDateFormatted) {
                                BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                cellValue = bd.toString();
                            }else {
                                Date dateCellValue = cell.getDateCellValue();
                                cellValue = DateUtil.dateTimeToString(DateUtil.dateToLocalDateTime(dateCellValue));
                            }
                            break;
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        default:
                            cellValue = "";
                            break;
                    }
                    Strategy strategy = excelStrategy.get(j, cellValue);
                    if (strategy.isAvailable()) {
                        strs[index++] = strategy.getValue();
                    }else{
                        isAvailable = false;
                    }
                }
            }
            if(isAvailable ){
                list.add(strs);
            }
        }
    }

    @SuppressWarnings({"unchecked" })
    private static <T extends HashMap> void operXSSFWorkBook(File excelFile, T beanMap, XSSFSheet sheet, int rowNum,
                                                               int cellRow, List<T> list, int nameLen, ExcelNameStrategy excelStrategy, String[] names) throws Exception {
        int lastRowNum = sheet.getLastRowNum();
        for (int i = rowNum; i <= lastRowNum; i++) {
            T t = (T) beanMap.clone();
            XSSFRow row = sheet.getRow(i);
            if (isEmptyRow(row)) {
                continue;
            }
            short lastCellNum = row.getLastCellNum();
            boolean isAvailable=true;
            for (int j = cellRow; j <= lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                String cellValue = null;
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    switch (cellType) {
                        case NUMERIC:
                            boolean cellDateFormatted = HSSFDateUtil.isCellDateFormatted(cell);
                            if (!cellDateFormatted) {
                                BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                cellValue = bd.toString();
                            }else {
                                Date dateCellValue = cell.getDateCellValue();
                                cellValue = DateUtil.dateTimeToString(DateUtil.dateToLocalDateTime(dateCellValue));
                            }
                            break;
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        default:
                            cellValue = "";
                            break;
                    }
                }
                if (j < nameLen) {
                    Strategy strategy = excelStrategy.get(names[j], cellValue);
                    if(strategy.isAvailable()){
                        t.put(names[j], strategy.getValue());
                    }else{
                        isAvailable = false;
                    }
                }
            }

            if(isAvailable){
                list.add(t);
            }
        }

    }

    @SuppressWarnings({"unchecked" })
    private static <T extends HashMap> void operHSSFWorkBook(File excelFile, T beanMap, HSSFSheet sheet, int rowNum,
                                                               int cellRow, List<T> list, int nameLen, ExcelNameStrategy excelStrategy, String... names) throws Exception {
        int lastRowNum = sheet.getLastRowNum();
        for (int i = rowNum; i <= lastRowNum; i++) {
            T t = (T) beanMap.clone();
            HSSFRow row = sheet.getRow(i);
            if (isEmptyRow(row)) {
                continue;
            }
            short lastCellNum = row.getLastCellNum();
            boolean isAvailable = true;
            for (int j = cellRow; j <= lastCellNum; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    String cellValue = null;
                    switch (cellType) {
                        case NUMERIC:
                            boolean cellDateFormatted = HSSFDateUtil.isCellDateFormatted(cell);
                            if (!cellDateFormatted) {
                                BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                cellValue = bd.toString();
                            }else {
                                Date dateCellValue = cell.getDateCellValue();
                                cellValue = DateUtil.dateTimeToString(DateUtil.dateToLocalDateTime(dateCellValue));
                            }
                            break;
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        default:
                            cellValue = "";
                            break;
                    }
                    if (j < nameLen) {
                        Strategy strategy = excelStrategy.get(names[j], cellValue);
                        if(strategy.isAvailable()){
                            t.put(names[j], strategy.getValue());
                        }else{
                            isAvailable=false;
                            break;
                        }
                    }
                }
            }
            if(isAvailable){
                list.add(t);
            }
        }
    }

    public static HSSFWorkbook getHSSFWorkbook(File excelFile) throws Exception {
        FileInputStream s = new FileInputStream(excelFile);
        HSSFWorkbook wb = new HSSFWorkbook(s);
        s.close();
        return wb;
    }

    public static XSSFWorkbook getXSSFWorkbook(File excelFile) throws Exception {
        FileInputStream s = new FileInputStream(excelFile);
        XSSFWorkbook wb = new XSSFWorkbook(s);
        s.close();
        return wb;
    }


    /**
     * Checks if a {@code Row} is {@code null}, empty or all cells in this row are blank.
     * @param row
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isEmptyRow(Row row) {
        if (row == null || row.toString().isEmpty()) {
            return true;
        } else {
            Iterator<Cell> it = row.iterator();
            boolean isEmpty = true;
            while (it.hasNext()) {
                Cell cell = it.next();
                if (cell.getCellType() !=  CellType.BLANK) {
                    isEmpty = false;
                    break;
                }
            }
            return isEmpty;
        }
    }
}
