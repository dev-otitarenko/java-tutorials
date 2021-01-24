package com.maestro.lib.excel.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExcelUtils {
    public static Cell setHeaderCell(Row hdrRow, int rn, String title, CellStyle style) {
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(title);
        if (style != null) cell.setCellStyle(style);

        return cell;
    }

    public static Cell setDataCell(Row hdrRow, int rn, String value, CellStyle style ) {
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(StringUtils.isBlank(value) ? "" : value);
        if (style != null) cell.setCellStyle(style);

        return cell;
    }

    public static Cell setDataCell(Row hdrRow, int rn, Long value, CellStyle style ) {
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(value == null ? "" : value.toString());
        if (style != null) cell.setCellStyle(style);

        return cell;
    }

    public static Cell setDataCell(Row hdrRow, int rn, Integer value, CellStyle style ) {
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(value == null ? "" : value.toString());
        if (style != null) cell.setCellStyle(style);

        return cell;
    }
    
    public static Cell setDataCell(Row hdrRow, int rn, Double value, CellStyle style ) {
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(value == null ? "" : new BigDecimal(value.toString()).stripTrailingZeros().toPlainString());
        if (style != null) cell.setCellStyle(style);
        
        return cell;
    }

    public static Cell setDataCell(Row hdrRow, int rn, Float value, CellStyle style ) {
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(value == null ? "" : new BigDecimal(value.toString()).stripTrailingZeros().toPlainString());
        if (style != null) cell.setCellStyle(style);

        return cell;
    }
    
    public static Cell setDataCell(Row hdrRow, int rn, Date value, CellStyle style ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        Cell cell = hdrRow.createCell(rn);
        cell.setCellValue(value == null ? "" : formatter.format(value));
        if (style != null) cell.setCellStyle(style);
        
        return cell;
    }

    public static CellStyle getHeaderStyle (Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.GREY_25_PERCENT.getIndex());

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBottomBorderColor(IndexedColors.DARK_GREEN.getIndex());
        cellStyle.setTopBorderColor(IndexedColors.DARK_GREEN.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.DARK_GREEN.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.DARK_GREEN.getIndex());
        cellStyle.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(font);

        return cellStyle;
    }

    public static CellStyle getDataStyle (Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return cellStyle;
    }

    public static Integer getCellValueAsInteger(Cell cell) {
        Integer ret = null;
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                double value = Double.parseDouble(cell.getStringCellValue());
                ret = (int)value;
            } else if (cell.getCellType() == CellType.NUMERIC) {
                double value = cell.getNumericCellValue();
                ret = (int) value;
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                ret = cell.getBooleanCellValue() ? 1 : 0;
            }
        }
        return ret;
    }
    
    public static Double getCellValueAsNumber(Cell cell) {
        Double ret = null;
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                ret = Double.parseDouble(cell.getStringCellValue());
            } else if (cell.getCellType() == CellType.NUMERIC) {
                ret = cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                ret = cell.getBooleanCellValue() ? Double.parseDouble("1") : Double.parseDouble("0");
            } else if (cell.getCellType() == CellType.BLANK) {
                ret = null;
            }
        }
        return ret;
    }
    
    public static String getCellValueAsString(Cell cell) {
        String strCellValue = null;
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                strCellValue = cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 20022019
                    strCellValue = dateFormat.format(cell.getDateCellValue());
                } else {
                    double value = cell.getNumericCellValue();
                    strCellValue = new BigDecimal(value).stripTrailingZeros().toPlainString();
                }
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                strCellValue = String.valueOf(cell.getBooleanCellValue());
            } else if (cell.getCellType() == CellType.BLANK) {
                strCellValue = "";
            }
        }
        return strCellValue;
    }

    public static Date getCellValueAsDate(Cell cell) {
        Date strCellValue = null;
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                String value = cell.getStringCellValue();
                if (!StringUtils.isBlank(value)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        strCellValue = dateFormat.parse(value);
                    } catch (ParseException e1) {
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            strCellValue = dateFormat.parse(value);
                        } catch (ParseException e2) {
                        }
                    }
                }
            } else if (cell.getCellType() == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    strCellValue = cell.getDateCellValue();
                }
            } else if (cell.getCellType() == CellType.BLANK) {
                strCellValue = null;
            }
        }
        return strCellValue;
    }
}
