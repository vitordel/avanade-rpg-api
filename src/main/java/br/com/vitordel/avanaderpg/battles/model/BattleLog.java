package br.com.vitordel.avanaderpg.battles.model;

import br.com.vitordel.avanaderpg.characters.model.Character;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "battle_logs")
public class BattleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "battle_id", nullable = false)
    private Battle battle;

    @ManyToOne
    @JoinColumn(name = "character_attacking", nullable = false)
    private Character characterAttacking;

    @ManyToOne
    @JoinColumn(name = "character_defending", nullable = false)
    private Character characterDefending;

    @Column(nullable = false)
    private Long turn;

    private Long attack;
    private Long defense;
    private Long damage;

    public BattleLog(Battle battle, Character characterAttacking, Character characterDefending, Long turn) {
        this.battle = battle;
        this.characterAttacking = characterAttacking;
        this.characterDefending = characterDefending;
        this.turn = turn;
    }
}
