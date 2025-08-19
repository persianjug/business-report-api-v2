package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Report {
  private Long id; // 報告書ID
  private LocalDate startDate; // 報告対象期間「開始日」
  private LocalDate endDate; // 報告対象期間「終了日」
  private CustomerInfo customerInfo; // 顧客情報
  private ProjectInfo projectInfo; // 案件情報
  private String overallProgress; // 全体状況
  private List<Task> tasks; // タスク一覧
  private String futurePlans; // 今後の予定
  private OtherInfo otherInfo; // その他
  private String consultation; // 相談
  private LocalDateTime createdAt; // 作成日
  private LocalDateTime updatedAt; // 更新日
  private String status; // ステータス（公開データか下書きデータか）
}
