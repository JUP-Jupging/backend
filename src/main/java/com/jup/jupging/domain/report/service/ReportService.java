package com.jup.jupging.domain.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jup.jupging.domain.report.dto.ReportDetailDto;
import com.jup.jupging.domain.report.dto.ReportReq;
import com.jup.jupging.domain.report.dto.ReportSummaryDto;
import com.jup.jupging.domain.report.entity.Report;
import com.jup.jupging.domain.report.mapper.ReportMapper;


@Service
public class ReportService implements IReportService{
	
	@Autowired
	private ReportMapper reportMapper; 

	@Override
	public int insertReport(ReportReq req) {
		return reportMapper.insertReport(req);
	}
	
	@Override
	public List<ReportSummaryDto> findMyReports(Long memberId){
		return reportMapper.findMyReports(memberId);
	}

	@Override
	public ReportDetailDto findMyReportDetail(Long reportId, Long memberId) {
		return reportMapper.findMyReportDetail(reportId, memberId);
	}

	@Override
	public ReportDetailDto findReportDetail(Long reportId) {
		return reportMapper.findReportDetail(reportId);
	}

	@Override
	public boolean pickReport(Long reportId) {
		return reportMapper.pickReport(reportId) > 0;
	}

	@Override
	public List<ReportDetailDto> getReportsByTrailId(Long trailId) {
		return reportMapper.getReportsByTrailId(trailId);
	}
}
