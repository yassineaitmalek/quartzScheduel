package com.test.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.test.dto.EventCreationDTO;
import com.test.dto.EventIntervalDTO;
import com.test.dto.EventUpdateDTO;
import com.test.dto.SchedulerParams;
import com.test.models.local.Event;
import com.test.repositories.local.EventRepository;
import com.test.services.jobs.EventJob;
import com.test.utility.DateUtility;
import com.test.utility.StringTemplate;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

  private final ModelMapper modelMapper;

  private final JobSchedulerService jobSchedulerService;

  public SchedulerParams<Event> toSchedulerParams(@Valid @NotNull Event event) {
    return SchedulerParams.<Event>builder()
        .object(event)
        .jobClass(EventJob.class)
        .mapKey("event")
        .jobId(event.getId())
        .group(Event.JOB_GROUP)
        .description("scheduel event")
        .startAt(DateUtility.getZoneDateTimeByLocalDateTime(event.getStart()))
        .reccurentEvent(event.isReccurentEvent())
        .cron(eventStartToCron(event))
        .build();

  }

  public String eventStartToCron(@Valid @NotNull Event event) {
    return StringTemplate.template("${S} ${M} ${H} ? * ${Ds} *")
        .addParameter("S", String.valueOf(event.getStart().getSecond()))
        .addParameter("M", String.valueOf(event.getStart().getMinute()))
        .addParameter("H", String.valueOf(event.getStart().getHour()))
        .addParameter("Ds", event.daysToString())
        .build();

  }

  public boolean scheduelEvent(@Valid @NotNull Event event) {

    return jobSchedulerService.schedule(toSchedulerParams(event));
  }

  public boolean rescheduelEvent(@Valid @NotNull Event event) {

    return jobSchedulerService.reschedule(toSchedulerParams(event));
  }

  public boolean unscheduelEvent(@Valid @NotNull Event event) {

    return jobSchedulerService.unschedule(toSchedulerParams(event));
  }

  public Optional<Event> getById(@Valid @NotNull String id) {
    return eventRepository.findById(id);

  }

  public List<Event> getEventsByInterval(@Valid @NotNull EventIntervalDTO eventIntervalDTO) {
    return eventRepository.findByStartGreaterThanAndEndLessThan(eventIntervalDTO.getStart(),
        eventIntervalDTO.getEnd());

  }

  public Optional<Event> createEvent(@Valid @NotNull EventCreationDTO eventCreationDTO) {

    Event event = modelMapper.map(eventCreationDTO, Event.class);
    eventRepository.save(event);
    scheduelEvent(event);
    return Optional.of(event);

  }

  public Optional<Event> updateEvent(@Valid @NotNull String eventId, @Valid @NotNull EventUpdateDTO eventUpdateDTO) {
    return eventRepository.findById(eventId)
        .map(e -> updateEvent(e, eventUpdateDTO))
        .orElse(Optional.empty());

  }

  public Optional<Event> updateEvent(@Valid @NotNull Event event, @Valid @NotNull EventUpdateDTO eventUpdateDTO) {

    modelMapper.map(eventUpdateDTO, event);
    rescheduelEvent(event);
    return Optional.of(eventRepository.save(event));

  }

  public void deleteEvent(String id) {
    Optional.ofNullable(id)
        .map(eventRepository::findById)
        .filter(e -> e.isPresent())
        .map(e -> e.get())
        .ifPresent(this::deleteEvent);

  }

  public void deleteEvent(@Valid @NotNull Event event) {

    eventRepository.delete(event);
    unscheduelEvent(event);

  }

}
