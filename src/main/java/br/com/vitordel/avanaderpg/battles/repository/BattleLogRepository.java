package br.com.vitordel.avanaderpg.battles.repository;

import br.com.vitordel.avanaderpg.battles.model.BattleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleLogRepository extends JpaRepository<BattleLog, Long> {
    List<BattleLog> findByBattleId(Long battleId);
    BattleLog findByBattleIdAndTurn(Long battleId, Long turn);
}
