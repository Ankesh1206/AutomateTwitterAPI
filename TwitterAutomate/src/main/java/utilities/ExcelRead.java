package utilities;

import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRead {
	static XSSFWorkbook wb;
	static XSSFSheet sheet;

	public ExcelRead(String userFilePath, String sheetName) {
		try {
			wb = new XSSFWorkbook(userFilePath);
			sheet = wb.getSheet(sheetName);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}		
	}

	//Get Row Count
	public int getRowCount() {
		int rowCount = sheet.getPhysicalNumberOfRows();
		return rowCount;
	}

	//Get Column Count
	public int  getColCount() {
		int colCount = sheet.getRow(0).getLastCellNum();
		return colCount;
	}

	//Get data from sheet
	public Object getSheetData(int rowNum, int colNum) {
		
		DataFormatter formatter = new DataFormatter();
		Object userName = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
		//String userName = sheet.getRow(1).getCell(0).getStringCellValue();
		return userName;
	}
	
}
