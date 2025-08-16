// package com.example.demo.domain.service;

// import com.example.demo.domain.model.Report;
// import com.example.demo.domain.model.ProjectInfo;
// import com.example.demo.domain.model.Task;
// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.stereotype.Service;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.time.format.DateTimeFormatter;
// import java.util.Optional;

// @Service
// public class ReportExcelService {

//   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

//   public byte[] generateExcel(Report report) throws IOException {
//     Workbook workbook = new XSSFWorkbook();
//     Sheet sheet = workbook.createSheet("業務報告書");

//     // スタイル設定
//     CellStyle headerStyle = createHeaderStyle(workbook);
//     CellStyle sectionHeaderStyle = createSectionHeaderStyle(workbook);
//     CellStyle dataStyle = createDataStyle(workbook);
//     CellStyle multiLineStyle = createMultiLineStyle(workbook);
//     CellStyle taskHeaderStyle = createTaskHeaderStyle(workbook);

//     int rowNum = 0;

//     // タイトル
//     Row titleRow = sheet.createRow(rowNum++);
//     Cell titleCell = titleRow.createCell(0);
//     titleCell.setCellValue("業務報告書");
//     titleCell.setCellStyle(headerStyle);

//     // 基本情報
//     rowNum++;
//     createSectionHeader(sheet, rowNum++, "基本情報", sectionHeaderStyle);
//     createDataRow(sheet, rowNum++, "報告対象期間",
//         report.getStartDate().format(DATE_FORMATTER) + " 〜 " + report.getEndDate().format(DATE_FORMATTER), dataStyle);
//     createDataRow(sheet, rowNum++, "作成日", report.getCreatedAt().format(DATE_FORMATTER), dataStyle);

//     // 顧客情報
//     rowNum++;
//     createSectionHeader(sheet, rowNum++, "顧客情報", sectionHeaderStyle);
//     createDataRow(sheet, rowNum++, "エンド企業", report.getCustomerInfo().getEndClient(), dataStyle);
//     createDataRow(sheet, rowNum++, "上位企業", report.getCustomerInfo().getUpperClient(), dataStyle);
//     createDataRow(sheet, rowNum++, "業種", report.getCustomerInfo().getIndustry(), dataStyle);
//     createDataRow(sheet, rowNum++, "職場最寄駅", report.getCustomerInfo().getNearestStation(), dataStyle);

//     // 案件情報
//     rowNum++;
//     createSectionHeader(sheet, rowNum++, "案件情報", sectionHeaderStyle);
//     ProjectInfo projectInfo = Optional.ofNullable(report.getProjectInfo()).orElse(new ProjectInfo());
//     createDataRow(sheet, rowNum++, "案件名", projectInfo.getProjectName(), dataStyle);
//     createDataRow(sheet, rowNum++, "参画日",
//         projectInfo.getParticipationDate() != null ? projectInfo.getParticipationDate().format(DATE_FORMATTER) : "",
//         dataStyle);
//     createDataRow(sheet, rowNum++, "参画人数",
//         Optional.ofNullable(projectInfo.getNumberOfParticipants()).map(String::valueOf).orElse(""), dataStyle);
//     createDataRow(sheet, rowNum++, "通勤時間",
//         String.format("%d時間 %d分", projectInfo.getCommuteHours(), projectInfo.getCommuteMinutes()), dataStyle);
//     createDataRow(sheet, rowNum++, "勤務形態",
//         projectInfo.getWorkStyle() != null ? projectInfo.getWorkStyle() : "", dataStyle);
//     createDataRow(sheet, rowNum++, "ポジション",
//         projectInfo.getPosition() != null ? projectInfo.getPosition() : "", dataStyle);
//     createDataRow(sheet, rowNum++, "主要技術", projectInfo.getMainTechnology(), dataStyle);
//     createDataRow(sheet, rowNum++, "データベース", projectInfo.getDatabase(), dataStyle);

//     // 進捗状況
//     rowNum++;
//     createSectionHeader(sheet, rowNum++, "進捗状況", sectionHeaderStyle);
//     createDataRow(sheet, rowNum++, "全体状況", report.getOverallProgress(), multiLineStyle);

//     // タスク一覧 (表形式で出力)
//     rowNum++;
//     if (report.getTasks() != null && !report.getTasks().isEmpty()) {
//       Row taskHeaderRow = sheet.createRow(rowNum++);
//       createTaskHeaderCell(taskHeaderRow, 0, "タスク名", taskHeaderStyle);
//       createTaskHeaderCell(taskHeaderRow, 1, "状況", taskHeaderStyle);
//       createTaskHeaderCell(taskHeaderRow, 2, "問題・課題", taskHeaderStyle);

