package br.com.vitordel.avanaderpg.characters.dto;

import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import br.com.vitordel.avanaderpg.characters.validators.ValidCharacterCategory;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDto {

    @ValidCharacterCategory
    @NotEmpty(message = "The category is required")
    private CharacterCategory category;

    @NotEmpty(message = "The species is required")
    private String species;

    @NotEmpty(message = "The life is required")
    private Long life;

    @NotEmpty(message = "The strength is required")
    private Long strength;

    @NotEmpty(message = "The defense is required")
    private Long defense;

    @NotEmpty(message = "The agility is required")
    private Long agility;

    @NotEmpty(message = "The dice_quantity is required")
    private Long dice_quantity;

    @NotEmpty(message = "The dice_faces is required")
    private Long dice_faces;
}
