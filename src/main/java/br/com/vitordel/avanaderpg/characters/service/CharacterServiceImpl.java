package br.com.vitordel.avanaderpg.characters.service;


import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import br.com.vitordel.avanaderpg.characters.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterService{

    private final CharacterRepository characterRepository;

    @Override
    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    @Override
    public List<Character> getCharactersByCategory(String category) {
        CharacterCategory requestedCategory = parseCategory(category);
        if (requestedCategory == null) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }

        return characterRepository.findByCategory(requestedCategory);
    }

    @Override
    public Character getCharacterById(Long id) {
        Optional<Character> character = characterRepository.findById(id);
        if (character.isPresent()) {
            return character.get();
        } else {
            throw new EntityNotFoundException("Character with ID " + id + " not found");
        }
    }

    @Override
    public Character createCharacter(Character character) {
        return characterRepository.save(character);
    }

    @Override
    public Character updateCharacter(Long id, Character updatedCharacter) {
        Character existingCharacter = getCharacterById(id);
        if (existingCharacter != null) {
            existingCharacter.setCategory(String.valueOf(updatedCharacter.getCategory()));
            existingCharacter.setSpecies(updatedCharacter.getSpecies());
            existingCharacter.setLife(updatedCharacter.getLife());
            existingCharacter.setStrength(updatedCharacter.getStrength());
            existingCharacter.setDefense(updatedCharacter.getDefense());
            existingCharacter.setAgility(updatedCharacter.getAgility());
            existingCharacter.setDiceQuantity(updatedCharacter.getDiceQuantity());
            existingCharacter.setDiceFaces(updatedCharacter.getDiceFaces());

            return characterRepository.save(existingCharacter);
        }
        return null;
    }

    @Override
    public void deleteCharacter(Long id) {
        getCharacterById(id);

        characterRepository.deleteById(id);
    }

    private CharacterCategory parseCategory(String category) {
        try {
            return CharacterCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
