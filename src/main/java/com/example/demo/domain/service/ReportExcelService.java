package com.example.demo.domain.service;

import com.example.demo.domain.model.Report;
import com.example.demo.domain.model.ProjectInfo;
import com.example.demo.domain.model.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ReportExcelService {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  public byte[] generateExcel(Report report) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("業務報告書");

    // スタイル設定
    CellStyle titleStyle = createTitleStyle(workbook);
    CellStyle sectionHeaderStyle = createSectionHeaderStyle(workbook);
    CellStyle labelStyle = createLabelStyle(workbook);
    CellStyle dataStyle = createDataStyle(workbook);
    CellStyle multiLineDataStyle = createMultiLineDataStyle(workbook);
    CellStyle taskHeaderStyle = createTaskHeaderStyle(workbook);
    CellStyle taskCellStyle = createTaskCellStyle(workbook);
    CellStyle multiLineTaskCellStyle = createMultiLineTaskCellStyle(workbook);

    int rowNum = 0;

    // ★ タイトル (A1:B1を結合)
    Row titleRow = sheet.createRow(rowNum++);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("業務報告書");
    titleCell.setCellStyle(titleStyle);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

    // ★ 基本情報
    rowNum++;
    rowNum = createSection(sheet, rowNum, "基本情報", report, sectionHeaderStyle, labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "報告対象期間",
        report.getStartDate().format(DATE_FORMATTER) + " ～ " + report.getEndDate().format(DATE_FORMATTER), labelStyle,
        dataStyle);
    createDataRow(sheet, rowNum++, "作成日", report.getCreatedAt().format(DATE_FORMATTER), labelStyle, dataStyle);

    // ★ 顧客情報
    rowNum++;
    rowNum = createSection(sheet, rowNum, "顧客情報", report, sectionHeaderStyle, labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "エンド企業", report.getCustomerInfo().getEndClient(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "上位企業", report.getCustomerInfo().getUpperClient(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "業種", report.getCustomerInfo().getIndustry(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "職場最寄駅", report.getCustomerInfo().getNearestStation(), labelStyle, dataStyle);

    // ★ 案件情報
    rowNum++;
    rowNum = createSection(sheet, rowNum, "案件情報", report, sectionHeaderStyle, labelStyle, dataStyle);
    ProjectInfo projectInfo = Optional.ofNullable(report.getProjectInfo()).orElse(new ProjectInfo());
    createDataRow(sheet, rowNum++, "案件名", projectInfo.getProjectName(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "参画日",
        Optional.ofNullable(projectInfo.getParticipationDate()).map(DATE_FORMATTER::format).orElse(""), labelStyle,
        dataStyle);
    createDataRow(sheet, rowNum++, "参画人数",
        Optional.ofNullable(projectInfo.getNumberOfParticipants()).map(String::valueOf).orElse(""), labelStyle,
        dataStyle);
    createDataRow(sheet, rowNum++, "通勤時間",
        String.format("%d時間 %d分", projectInfo.getCommuteHours(), projectInfo.getCommuteMinutes()), labelStyle,
        dataStyle);
    createDataRow(sheet, rowNum++, "勤務形態",
        Optional.ofNullable(projectInfo.getWorkStyle()).orElse(""), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "ポジション",
        Optional.ofNullable(projectInfo.getPosition()).orElse(""), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "主要技術", projectInfo.getMainTechnology(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "データベース", projectInfo.getDatabase(), labelStyle, dataStyle);

    // ★ 進捗状況
    rowNum++;
    rowNum = createSection(sheet, rowNum, "進捗状況", report, sectionHeaderStyle, labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "全体状況", report.getOverallProgress(), labelStyle, multiLineDataStyle);

    // ★ タスク一覧 (表形式)
    rowNum++;
    rowNum = createSection(sheet, rowNum, "タスク", report, sectionHeaderStyle, labelStyle, dataStyle);
    if (report.getTasks() != null && !report.getTasks().isEmpty()) {
      Row taskHeaderRow = sheet.createRow(rowNum++);
      createTaskHeaderCell(taskHeaderRow, 0, "タスク名", taskHeaderStyle);
      createTaskHeaderCell(taskHeaderRow, 1, "状況", taskHeaderStyle);
      createTaskHeaderCell(taskHeaderRow, 2, "問題点", taskHeaderStyle);

      for (Task task : report.getTasks()) {
        Row taskRow = sheet.createRow(rowNum++);
        createTaskCell(taskRow, 0, task.getTaskName(), taskCellStyle);
        createTaskCell(taskRow, 1, task.getTaskProblem(), multiLineTaskCellStyle);
        createTaskCell(taskRow, 2, task.getTaskProblem(), multiLineTaskCellStyle);
      }
    } else {
      createDataRow(sheet, rowNum++, "タスク", "タスクは登録されていません。", labelStyle, dataStyle);
    }

    // ★ 今後の予定
    rowNum++;
    createDataRow(sheet, rowNum++, "今後の予定", report.getFuturePlans(), labelStyle, multiLineDataStyle);

    // ★ その他
    rowNum++;
    rowNum = createSection(sheet, rowNum, "その他", report, sectionHeaderStyle, labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "顧客状況", report.getOtherInfo().getCustomerStatus(), labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "営業情報", report.getOtherInfo().getSalesInfo(), labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "健康状況", report.getOtherInfo().getHealthStatus(), labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "休暇予定", report.getOtherInfo().getVacationPlans(), labelStyle, multiLineDataStyle);

    // ★ 上司への相談
    rowNum++;
    createDataRow(sheet, rowNum++, "上司への相談", report.getConsultation(), labelStyle, multiLineDataStyle);

    // サイズを調整
    for (int i = 0; i < 3; i++) {
      sheet.autoSizeColumn(i);
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return outputStream.toByteArray();
  }

  // ヘルパーメソッド: セクションを作成
  private int createSection(Sheet sheet, int rowNum, String title, Report report, CellStyle headerStyle,
      CellStyle labelStyle, CellStyle dataStyle) {
    Row row = sheet.createRow(rowNum++);
    Cell cell = row.createCell(0);
    cell.setCellValue(title);
    cell.setCellStyle(headerStyle);
    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));
    return rowNum;
  }

  // ヘルパーメソッド: データを書き込む行を作成
  private void createDataRow(Sheet sheet, int rowNum, String label, String value, CellStyle labelStyle,
      CellStyle dataStyle) {
    Row row = sheet.createRow(rowNum);
    Cell labelCell = row.createCell(0);
    labelCell.setCellValue(label);
    labelCell.setCellStyle(labelStyle);

    Cell valueCell = row.createCell(1);
    valueCell.setCellValue(Optional.ofNullable(value).orElse(""));
    valueCell.setCellStyle(dataStyle);
  }

  // ヘルパーメソッド: タスクヘッダーセルを作成
  private void createTaskHeaderCell(Row row, int column, String label, CellStyle style) {
    Cell cell = row.createCell(column);
    cell.setCellValue(label);
    cell.setCellStyle(style);
  }

  // ヘルパーメソッド: タスクデータセルを作成
  private void createTaskCell(Row row, int column, String value, CellStyle style) {
    Cell cell = row.createCell(column);
    cell.setCellValue(Optional.ofNullable(value).orElse(""));
    cell.setCellStyle(style);
  }

  // スタイル定義
  private CellStyle createTitleStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 14);
    style.setFont(font);
    return style;
  }

  private CellStyle createSectionHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    return style;
  }

  private CellStyle createLabelStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }

  private CellStyle createDataStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }

  private CellStyle createMultiLineDataStyle(Workbook workbook) {
    CellStyle style = createDataStyle(workbook);
    style.setWrapText(true);
    return style;
  }

  private CellStyle createTaskHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }

  private CellStyle createTaskCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }

  private CellStyle createMultiLineTaskCellStyle(Workbook workbook) {
    CellStyle style = createTaskCellStyle(workbook);
    style.setWrapText(true);
    return style;
  }
}