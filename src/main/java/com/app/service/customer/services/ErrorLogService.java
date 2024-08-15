package com.app.service.customer.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogService {
	@Async
	public void generateErrorLogExcelFile(Map<Integer, Map<String, String>> errorsMap) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet xssfSheet = workbook.createSheet();
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setFillBackgroundColor(HSSFColorPredefined.BLACK.getIndex());
			int rowNum = 0;
			List<Integer> rowList = errorsMap.keySet().stream().collect(Collectors.toList());
			XSSFRow xssfRow = xssfSheet.createRow(rowNum++);
			XSSFCell cell1 = xssfRow.createCell(0);
			cell1.setCellValue("ROW NUMBER");
			cell1.setCellStyle(cellStyle);
			XSSFCell cell2 = xssfRow.createCell(1);
			cell2.setCellValue("ERROR COLUMN");
			cell2.setCellStyle(cellStyle);
			XSSFCell cell3 = xssfRow.createCell(2);
			cell3.setCellValue("ERROR DESCRIPTION");
			cell3.setCellStyle(cellStyle);
			for (int i = 0; i < rowList.size(); i++) {
				rowNum = addRows(xssfSheet, rowNum++, rowList.get(i), errorsMap.get(rowList.get(i)), cellStyle);
			}
			Path path = Paths.get("/users/vvravikiran/downloads/" + LocalDateTime.now() + ".xlsx");
			try {
				File file = path.toFile();
				file.setReadable(true);
				FileOutputStream out = new FileOutputStream(file);
				workbook.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int addRows(XSSFSheet xssfSheet, int rowCount, int rowNum, Map<String, String> errorColumns,
			XSSFCellStyle cellStyle) {
		List<String> errorColumnNames = errorColumns.keySet().stream().collect(Collectors.toList());
		for (int i = 0; i < errorColumnNames.size(); i++) {
			XSSFRow hssfRow = xssfSheet.createRow(rowCount++);
			hssfRow.createCell(0).setCellValue("Row " + rowNum);
			hssfRow.createCell(1).setCellValue(errorColumnNames.get(i));
			hssfRow.createCell(2).setCellValue(errorColumns.get(errorColumnNames.get(i)));
		}
		return rowCount;
	}
}
