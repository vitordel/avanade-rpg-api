package br.com.vitordel.avanaderpg.characters.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CharacterCategoryValidator.class)
public @interface ValidCharacterCategory {
    String message() default "Invalid character category";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}