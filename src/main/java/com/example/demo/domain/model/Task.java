package com.example.demo.domain.model;

import lombok.Data;

@Data
public class Task {
  private Long id; // 作業ID
  private String taskName; // 作業名
  private String status; // 状況
  private String problem; // 問題点
}
