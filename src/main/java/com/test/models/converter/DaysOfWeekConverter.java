package com.test.models.converter;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DaysOfWeekConverter implements AttributeConverter<Set<DayOfWeek>, String> {

  private static final String SPLIT_CHAR = ",";

  @Override
  public String convertToDatabaseColumn(Set<DayOfWeek> days) {
    return Optional.ofNullable(days).map(this::convertToDayOfWeeksString).orElse(null);
  }

  @Override
  public Set<DayOfWeek> convertToEntityAttribute(String days) {
    return Optional.ofNullable(days).map(this::convertToDayOfWeeks).orElseGet(HashSet::new);
  }

  public String convertToDayOfWeeksString(Set<DayOfWeek> days) {
    if (days.isEmpty()) {
      return null;
    }
    return days.stream().sorted().map(e -> String.valueOf(e.ordinal() + 1)).collect(Collectors.joining(SPLIT_CHAR));

  }

  public Set<DayOfWeek> convertToDayOfWeeks(String days) {

    return Stream.of(days.split(SPLIT_CHAR))
        .map(Integer::parseInt)
        .map(this::toDaysOfWeek)
        .sorted()
        .collect(Collectors.toSet());

  }

  private DayOfWeek toDaysOfWeek(Integer e) {
    return DayOfWeek.of(e);
  }
}