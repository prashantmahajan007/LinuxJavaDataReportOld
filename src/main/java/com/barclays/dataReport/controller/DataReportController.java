package com.barclays.dataReport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.barclays.dataReport.entity.LinuxJavaDataSheet;
import com.barclays.dataReport.service.DataReportService;

@RestController
public class DataReportController {
	
	private final DataReportService dataReportService;
	
	public DataReportController(DataReportService uploadService) {
		this.dataReportService = uploadService;
	}

	@GetMapping("/dataReport")
	public List<LinuxJavaDataSheet> upload(@RequestParam("file") MultipartFile file) throws Exception{
		return dataReportService.getLinuxJavaDataReport(file);
	}
}
