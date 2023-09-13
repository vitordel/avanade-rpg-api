package br.com.vitordel.avanaderpg.characters.service;

import br.com.vitordel.avanaderpg.characters.model.Character;

import java.util.List;

public interface CharacterService {

    List<Character> getAllCharacters();
    List<Character> getCharactersByCategory(String category);
    Character getCharacterById(Long id);
    Character createCharacter(Character character);
    Character updateCharacter(Long id, Character character);
    void deleteCharacter(Long id);

}
