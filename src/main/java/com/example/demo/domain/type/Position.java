package com.example.demo.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
  PG("PG"),
  SE("SE"),
  SE_EMPLOYEE_SUBSTITUTE("SE(社員代替)"),
  TESTER("テスター"),
  OPERATOR("オペレーター"),
  PL("PL"),
  PM("PM"),
  EMPLOYEE_SUBSTITUTE("社員代替");

  @JsonValue
  private final String displayValue;

  // displayValueからEnumを取得するメソッド
  public static Position fromDisplayValue(String displayValue) {
    for (Position position : Position.values()) {
      if (position.displayValue.equals(displayValue)) {
        return position;
      }
    }
    throw new IllegalArgumentException("Unknown display value: " + displayValue);
  }
}