package com.jup.jupging.domain.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jup.jupging.domain.report.dto.ReportDetailDto;
import com.jup.jupging.domain.report.dto.ReportReq;
import com.jup.jupging.domain.report.dto.ReportSummaryDto;

@Mapper
public interface ReportMapper {
	int insertReport(ReportReq req);
	List<ReportSummaryDto> findMyReports(Long memberId);
	ReportDetailDto findMyReportDetail(@Param("memberId") Long memberId, @Param("reportId") Long reportId);
	ReportDetailDto findReportDetail(@Param("reportId") Long reportId);
	int pickReport(Long reportId);
	List<ReportDetailDto> getReportsByTrailId(Long trailId);
	
}
