
package com.test.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.EventCreationDTO;
import com.test.dto.EventIntervalDTO;
import com.test.dto.EventUpdateDTO;
import com.test.models.local.Event;
import com.test.services.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/interval")
    public List<Event> getEvents(@Valid @RequestBody EventIntervalDTO eventIntervalDTO) {
        return eventService.getEventsByInterval(eventIntervalDTO);
    }

    @GetMapping
    public List<Event> getEvents(@RequestParam("start") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {

        return eventService.getEventsByInterval(EventIntervalDTO.builder().start(start).end(end).build());
    }

    @PostMapping
    public Optional<Event> createEvent(@Valid @RequestBody EventCreationDTO eventCreationDTO) {
        return eventService.createEvent(eventCreationDTO);
    }

    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable String id) {
        return eventService.getById(id);
    }

    @PostMapping("/{id}")
    // @PatchMapping("/{id}")
    public Optional<Event> updateEvent(@PathVariable String id, @RequestBody EventUpdateDTO eventUpdateDTO) {

        return eventService.updateEvent(id, eventUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {

        eventService.deleteEvent(id);
    }

}