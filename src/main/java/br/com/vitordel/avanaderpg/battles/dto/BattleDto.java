package br.com.vitordel.avanaderpg.battles.dto;

import br.com.vitordel.avanaderpg.battles.model.Battle;
import br.com.vitordel.avanaderpg.characters.model.Character;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BattleDto {

    @ManyToOne
    private Character mainCharacter;

    @ManyToOne
    private Character opponent;

    private String starter;

    public BattleDto(Battle battle) {
        this.mainCharacter = battle.getMainCharacter();
        this.opponent = battle.getOpponent();
        this.starter = battle.getFirstTurn().getSpecies();
    }
}
