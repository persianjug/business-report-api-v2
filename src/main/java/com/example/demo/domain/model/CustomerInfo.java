package com.example.demo.domain.model;

import lombok.Data;

@Data
public class CustomerInfo {
  private String endClient; // エンド企業
  private String upperClient; // 上位企業
  private String industry; // 業種
  private String nearestStation; // 職場最寄駅
}
