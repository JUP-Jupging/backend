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
	public int insertReport(ReportReq req, Long memberId) {
		
		Map<String,Integer> categoryMap;
		if(req.getCategoryCounts() != null) {
			categoryMap = req.getCategoryCounts();
		}else {
			categoryMap = Map.of();
		}
	
	//DTO를 받아 엔티티로 바꾸기

		Report r = new Report();
		r.setMemberId(memberId);
		r.setTrailId(req.getTrailId());
		r.setTitle(req.getTitle());
		r.setLat(req.getLat());
		r.setLng(req.getLng());
		r.setImageUrl(req.getImageUrl());
		r.setIsPicked(req.getIsPicked());
		
		r.setPaper(categoryMap.getOrDefault("paper", 0));
		r.setCan(categoryMap.getOrDefault("can", 0));
		r.setPlastic(categoryMap.getOrDefault("plastic", 0));
		r.setVinyl(categoryMap.getOrDefault("vinyl", 0));
		r.setGlass(categoryMap.getOrDefault("glass", 0));
		r.setStyro(categoryMap.getOrDefault("styro", 0));
		r.setBattery(categoryMap.getOrDefault("battery", 0));
		
	
		return reportMapper.insertReport(r);
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
}
