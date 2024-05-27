package com.test.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

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
public class EventIntervalDTO {

  @NotNull
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime start;

  @NotNull
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime end;

}
