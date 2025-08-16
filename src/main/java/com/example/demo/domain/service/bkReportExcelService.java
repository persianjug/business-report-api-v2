// package com.example.demo.domain.service;

// import com.example.demo.domain.mapper.ReportMapper;
// import com.example.demo.domain.model.ProjectInfo;
// import com.example.demo.domain.model.Report;
// import com.example.demo.domain.model.Task;
// import lombok.RequiredArgsConstructor;
// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.stereotype.Service;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.time.format.DateTimeFormatter;
// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class ReportExcelService {

//   private final ReportMapper reportMapper;
//   private final ReportService reportService; // ReportServiceも使用します

//   private static final String TEMPLATE_PATH = "excel/report_template.xlsx";
//   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

//   /**
//    * 指定されたIDの業務報告書をExcelファイルとして作成します。
//    *
//    * @param id 業務報告書ID
//    * @return 作成されたExcelファイルのバイト配列
//    * @throws IOException ファイル操作中にエラーが発生した場合
//    */
//   public byte[] createReportExcelById(Long id) throws IOException {
//     // IDに紐づく報告書を取得
//     Optional<Report> optionalReport = reportService.getReportById(id);
//     if (optionalReport.isEmpty()) {
//       throw new IllegalArgumentException("指定されたIDの報告書が見つかりません。");
//     }
//     Report report = optionalReport.get();

//     try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//       Sheet sheet = workbook.createSheet("業務報告書");

//       // スタイル設定
//       CellStyle headerStyle = createHeaderStyle(workbook);
//       CellStyle dataStyle = createDataStyle(workbook);
//       CellStyle multiLineStyle = createMultiLineStyle(workbook);

//       // テンプレートから読み込む代わりに、ここで直接レイアウトを構築
//       int rowNum = 0;
//       Row row;
//       Cell cell;

//       // タイトル
//       row = sheet.createRow(rowNum++);
//       row.setHeightInPoints(25);
//       cell = row.createCell(0);
//       cell.setCellValue("業務報告書");
//       cell.setCellStyle(headerStyle);

//       // 基本情報
//       rowNum = createSectionHeader(sheet, rowNum, "基本情報", headerStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "報告対象期間", headerStyle);
//       createCell(row, 1,
//           report.getStartDate().format(DATE_FORMATTER) + " 〜 " + report.getEndDate().format(DATE_FORMATTER), dataStyle);

//       // 顧客情報
//       rowNum = createSectionHeader(sheet, rowNum, "顧客情報", headerStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "エンド企業", headerStyle);
//       createCell(row, 1, report.getCustomerInfo().getEndClient(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "上位企業", headerStyle);
//       createCell(row, 1, report.getCustomerInfo().getUpperClient(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "業種", headerStyle);
//       createCell(row, 1, report.getCustomerInfo().getIndustry(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "職場最寄駅", headerStyle);
//       createCell(row, 1, report.getCustomerInfo().getNearestStation(), dataStyle);

//       // 案件情報
//       ProjectInfo projectInfo = report.getProjectInfo();
//       rowNum = createSectionHeader(sheet, rowNum, "案件情報", headerStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "案件名", headerStyle);
//       createCell(row, 1, projectInfo.getProjectName(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "参画日", headerStyle);
//       createCell(row, 1, projectInfo.getParticipationDate().format(DATE_FORMATTER), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "参画人数", headerStyle);
//       createCell(row, 1, String.valueOf(projectInfo.getNumberOfParticipants()), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "通勤時間", headerStyle);
//       createCell(row, 1, String.format("%d時間 %d分", projectInfo.getCommuteHours(), projectInfo.getCommuteMinutes()),
//           dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "勤務形態", headerStyle);
//       createCell(row, 1, projectInfo.getWorkStyle().getDisplayValue(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "ポジション", headerStyle);
//       createCell(row, 1, projectInfo.getPosition().getDisplayValue(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "主要技術", headerStyle);
//       createCell(row, 1, projectInfo.getMainTechnology(), dataStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "データベース", headerStyle);
//       createCell(row, 1, projectInfo.getDatabase(), dataStyle);

