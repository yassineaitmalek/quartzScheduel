package com.test.validation.impl;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.test.exception.config.ClientSideException;
import com.test.models.local.Event;
import com.test.validation.EventValidator;

public class EventValidatorImpl implements ConstraintValidator<EventValidator, Event> {

	@Override
	public boolean isValid(Event event, ConstraintValidatorContext context) {
		if (Objects.isNull(event)) {
			throw new ClientSideException("Event can not be null");
		}
		if (event.getStart().isAfter(event.getEnd())) {
			throw new ClientSideException("Start date can not be after end date");
		}
		if (event.isReccurentEvent() && (event.getDays() == null || event.getDays().isEmpty())) {
			throw new ClientSideException("reccurentEvent event must have a set of days not null and not empy");

		}
		return true;
	}
}