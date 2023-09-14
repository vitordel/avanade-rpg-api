package br.com.vitordel.avanaderpg.battles.repository;

import br.com.vitordel.avanaderpg.battles.model.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository extends JpaRepository<Battle, Long> {
}