//       // 進捗状況
//       rowNum = createSectionHeader(sheet, rowNum, "進捗状況", headerStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "全体状況", headerStyle);
//       createCell(row, 1, report.getOverallProgress(), multiLineStyle);

//       // タスク一覧 (動的に行を追加)
//       if (report.getTasks() != null && !report.getTasks().isEmpty()) {
//         row = sheet.createRow(rowNum++);
//         createCell(row, 0, "タスク一覧", headerStyle);
//         rowNum++; // 見出しとタスクの間を空ける
//         createTaskHeaders(sheet, rowNum, headerStyle);
//         rowNum++;

//         for (Task task : report.getTasks()) {
//           row = sheet.createRow(rowNum++);
//           createCell(row, 0, task.getTaskName(), dataStyle);
//           createCell(row, 1, task.getStatus(), multiLineStyle);
//           createCell(row, 2, task.getProblem(), multiLineStyle);
//         }
//         rowNum++; // タスクと次のセクションの間を空ける
//       } else {
//         row = sheet.createRow(rowNum++);
//         createCell(row, 0, "タスク", headerStyle);
//         createCell(row, 1, "タスクは登録されていません。", dataStyle);
//       }

//       // 今後の予定
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "今後の予定", headerStyle);
//       createCell(row, 1, report.getFuturePlans(), multiLineStyle);

//       // その他
//       rowNum = createSectionHeader(sheet, rowNum, "その他", headerStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "顧客状況", headerStyle);
//       createCell(row, 1, report.getOtherInfo().getCustomerStatus(), multiLineStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "営業情報", headerStyle);
//       createCell(row, 1, report.getOtherInfo().getSalesInfo(), multiLineStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "健康状況", headerStyle);
//       createCell(row, 1, report.getOtherInfo().getHealthStatus(), multiLineStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "休暇予定", headerStyle);
//       createCell(row, 1, report.getOtherInfo().getVacationPlans(), multiLineStyle);

//       // 上司への相談
//       rowNum = createSectionHeader(sheet, rowNum, "上司への相談", headerStyle);
//       row = sheet.createRow(rowNum++);
//       createCell(row, 0, "相談内容", headerStyle);
//       createCell(row, 1, report.getConsultation(), multiLineStyle);

//       // Excelファイルをバイト配列に書き込む
//       workbook.write(out);
//       return out.toByteArray();
//     }
//   }

//   // セクションヘッダーを作成するヘルパーメソッド
//   private int createSectionHeader(Sheet sheet, int rowNum, String title, CellStyle style) {
//     Row row = sheet.createRow(rowNum++);
//     row.setHeightInPoints(20);
//     Cell cell = row.createCell(0);
//     cell.setCellValue(title);
//     cell.setCellStyle(style);
//     return rowNum;
//   }

//   // タスクテーブルのヘッダーを作成するヘルパーメソッド
//   private void createTaskHeaders(Sheet sheet, int rowNum, CellStyle style) {
//     Row row = sheet.createRow(rowNum);
//     createCell(row, 0, "タスク名", style);
//     createCell(row, 1, "状況", style);
//     createCell(row, 2, "問題・課題", style);
//   }

//   // セルを作成するヘルパーメソッド
//   private void createCell(Row row, int column, String value, CellStyle style) {
//     Cell cell = row.createCell(column);
//     cell.setCellValue(value != null ? value : "");
//     cell.setCellStyle(style);
//   }

//   // ヘッダースタイルを作成
//   private CellStyle createHeaderStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     Font font = workbook.createFont();
//     font.setBold(true);
//     style.setFont(font);
//     style.setAlignment(HorizontalAlignment.LEFT);
//     style.setVerticalAlignment(VerticalAlignment.CENTER);
//     return style;
//   }

//   // データスタイルを作成
//   private CellStyle createDataStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setAlignment(HorizontalAlignment.LEFT);
//     style.setVerticalAlignment(VerticalAlignment.TOP);
//     return style;
//   }

//   // 複数行対応のデータスタイルを作成
//   private CellStyle createMultiLineStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setAlignment(HorizontalAlignment.LEFT);
//     style.setVerticalAlignment(VerticalAlignment.TOP);
//     style.setWrapText(true);
//     return style;
//   }
// }