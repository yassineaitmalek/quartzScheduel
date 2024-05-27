package com.test.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class EventCreationDTO {

  @NotNull
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime start;

  @NotNull
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime end;

  @NotBlank
  @NotNull
  private String content;

  @NotNull
  @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")
  private String color;

  private boolean reccurentEvent;

  private Set<DayOfWeek> days;

}