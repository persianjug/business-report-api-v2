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

  /**
   * 最新の業務報告書を1件取得します。
   * 
   * @return 最新のReportオブジェクト
   */
  Report findLatestReport();

  /**
   * 最新の業務報告書のIDを取得します。
   * 
   * @return 最新の報告書のID、存在しない場合はnull
   */
  Long findLatestReportId();
}
