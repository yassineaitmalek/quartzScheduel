package com.test.services.jobs;

import java.util.Optional;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.test.models.local.Event;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {

        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        Optional.ofNullable("event")
                .filter(jobDataMap::containsKey)
                .map(jobDataMap::get)
                .filter(Event.class::isInstance)
                .map(Event.class::cast)
                .ifPresent(e -> log.info("event form the scheduler {}", e));

    }

}
