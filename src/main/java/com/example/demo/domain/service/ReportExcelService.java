package com.example.demo.domain.service;

import com.example.demo.domain.model.Report;
import com.example.demo.domain.model.ProjectInfo;
import com.example.demo.domain.model.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ReportExcelService {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
  private static final String FONT_NAME = "Meiryo UI";

  /**
   * 業務報告書のExcelファイルを作成します。
   * 
   * @param report 業務報告書データ
   * @return Excelファイルイメージ
   * @throws IOException
   */
  public byte[] generateExcel(Report report) throws IOException {

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("業務報告書");

    // 全体スタイル設定
    setSheetStyle(sheet);

    // スタイル設定
    CellStyle titleStyle = createTitleStyle(workbook);
    CellStyle sectionHeaderStyle = createSectionHeaderStyle(workbook);
    CellStyle labelStyle = createLabelStyle(workbook);
    CellStyle dataStyle = createDataStyle(workbook);
    CellStyle multiLineDataStyle = createMultiLineDataStyle(workbook);
    CellStyle periodLabelStyle = createPeriodLabelStyle(workbook);
    CellStyle periodDataStyle = createPeriodDataStyle(workbook);

    int rowNum = 0;

    // タイトル
    Row titleRow = sheet.createRow(rowNum++);
    Cell titleCell = titleRow.createCell(1);
    titleCell.setCellValue("業務報告書");
    titleCell.setCellStyle(titleStyle);

    // 基本情報
    rowNum++;
    createDataRow(sheet, rowNum++, "報告対象期間",
        appendLineSeparator(report.getStartDate().format(DATE_FORMATTER) + " ～ " + report.getEndDate().format(DATE_FORMATTER)),
        periodLabelStyle, periodDataStyle);

    // 顧客情報
    rowNum++;
    rowNum = createSection(sheet, rowNum, "顧客情報", report, sectionHeaderStyle, labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "エンド企業",
        report.getCustomerInfo().getEndClient(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "上位企業",
        report.getCustomerInfo().getUpperClient(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "業種",
        report.getCustomerInfo().getIndustry(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "職場最寄駅",
        report.getCustomerInfo().getNearestStation(), labelStyle, dataStyle);

    // 案件情報
    rowNum++;
    rowNum = createSection(sheet, rowNum, "案件情報", report, sectionHeaderStyle, labelStyle, dataStyle);
    ProjectInfo projectInfo = Optional.ofNullable(report.getProjectInfo()).orElse(new ProjectInfo());
    createDataRow(sheet, rowNum++, "案件名",
        projectInfo.getProjectName(), labelStyle, dataStyle);
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
    createDataRow(sheet, rowNum++, "主要技術",
        projectInfo.getMainTechnology(), labelStyle, dataStyle);
    createDataRow(sheet, rowNum++, "データベース",
        projectInfo.getDatabase(), labelStyle, dataStyle);

    // 全体状況
    rowNum++;
    rowNum = createSection(sheet, rowNum, "全体状況", report, sectionHeaderStyle, labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "全体状況",
        appendLineSeparator(normalizeLineBreaks(report.getOverallProgress())),
        labelStyle, multiLineDataStyle);

    // タスク
    if (report.getTasks() != null && !report.getTasks().isEmpty()) {
      int taskNumber = 1;
      for (Task task : report.getTasks()) {
        rowNum++;
        String taskTitle = "タスク" + getJapaneseNumber(taskNumber);
        rowNum = createSection(sheet, rowNum, taskTitle, report, sectionHeaderStyle, labelStyle, dataStyle);
        createDataRow(sheet, rowNum++, "タスク名",
            task.getTaskName(), labelStyle, dataStyle);
        createDataRow(sheet, rowNum++, "状況",
            appendLineSeparator(normalizeLineBreaks(task.getTaskProgress())),
            labelStyle, multiLineDataStyle);
        createDataRow(sheet, rowNum++, "課題・問題点",
            appendLineSeparator(normalizeLineBreaks(task.getTaskProblem())),
            labelStyle, multiLineDataStyle);
        taskNumber++;
      }
    } else {
      rowNum++;
      createDataRow(sheet, rowNum++, "タスク", "タスクは登録されていません。", labelStyle, dataStyle);
    }

    // 今後の予定
    rowNum++;
    rowNum = createSection(sheet, rowNum, "今後の予定", report, sectionHeaderStyle, labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "今後の予定",
        appendLineSeparator(normalizeLineBreaks(report.getFuturePlans())),
        labelStyle, multiLineDataStyle);

    // その他
    rowNum++;
    rowNum = createSection(sheet, rowNum, "その他", report, sectionHeaderStyle, labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "顧客状況",
        appendLineSeparator(normalizeLineBreaks(report.getOtherInfo().getCustomerStatus())),
        labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "営業情報",
        appendLineSeparator(normalizeLineBreaks(report.getOtherInfo().getSalesInfo())),
        labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "健康状況",
        appendLineSeparator(normalizeLineBreaks(report.getOtherInfo().getHealthStatus())),
        labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "休暇予定",
        appendLineSeparator(normalizeLineBreaks(report.getOtherInfo().getVacationPlans())),
        labelStyle, multiLineDataStyle);

    // 上司への相談
    rowNum++;
    rowNum = createSection(sheet, rowNum, "相談", report, sectionHeaderStyle, labelStyle, multiLineDataStyle);
    createDataRow(sheet, rowNum++, "上司への相談",
        appendLineSeparator(normalizeLineBreaks(report.getConsultation())),
        labelStyle, multiLineDataStyle);

    // Excel書き込み
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return outputStream.toByteArray();
  }

  /**
   * セクション行を作成します。
   * 
   * @param sheet       ワークシート
   * @param rowNum      現在の行番号
   * @param title       タイトル
   * @param report      報告書データ
   * @param headerStyle ヘッダーのスタイル定義
   * @param labelStyle  ラベルのスタイル定義
   * @param dataStyle   データ内容のスタイル定義
   * @return 行出力後の行番号
   */
  private int createSection(Sheet sheet, int rowNum, String title, Report report, CellStyle headerStyle,
      CellStyle labelStyle, CellStyle dataStyle) {
    Row row = sheet.createRow(rowNum++);
    Cell cell = row.createCell(1);
    cell.setCellValue(title);
    cell.setCellStyle(headerStyle);
    return rowNum;
  }

  /**
   * データを書き込む行を作成します。
   * 
   * @param sheet      ワークシート
   * @param rowNum     現在の行番号
   * @param label      ラベル
   * @param value      データ内容
   * @param labelStyle ラベルのスタイル定義
   * @param dataStyle  データ内容のスタイル定義
   */
  private void createDataRow(Sheet sheet, int rowNum, String label, String value,
      CellStyle labelStyle, CellStyle dataStyle) {
    Row row = sheet.createRow(rowNum);
    Cell labelCell = row.createCell(1);
    labelCell.setCellValue(label);
    labelCell.setCellStyle(labelStyle);

    Cell valueCell = row.createCell(2);
    valueCell.setCellValue(Optional.ofNullable(value).orElse(""));
    valueCell.setCellStyle(dataStyle);
  }

  /**
   * タイトルのスタイルを作成します。
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createTitleStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setVerticalAlignment(VerticalAlignment.TOP);
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 16);
    font.setFontName(FONT_NAME);
    style.setFont(font);
    return style;
  }

  /**
   * セクションのヘッダースタイルを作成します。
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createSectionHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setVerticalAlignment(VerticalAlignment.TOP);
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontName(FONT_NAME);
    style.setFont(font);
    return style;
  }

  /**
   * ラベルのスタイルを作成します。
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createLabelStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setVerticalAlignment(VerticalAlignment.TOP);
    style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    Font font = workbook.createFont();
    font.setColor(IndexedColors.WHITE.getIndex());
    font.setFontName(FONT_NAME);
    style.setFont(font);
    return style;
  }

  /**
   * 報告対象期間のラベルのスタイルを作成します。
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createPeriodLabelStyle(Workbook workbook) {
    CellStyle style = createLabelStyle(workbook);
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 16);
    font.setColor(IndexedColors.WHITE.getIndex());
    font.setFontName(FONT_NAME);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    return style;
  }

  /**
   * データ内容スタイルを作成します。
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createDataStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setVerticalAlignment(VerticalAlignment.TOP);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    Font font = workbook.createFont();
    font.setFontName(FONT_NAME);
    style.setFont(font);
    return style;
  }

  /**
   * データ内容スタイルを作成します（改行あり）
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createMultiLineDataStyle(Workbook workbook) {
    CellStyle style = createDataStyle(workbook);
    style.setVerticalAlignment(VerticalAlignment.TOP);
    style.setWrapText(true);
    Font font = workbook.createFont();
    font.setFontName(FONT_NAME);
    style.setFont(font);
    return style;
  }

  /**
   * 報告対象期間のデータ内容スタイルを作成します。
   * 
   * @param workbook ワークブックオブジェクト
   * @return スタイル定義オブジェクト
   */
  private CellStyle createPeriodDataStyle(Workbook workbook) {
    CellStyle style = createMultiLineDataStyle(workbook);
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 16);
    font.setFontName(FONT_NAME);
    style.setFont(font);
    return style;
  }

  /**
   * シート全体のスタイルを作成します。
   * 
   * @param sheet ワークシートオブジェクト
   */
  public void setSheetStyle(Sheet sheet) {
    // 1列目は3文字分の幅を設定
    sheet.setColumnWidth(0, 768);

    // 2列目は30文字分の幅を設定
    sheet.setColumnWidth(1, 7680);
    // sheet.autoSizeColumn(1);

    // 3列目は100文字分の幅を設定
    sheet.setColumnWidth(2, 25600);

    // A3セルを基点にウインドウを固定
    sheet.createFreezePane(0, 2);

    // ワークシートの目盛線を非表示
    sheet.setDisplayGridlines(false);

    // シートの表示倍率率(80%)
    sheet.setZoom(80);
  }

  /**
   * 数字を丸囲み漢数字に変換します。
   * 1～10まで対応
   * 
   * @param number 変換する数字
   * @return 丸囲み漢数字
   */
  private String getJapaneseNumber(int number) {
    String[] japaneseNumbers = { "", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩" };
    if (number > 0 && number < japaneseNumbers.length) {
      return japaneseNumbers[number];
    }
    return String.valueOf(number); // 範囲外の場合は通常の数字を返す
  }

  /**
   * 文字列内のすべての改行コードを正規化します。
   * 
   * @param text 処理対象の文字列
   * @return 正規化された文字列
   */
  private String normalizeLineBreaks(String text) {
    if (text == null) {
      return "";
    }

    return text.replace("\\n", System.lineSeparator());
  }

  /**
   * Nullを空文字に変換する
   * 
   * @param str 入力文字列
   * @return 変換後文字列
   */
  private static String convertNullIntoEmptyString(String str) {
    return Optional.ofNullable(str).orElse("");
  }

  /**
   * 文字列の末尾に改行を追加する（nullの場合は空文字）
   * 
   * @param str 入力文字列
   * @return 末尾に改行が追加された文字列、または空文字
   */
  private static String appendLineSeparator(String str) {
    return convertNullIntoEmptyString(str) + System.lineSeparator();
  }

}