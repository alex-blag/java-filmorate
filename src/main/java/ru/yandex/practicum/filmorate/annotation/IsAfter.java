package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateValidator.class)
public @interface IsAfter {
    String date();

    String pattern() default "yyyy-MM-dd";

    String message() default "Value must be before the date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
