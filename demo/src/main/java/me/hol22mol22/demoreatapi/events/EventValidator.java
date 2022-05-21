package me.hol22mol22.demoreatapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if (eventDto.getMaxPrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){
            errors.rejectValue("basePrice","wrongValue","BasePrice is wrong.");
            errors.rejectValue("maxPrice","wrongValue","MaxPrice is wrong.");
            errors.reject("wrongPrices", "Values for prices are wrong");
        }

        LocalDateTime endEventDatetime = eventDto.getEndEventDateTime();
        if (endEventDatetime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDatetime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDatetime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime", "wrongValue","endEventDateTime is wrong");


        }


        // Todo BeginEventDateTime
        // Todo CloseEnrollmentDateTime

    }
}
