package com.jup.jupging.domain.report.service;

import java.util.List;

import com.jup.jupging.domain.report.dto.ReportDetailDto;
import com.jup.jupging.domain.report.dto.ReportReq;
import com.jup.jupging.domain.report.dto.ReportSummaryDto;


public interface IReportService {
	int insertReport(ReportReq req);

	List<ReportSummaryDto> findMyReports(Long memberId);
	
	ReportDetailDto findMyReportDetail(Long reportId, Long memberId);
	
	ReportDetailDto findReportDetail(Long reportId);

	boolean pickReport(Long reportId);

	List<ReportDetailDto> getReportsByTrailId(Long trailId);
}
