package com.example.demo.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.mapper.ReportMapper;
import com.example.demo.domain.model.Report;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
  private final ReportMapper reportMapper;

  /**
   * 新しい業務報告書を登録します。
   * 
   * @param report 登録する報告書データ
   * @return 登録された報告書オブジェクト
   */
  @Transactional
  public Report createReport(Report report) {
    // report.setCreatedAt(LocalDate.now());
    reportMapper.insertReport(report);

    // タスクが存在する場合のみ挿入
    if (report.getTasks() != null && !report.getTasks().isEmpty()) {
      reportMapper.insertTasks(report.getId(), report.getTasks());
    }

    return report;
  }

  /**
   * IDで業務報告書を取得します。
   * 
   * @param id 報告書ID
   * @return 該当する報告書オブジェクト
   */
  public Report getReportById(Long id) {
    return reportMapper.findByIdWithTasks(id);
  }

  /**
   * 全ての業務報告書を取得します。
   * 
   * @return 報告書オブジェクトのリスト
   */
  public List<Report> getAllReports() {
    return reportMapper.findAllWithTasks();
  }

  /**
   * 業務報告書を更新します。
   * 
   * @param report 更新する報告書データ
   * @return 更新された報告書オブジェクト
   */
  @Transactional
  public Report updateReport(Report report) {
    // 報告書本体を更新
    reportMapper.updateReport(report);

    // 既存のタスクをすべて削除
    reportMapper.deleteTasksByReportId(report.getId());

    // 新しいタスクを再挿入
    if (report.getTasks() != null && !report.getTasks().isEmpty()) {
      reportMapper.insertTasks(report.getId(), report.getTasks());
    }

    return report;
  }

  /**
   * IDを指定して業務報告書を削除します。
   * 
   * @param id 削除する報告書ID
   */
  @Transactional
  public void deleteReport(Long id) {
    // 関連するタスクを先に削除
    reportMapper.deleteTasksByReportId(id);
    // 報告書本体を削除
    reportMapper.deleteReport(id);
  }

  /**
   * 最新の業務報告書を取得します。
   * 
   * @return 最新のReportオブジェクト
   */
  public Report getLatestReport() {
    return reportMapper.findLatestReport();
  }

  /**
   * 報告書が1件以上存在するか確認します。
   * 
   * @return 存在する場合はtrue
   */
  public boolean hasReports() {
    return reportMapper.findLatestReportId() != null;
  }

  /**
   * 登録済みの業務報告書を取得します。
   * 
   * @return 登録済みのReportオブジェクト
   */
  public List<Report> getPublishedReports() {
    return reportMapper.findByStatus("published");
  }

  /**
   * 下書きの業務報告書を取得します。
   * 
   * @return 下書きのReportオブジェクト
   */
  public List<Report> getDraftReports() {
    return reportMapper.findByStatus("draft");
  }

}
