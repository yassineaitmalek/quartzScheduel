package com.test.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateDTO {

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime start;

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime end;

  private String content;

  private String color;

  private boolean reccurentEvent;

  private Set<DayOfWeek> days;
}