package br.com.vitordel.avanaderpg.characters.repository;

import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findByCategory(CharacterCategory category);
}
