package com.test.services;

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.test.dto.SchedulerParams;
import com.test.exception.config.ClientSideException;
import com.test.utility.DateUtility;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class JobSchedulerService {

    private final Scheduler scheduler;

    public <T> boolean schedule(@Valid @NotNull SchedulerParams<T> schedulerParams) {
        if (schedulerParams.getStartAt().isBefore(DateUtility.nowZonedDateTime())) {
            throw new ClientSideException("dateTime must be after current time");
        }

        return Try.of(() -> schedulerParams)
                .map(this::buildJobDetail)
                .mapTry(jobDetail -> scheduler.scheduleJob(jobDetail, buildJobTrigger(jobDetail, schedulerParams)))
                .map(e -> Boolean.TRUE)
                .onFailure(ex -> log.error(ex.getMessage(), ex))
                .getOrElse(Boolean.FALSE);

    }

    public <T> boolean unschedule(@Valid @NotNull SchedulerParams<T> schedulerParams) {

        return Try.of(() -> TriggerKey.triggerKey(schedulerParams.getJobId(), schedulerParams.getGroup()))
                .mapTry(scheduler::getTrigger)
                .filter(Objects::nonNull)
                .mapTry(e -> scheduler.unscheduleJob(e.getKey()))
                .onFailure(ex -> log.error(ex.getMessage(), ex))
                .getOrElse(Boolean.FALSE);

    }

    public <T> boolean reschedule(@Valid @NotNull SchedulerParams<T> schedulerParams) {
        unschedule(schedulerParams);
        return schedule(schedulerParams);

    }

    public <T> JobDetail buildJobDetail(@Valid @NotNull SchedulerParams<T> schedulerParams) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(schedulerParams.getMapKey(), schedulerParams.getObject());
        return JobBuilder.newJob(schedulerParams.getJobClass())
                .withIdentity(schedulerParams.getJobId(), schedulerParams.getGroup())
                .withDescription(schedulerParams.getDescription())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public <T> ScheduleBuilder<?> getScheduleBuilder(SchedulerParams<T> schedulerParams) {
        switch (schedulerParams.isReccurentEvent() ? 1 : 0) {
            case 1:
                return CronScheduleBuilder.cronSchedule(schedulerParams.getCron());
            default:
                return SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();
        }
    }

    public <T> Trigger buildJobTrigger(@Valid @NotNull JobDetail jobDetail,
            @Valid @NotNull SchedulerParams<T> schedulerParams) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(schedulerParams.getJobId(), schedulerParams.getGroup())
                .withDescription(schedulerParams.getDescription())
                .startAt(Date.from(schedulerParams.getStartAt().toInstant()))
                .withSchedule(getScheduleBuilder(schedulerParams))
                .build();

    }

}
