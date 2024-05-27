package com.test.models.local;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.test.models.config.BaseEntity;
import com.test.models.converter.DaysOfWeekConverter;
import com.test.validation.EventValidator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EventValidator
@EqualsAndHashCode(callSuper = false)
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE event SET deleted = true WHERE id=? and version = ?")
public class Event extends BaseEntity {

    public static final String JOB_GROUP = "event-jobs";

    private String content;

    @NotNull
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime start;

    @NotNull
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime end;

    @NotNull
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")
    private String color;

    @Builder.Default
    private boolean deleted = false;

    private boolean reccurentEvent;

    @Convert(converter = DaysOfWeekConverter.class)
    private Set<DayOfWeek> days;

    public String daysToString() {
        return days.stream().map(e -> String.valueOf(e.ordinal() + 1)).sorted().collect(Collectors.joining(","));
    }
}
