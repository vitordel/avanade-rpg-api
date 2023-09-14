package br.com.vitordel.avanaderpg.battles.model;

import br.com.vitordel.avanaderpg.characters.model.Character;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "battles")
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_character_id", nullable = false)
    private Character mainCharacter;

    @Column(name = "main_character_life", nullable = false)
    private Long mainCharacterLife;

    @ManyToOne
    @JoinColumn(name = "opponent_id", nullable = false)
    private Character opponent;

    @Column(name = "opponent_life", nullable = false)
    private Long opponentLife;

    @ManyToOne
    @JoinColumn(name = "first_turn_id", nullable = false)
    private Character firstTurn;

    @Column(name = "turn", nullable = false)
    private Long turn = 1L;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Character winner;

    public Battle(Character mainCharacter, Long mainCharacterLife, Character opponent, Long opponentLife, Character firstTurn) {
        this.mainCharacter = mainCharacter;
        this.mainCharacterLife = mainCharacterLife;
        this.opponent = opponent;
        this.opponentLife = opponentLife;
        this.firstTurn = firstTurn;
    }
}
