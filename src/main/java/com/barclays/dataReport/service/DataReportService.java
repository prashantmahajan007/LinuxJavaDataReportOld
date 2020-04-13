package com.barclays.dataReport.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.barclays.dataReport.entity.LinuxJavaDataSheet;
import com.barclays.dataReport.util.ExcelUtil;

@Service
public class DataReportService {

	private final ExcelUtil excelUtil;

	public DataReportService(ExcelUtil excelUtil) {
		this.excelUtil = excelUtil;
	}

	public List<LinuxJavaDataSheet> getLinuxJavaDataReport(MultipartFile file) throws Exception {

		Path tempDir = Files.createTempDirectory("");
		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
		file.transferTo(tempFile);

		Workbook workbook = WorkbookFactory.create(tempFile);
		Sheet sheet = workbook.getSheetAt(0);
		Supplier<Stream<Row>> rowStreamSupplier = excelUtil.getRowStreamSupplier(sheet);

		return rowStreamSupplier.get()
				.skip(1) // as it is a header.
				.map(row -> {
					return populateLinuxJavaDataSheetByRow(row); 
				})
				.collect(Collectors.toList());
	}

	private LinuxJavaDataSheet populateLinuxJavaDataSheetByRow(Row row) {
		LinuxJavaDataSheet dataReportObj = new LinuxJavaDataSheet();
		dataReportObj.setPlatform(getStringValue(row.getCell(0)));
		dataReportObj.setServerName(getStringValue(row.getCell(1)));
		dataReportObj.setEnv(getStringValue(row.getCell(2)));
		dataReportObj.setTc(getStringValue(row.getCell(3)));
		dataReportObj.setService(getStringValue(row.getCell(4)));
		dataReportObj.setItsi(getStringValue(row.getCell(5)));
		dataReportObj.setRtbManager(getStringValue(row.getCell(6)));
		dataReportObj.setRtbLead(getStringValue(row.getCell(7)));
		dataReportObj.setIsPrimary(getBooleanValue(row.getCell(8)));
		dataReportObj.setJavaLocation(getStringValue(row.getCell(9)));
		dataReportObj.setJavaClass(getStringValue(row.getCell(10)));
		dataReportObj.setFileVersion(getStringValue(row.getCell(11)));
		dataReportObj.setJavaVersion(getIntegerValue(row.getCell(12)));
		dataReportObj.setJavaType(getStringValue(row.getCell(13)));
		dataReportObj.setPbtCiName(getStringValue(row.getCell(14)));
		dataReportObj.setCommandLastExecuted(getDateValue(row.getCell(15)));
		dataReportObj.setDormancy(getStringValue(row.getCell(16)));
		dataReportObj.setLowCritCount(getIntegerValue(row.getCell(17)));
		dataReportObj.setMedCritCount(getIntegerValue(row.getCell(18)));
		dataReportObj.setHighCritCount(getIntegerValue(row.getCell(19)));
		dataReportObj.setUtilityServer(getStringValue(row.getCell(20)));
		dataReportObj.setUtilityName(getStringValue(row.getCell(21)));
		dataReportObj.setVendor(getStringValue(row.getCell(22)));
		dataReportObj.setEmbeddedType(getStringValue(row.getCell(23)));
		dataReportObj.setJavaClass2(getStringValue(row.getCell(24)));
		dataReportObj.setSuspectedLatestJavaVersion(getStringValue(row.getCell(25)));
		dataReportObj.setComments(getStringValue(row.getCell(26)));
		dataReportObj.setProposedDate(getDateValue(row.getCell(27)));
		return dataReportObj;
	}

	private String getStringValue(Cell cell) {
		return (cell != null && cell.getCellType() == CellType.STRING) ?  cell.getStringCellValue() : "";
	}

	private Date getDateValue(Cell cell) {
		return cell != null ?  cell.getDateCellValue() : null;
	}

	private Integer getIntegerValue(Cell cell) {
		return (cell != null && cell.getCellType() == CellType.NUMERIC) ?  (int)cell.getNumericCellValue() : null;
	}

	private Boolean getBooleanValue(Cell cell) {
		return (cell != null && cell.getCellType() == CellType.BOOLEAN) ?  cell.getBooleanCellValue() : Boolean.FALSE;
	}

}
