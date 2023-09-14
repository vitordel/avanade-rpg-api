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

    @Column(name = "attack_roll")
    private Long attackRoll;
    @Column(name = "attack_result")
    private Long attackResult;

    @Column(name = "defense_roll")
    private Long defenseRoll;
    @Column(name = "defense_result")
    private Long defenseResult;

    @Column(name = "damage_roll")
    private String damageRoll;

    @Column(name = "damage_result")
    private Long damageResult;

    public BattleLog(Battle battle, Character characterAttacking, Character characterDefending, Long turn) {
        this.battle = battle;
        this.characterAttacking = characterAttacking;
        this.characterDefending = characterDefending;
        this.turn = turn;
    }
}
