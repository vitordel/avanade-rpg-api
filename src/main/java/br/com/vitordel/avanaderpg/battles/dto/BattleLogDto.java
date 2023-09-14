package br.com.vitordel.avanaderpg.battles.dto;

import br.com.vitordel.avanaderpg.battles.model.BattleLog;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BattleLogDto {
    private Long id;
    private Long battleId;
    private Long characterAttackingId;
    private Long characterDefendingId;
    private Long turn;
    private Long attackRoll;
    private Long attackResult;
    private Long defenseRoll;
    private Long defenseResult;
    private String damageRoll;
    private Long damageResult;

    public BattleLogDto(BattleLog battleLog) {
        this.id = battleLog.getId();
        this.battleId = battleLog.getBattle().getId();
        this.characterAttackingId = battleLog.getCharacterAttacking().getId();
        this.characterDefendingId = battleLog.getCharacterDefending().getId();
        this.turn = battleLog.getTurn();
        this.attackRoll = battleLog.getAttackRoll();
        this.attackResult = battleLog.getAttackResult();
        this.defenseRoll = battleLog.getDefenseRoll();
        this.defenseResult = battleLog.getDefenseResult();
        this.damageRoll = battleLog.getDamageRoll();
        this.damageResult = battleLog.getDamageResult();
    }
}

