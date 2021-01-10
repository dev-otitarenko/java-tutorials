package com.maestro.lib.excel;

import com.maestro.lib.excel.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelLoader {
	private List<String> _params = new ArrayList<>();
	private List<Map<String, String>> _data = new ArrayList<>();
	public List<Map<String, String>> getData() {
		return this._data;
	}
	public List<String> getParams() {
		return this._params;
	}
	
	public void parse(InputStream excelFile) throws IOException {
		Workbook workbook = new XSSFWorkbook(excelFile);
		
		try {
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			Iterator rows = sheet.rowIterator();

			while (rows.hasNext()) {
				row = (Row) rows.next();
				Iterator cells = row.cellIterator();

				if (row.getRowNum() == 0) { // HEADER
					this.outputHeaderRow(cells);
				} else { // DATA
					Map<String, String> _doc = this.outputDataRows(cells);
					if (_doc != null) {
						this._data.add(_doc);
					}
				}
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	private void outputHeaderRow (Iterator cells) {
		while (cells.hasNext()) {
			Cell cell = (Cell) cells.next();
			final String val = ExcelUtils.getCellValueAsString(cell);
			if (!StringUtils.isBlank(val)) {
				_params.add(val);
			} else {
				_params.add(String.format("GENERATE__COL_%d", cell.getColumnIndex()));
			}
		}
	}

	private Map<String, String> outputDataRows(Iterator cells) {
		Cell currentCell;
		int cnt = 0;

		Map<String, String> ret = new HashMap<>();

		while (cells.hasNext()) {
			++cnt;
			if (cnt > _params.size()) break;
			currentCell = (Cell) cells.next();

			final String colName = currentCell.getColumnIndex() >= _params.size() ? null : _params.get(currentCell.getColumnIndex()),
						 cellValue = ExcelUtils.getCellValueAsString(currentCell);
			if (!StringUtils.isBlank(colName) && !StringUtils.isBlank(cellValue))
				ret.put(colName, cellValue);
		}

		return ret;
	}
}
