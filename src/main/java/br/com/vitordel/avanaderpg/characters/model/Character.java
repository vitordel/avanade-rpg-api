package br.com.vitordel.avanaderpg.characters.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity(name = "characters")
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

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
