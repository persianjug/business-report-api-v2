package com.example.demo.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkStyle {
  HYBRID_OVER_60("併用勤務(在宅率6割以上)"),
  HYBRID_UNDER_60("併用勤務(在宅率6割未満)"),
  ONSITE("現場勤務"),
  REMOTE("在宅勤務");

  @JsonValue
  private final String displayValue;

  // displayValueからEnumを取得するメソッド
  public static WorkStyle fromDisplayValue(String displayValue) {
    for (WorkStyle workStyle : WorkStyle.values()) {
      if (workStyle.displayValue.equals(displayValue)) {
        return workStyle;
      }
    }
    throw new IllegalArgumentException("Unknown display value: " + displayValue);
  }
}