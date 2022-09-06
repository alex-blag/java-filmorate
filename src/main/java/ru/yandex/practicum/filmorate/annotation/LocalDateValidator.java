package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateValidator implements ConstraintValidator<IsAfter, LocalDate> {

    private LocalDate before;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        before = LocalDate.parse(
                constraintAnnotation.date(),
                DateTimeFormatter.ofPattern(constraintAnnotation.pattern())
        );
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date == null || before.isBefore(date);
    }
}
