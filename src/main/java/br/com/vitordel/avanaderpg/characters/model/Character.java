package br.com.vitordel.avanaderpg.characters.model;

import br.com.vitordel.avanaderpg.characters.dto.CharacterDto;
import br.com.vitordel.avanaderpg.exceptions.InvalidCharacterCategoryException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "characters")
@NoArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterCategory category;

    public void setCategory(String category) {
        this.category = CharacterCategory.valueOf(category.toUpperCase());
    }

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private Long life;

    @Column(nullable = false)
    private Long strength;

    @Column(nullable = false)
    private Long defense;

    @Column(nullable = false)
    private Long agility;

    @Column(name = "dice_quantity", nullable = false)
    private Long diceQuantity;

    @Column(name = "dice_faces", nullable = false)
    private Long diceFaces;

    public Character(CharacterDto characterDto) {
        if (!isValidCategory(characterDto.getCategory())) {
            throw new InvalidCharacterCategoryException("Invalid character category");
        }

        this.category = CharacterCategory.valueOf(characterDto.getCategory().toUpperCase());
        this.species = characterDto.getSpecies();
        this.life = characterDto.getLife();
        this.strength = characterDto.getStrength();
        this.defense = characterDto.getDefense();
        this.agility = characterDto.getAgility();
        this.diceQuantity = characterDto.getDiceQuantity();
        this.diceFaces = characterDto.getDiceFaces();
    }

    private boolean isValidCategory(String category) {
        try {
            CharacterCategory.valueOf(category.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
