package br.com.vitordel.avanaderpg.characters.validators;

import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CharacterCategoryValidator implements ConstraintValidator<ValidCharacterCategory, String> {

    @Override
    public boolean isValid(String category, ConstraintValidatorContext context) {
        return category != null && Arrays.stream(CharacterCategory.values())
                .anyMatch(enumCategory ->
                        enumCategory.name().equalsIgnoreCase(category));
    }
}
