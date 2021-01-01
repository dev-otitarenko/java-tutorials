package com.maestro.lib.excel;

import com.maestro.lib.excel.utils.ConvertUtils;
import com.maestro.lib.excel.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelWriter {
    public static byte[] writeFile(final List<String> columns, final List<HashMap<String, Object>> data) throws IOException {
        File tmpFile = File.createTempFile("data", ".xlsx");
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Query");

        CellStyle dataStyle = ExcelUtils.getDataStyle(sheet);
        {
            CellStyle hdrRowStyle = ExcelUtils.getHeaderStyle(sheet);
            Row row = sheet.createRow(0);
            int ncol = 0;
            ExcelUtils.setHeaderCell(row, 0, "", hdrRowStyle);
            for (String col : columns) {
                ExcelUtils.setHeaderCell(row, ++ncol, col, hdrRowStyle);
            }
        }

        {
            int nrow = 1;
            for (HashMap<String, Object> rdata : data) {
                Row row = sheet.createRow(++nrow);
                int ncol = 0;
                ExcelUtils.setDataCell(row, 0, nrow - 1, dataStyle);

                for (String col : columns) {
                    fillDataCell(dataStyle, row, ++ncol, rdata.get(col));
                }
            }
        }

        // AutoResize all columns
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet s = sheetIterator.next();
            for (int i = 0; i < columns.size(); i++) {
                s.autoSizeColumn(i, true);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(tmpFile);

        // write this workbook to OutputStream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();

        return ConvertUtils.convert2bytes_array(tmpFile);
    }

    private static void fillDataCell(CellStyle dataStyle, Row row, int ncol, Object v) {
        if (v != null) {
            if (v instanceof Date) {
                ExcelUtils.setDataCell(row, ncol, (Date) v, dataStyle);
            } else if (v instanceof Double) {
                ExcelUtils.setDataCell(row, ncol, Double.parseDouble(v.toString()), dataStyle);
            } else if (v instanceof Long) {
                ExcelUtils.setDataCell(row, ncol, Long.parseLong(v.toString()), dataStyle);
            } else {
                ExcelUtils.setDataCell(row, ncol, v.toString(), dataStyle);
            }
        } else {
            ExcelUtils.setDataCell(row, ++ncol, "", dataStyle);
        }
    }
}
