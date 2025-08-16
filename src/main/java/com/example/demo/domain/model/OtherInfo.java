package com.example.demo.domain.model;

import lombok.Data;

@Data
public class OtherInfo {
  private String customerStatus; // 顧客の状況
  private String salesInfo; // 営業情報
  private String healthStatus; // 健康状態
  private String vacationPlans; // 休暇予定
}
