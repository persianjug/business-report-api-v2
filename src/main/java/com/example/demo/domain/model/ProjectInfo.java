package com.example.demo.domain.model;

import java.time.LocalDate;

import com.example.demo.domain.type.Position;
import com.example.demo.domain.type.WorkStyle;

import lombok.Data;

@Data
public class ProjectInfo {
  private String projectName; // 案件名
  private LocalDate participationDate; // 参画日
  private int numberOfParticipants; // 参画人数
  private Integer commuteHours; // 通勤時間(時間)
  private Integer commuteMinutes; // 通勤時間(分)
  // private WorkStyle workStyle; // 勤務形態
  // private Position position; // ポジション
  private String workStyle; // 勤務形態
  private String position; // ポジション
  private String mainTechnology; // 主要技術
  private String database; // データベース
}