//       for (Task task : report.getTasks()) {
//         Row taskRow = sheet.createRow(rowNum++);
//         createTaskCell(taskRow, 0, task.getTaskName(), dataStyle);
//         createTaskCell(taskRow, 1, task.getStatus(), multiLineStyle);
//         createTaskCell(taskRow, 2, task.getProblem(), multiLineStyle);
//       }
//     } else {
//       createDataRow(sheet, rowNum++, "タスク", "タスクは登録されていません。", dataStyle);
//     }

//     // 今後の予定
//     rowNum++;
//     createDataRow(sheet, rowNum++, "今後の予定", report.getFuturePlans(), multiLineStyle);

//     // その他
//     rowNum++;
//     createSectionHeader(sheet, rowNum++, "その他", sectionHeaderStyle);
//     createDataRow(sheet, rowNum++, "顧客状況", report.getOtherInfo().getCustomerStatus(), multiLineStyle);
//     createDataRow(sheet, rowNum++, "営業情報", report.getOtherInfo().getSalesInfo(), multiLineStyle);
//     createDataRow(sheet, rowNum++, "健康状況", report.getOtherInfo().getHealthStatus(), multiLineStyle);
//     createDataRow(sheet, rowNum++, "休暇予定", report.getOtherInfo().getVacationPlans(), multiLineStyle);

//     // 上司への相談
//     rowNum++;
//     createSectionHeader(sheet, rowNum++, "上司への相談", sectionHeaderStyle);
//     createDataRow(sheet, rowNum++, "相談内容", report.getConsultation(), multiLineStyle);

//     // サイズを調整
//     for (int i = 0; i < 3; i++) {
//       sheet.autoSizeColumn(i);
//     }

//     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//     workbook.write(outputStream);
//     workbook.close();
//     return outputStream.toByteArray();
//   }

//   // ヘルパーメソッド: データを書き込む行を作成
//   private void createDataRow(Sheet sheet, int rowNum, String label, String value, CellStyle valueStyle) {
//     Row row = sheet.createRow(rowNum);
//     Cell labelCell = row.createCell(0);
//     labelCell.setCellValue(label);
//     labelCell.setCellStyle(createLabelStyle(sheet.getWorkbook())); // ラベル用のスタイル

//     Cell valueCell = row.createCell(1);
//     valueCell.setCellValue(Optional.ofNullable(value).orElse(""));
//     valueCell.setCellStyle(valueStyle);
//   }

//   // ヘルパーメソッド: セクションヘッダーを作成
//   private void createSectionHeader(Sheet sheet, int rowNum, String title, CellStyle style) {
//     Row row = sheet.createRow(rowNum);
//     Cell cell = row.createCell(0);
//     cell.setCellValue(title);
//     cell.setCellStyle(style);
//   }

//   // ヘルパーメソッド: タスクヘッダーセルを作成
//   private void createTaskHeaderCell(Row row, int column, String label, CellStyle style) {
//     Cell cell = row.createCell(column);
//     cell.setCellValue(label);
//     cell.setCellStyle(style);
//   }

//   // ヘルパーメソッド: タスクデータセルを作成
//   private void createTaskCell(Row row, int column, String value, CellStyle style) {
//     Cell cell = row.createCell(column);
//     cell.setCellValue(Optional.ofNullable(value).orElse(""));
//     cell.setCellStyle(style);
//   }

//   // セルスタイル定義
//   private CellStyle createHeaderStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setAlignment(HorizontalAlignment.CENTER);
//     style.setVerticalAlignment(VerticalAlignment.CENTER);
//     Font font = workbook.createFont();
//     font.setBold(true);
//     font.setFontHeightInPoints((short) 14);
//     style.setFont(font);
//     return style;
//   }

//   private CellStyle createSectionHeaderStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//     style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//     Font font = workbook.createFont();
//     font.setBold(true);
//     style.setFont(font);
//     return style;
//   }

//   private CellStyle createLabelStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//     style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//     style.setBorderBottom(BorderStyle.THIN);
//     style.setBorderTop(BorderStyle.THIN);
//     style.setBorderLeft(BorderStyle.THIN);
//     style.setBorderRight(BorderStyle.THIN);
//     return style;
//   }

//   private CellStyle createDataStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setBorderBottom(BorderStyle.THIN);
//     style.setBorderTop(BorderStyle.THIN);
//     style.setBorderLeft(BorderStyle.THIN);
//     style.setBorderRight(BorderStyle.THIN);
//     return style;
//   }

//   private CellStyle createMultiLineStyle(Workbook workbook) {
//     CellStyle style = createDataStyle(workbook);
//     style.setWrapText(true);
//     return style;
//   }

//   private CellStyle createTaskHeaderStyle(Workbook workbook) {
//     CellStyle style = workbook.createCellStyle();
//     style.setAlignment(HorizontalAlignment.CENTER);
//     style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
//     style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//     style.setBorderBottom(BorderStyle.THIN);
//     style.setBorderTop(BorderStyle.THIN);
//     style.setBorderLeft(BorderStyle.THIN);
//     style.setBorderRight(BorderStyle.THIN);
//     return style;
//   }
// }