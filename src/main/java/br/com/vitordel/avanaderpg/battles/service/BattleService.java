package br.com.vitordel.avanaderpg.battles.service;

import br.com.vitordel.avanaderpg.battles.dto.StartBattleDto;
import br.com.vitordel.avanaderpg.battles.model.Battle;
import br.com.vitordel.avanaderpg.battles.model.BattleLog;

import java.util.List;

public interface BattleService {

    List<Battle> getAllBattles();
    Battle getBattleById(Long battleId);
    List<BattleLog> getBattleLogsByBattleId(Long battleId);
    Battle startBattle(StartBattleDto startBattleDto);

    BattleLog createBattleLog(Battle battle);

    BattleLog performAttack(Battle battle);
    BattleLog performDefense(Battle battle);

    BattleLog calculateDamage(Battle battle);
}
