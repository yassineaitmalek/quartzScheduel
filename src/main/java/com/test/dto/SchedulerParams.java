package com.test.dto;

import java.time.ZonedDateTime;

import org.quartz.Job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerParams<T> {

  private T object;

  private Class<? extends Job> jobClass;

  private String mapKey;

  private String jobId;

  private String group;

  private String description;

  private ZonedDateTime startAt;

  private boolean reccurentEvent;

  private String cron;

}
