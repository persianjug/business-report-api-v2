package com.example.demo.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.model.Report;
import com.example.demo.domain.model.Task;

@Mapper
public interface ReportMapper {
  int insertReport(Report report);

  void insertTasks(@Param("reportId") Long reportId, @Param("tasks") List<Task> tasks);

  Report findByIdWithTasks(@Param("id") Long id);

  List<Report> findAllWithTasks();

  int updateReport(Report report);

  void deleteTasksByReportId(Long reportId);

  int deleteReport(Long id);

  Report findLatestReport();

  Long findLatestReportId();

  List<Report> findByStatus(String status);
}
