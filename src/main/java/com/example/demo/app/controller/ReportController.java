package com.example.demo.app.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.Report;
import com.example.demo.domain.service.ReportExcelService;
import com.example.demo.domain.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;
  private final ReportExcelService reportExcelService;

  /**
   * 業務報告書を新規作成します。
   * 
   * @param report 作成する報告書データ
   * @return 作成された報告書オブジェクトとHTTPステータス
   */
  @PostMapping
  public ResponseEntity<Report> createReport(@RequestBody Report report) {
    // デフォルトでstatusが設定されていない場合は"published"(公開済)と仮定
    if (report.getStatus() == null || report.getStatus().isEmpty()) {
      report.setStatus("published");
    }

    Report createdReport = reportService.createReport(report);
    return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
  }

  /**
   * 全ての業務報告書を取得します。
   * 
   * @return 業務報告書のリストとHTTPステータス
   */
  @GetMapping
  public ResponseEntity<List<Report>> getAllReports() {
    // List<Report> reports = reportService.getAllReports();
    List<Report> reports = reportService.getAllReports();
    return new ResponseEntity<>(reports, HttpStatus.OK);
  }

  /**
   * IDで業務報告書を取得します。
   * 
   * @param id 報告書ID
   * @return 該当する報告書オブジェクトとHTTPステータス
   */
  @GetMapping("/{id}")
  public ResponseEntity<Report> getReportById(@PathVariable("id") Long id) {
    Report report = reportService.getReportById(id);
    if (report != null) {
      return new ResponseEntity<>(report, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * IDを指定して業務報告書をExcelファイルで出力します。
   * 
   * @param id 報告書ID
   * @return Excelファイルのバイト配列
   * @throws IOException
   */
  @GetMapping("/{id}/excel")
  public ResponseEntity<byte[]> downloadExcel(@PathVariable("id") Long id) throws IOException {
    Report report = reportService.getReportById(id);
    if (report == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    byte[] excelBytes = reportExcelService.generateExcel(report);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "report_" + id + ".xlsx");

    return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
  }

  /**
   * 業務報告書を更新します。
   * 
   * @param id     更新対象の報告書ID
   * @param report 更新する報告書データ
   * @return 更新された報告書オブジェクト
   */
  @PutMapping("/{id}")
  public ResponseEntity<Report> updateReport(@PathVariable("id") Long id, @RequestBody Report report) {
    report.setId(id);
    // デフォルトでstatusが設定されていない場合は"published"と仮定
    if (report.getStatus() == null || report.getStatus().isEmpty()) {
      report.setStatus("published");
    }

    Report updatedReport = reportService.updateReport(report);
    return new ResponseEntity<>(updatedReport, HttpStatus.OK);
  }

  /**
   * 業務報告書を削除します。
   * 
   * @param id 削除する報告書ID
   * @return HTTPステータス
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReport(@PathVariable("id") Long id) {
    reportService.deleteReport(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * 最新の業務報告書を1件取得します。
   * 
   * @return 最新の報告書オブジェクト
   */
  @GetMapping("/latest")
  public ResponseEntity<Report> getLatestReport() {
    Report latestReport = reportService.getLatestReport();
    if (latestReport != null) {
      return new ResponseEntity<>(latestReport, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * 報告書が1件以上存在するか確認します。
   * 
   * @return 存在する場合はtrue
   */
  @GetMapping("/has-data")
  public ResponseEntity<Boolean> hasReports() {
    return new ResponseEntity<>(reportService.hasReports(), HttpStatus.OK);
  }

  /**
   * 下書き報告書の一覧取得
   * @return 作成された報告書オブジェクトとHTTPステータス
   */
  @GetMapping("/drafts")
  public ResponseEntity<List<Report>> getAllDrafts() {
    List<Report> drafts = reportService.getDraftReports();
    return new ResponseEntity<>(drafts, HttpStatus.OK);
  }

  /**
   * 登録済み報告書の一覧取得
   * @return 作成された報告書オブジェクトとHTTPステータス
   */
  @GetMapping("/published")
  public ResponseEntity<List<Report>> getAllpublished() {
    List<Report> published = reportService.getPublishedReports();
    return new ResponseEntity<>(published, HttpStatus.OK);
  }


}
