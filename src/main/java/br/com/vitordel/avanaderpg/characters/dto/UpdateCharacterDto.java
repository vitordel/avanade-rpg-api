package br.com.vitordel.avanaderpg.characters.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCharacterDto {
    private String category;
    private String species;

    @Min(value = 1, message = "Life must be at least 1")
    private Long life;

    @Min(value = 1, message = "Strength must be at least 1")
    private Long strength;

    @Min(value = 1, message = "Defense must be at least 1")
    private Long defense;

    @Min(value = 1, message = "Agility must be at least 1")
    private Long agility;

    @Min(value = 1, message = "Dice quantity must be at least 1")
    private Long diceQuantity;

    @Min(value = 1, message = "Dice faces must be at least 1")
    private Long diceFaces;
}
