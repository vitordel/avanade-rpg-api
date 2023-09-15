package br.com.vitordel.avanaderpg.characters.service;


import br.com.vitordel.avanaderpg.characters.dto.UpdateCharacterDto;
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
    public Character updateCharacter(Long id, UpdateCharacterDto updateCharacterDto) {

        Character existingCharacter = getCharacterById(id);
        if (updateCharacterDto.getCategory() != null && isValidCategory(updateCharacterDto.getCategory())) {
            existingCharacter.setCategory(updateCharacterDto.getCategory().toUpperCase());
        }
        if (updateCharacterDto.getSpecies() != null) {
            existingCharacter.setSpecies(updateCharacterDto.getSpecies());
        }
        if (updateCharacterDto.getLife() != null && updateCharacterDto.getLife() > 0) {
            existingCharacter.setLife(updateCharacterDto.getLife());
        }
        if (updateCharacterDto.getStrength() != null && updateCharacterDto.getStrength() > 0) {
            existingCharacter.setStrength(updateCharacterDto.getStrength());
        }
        if (updateCharacterDto.getDefense() != null && updateCharacterDto.getDefense() > 0) {
            existingCharacter.setDefense(updateCharacterDto.getDefense());
        }
        if (updateCharacterDto.getAgility() != null && updateCharacterDto.getAgility() > 0) {
            existingCharacter.setAgility(updateCharacterDto.getAgility());
        }
        if (updateCharacterDto.getDiceQuantity() != null && updateCharacterDto.getDiceQuantity() > 0) {
            existingCharacter.setDiceQuantity(updateCharacterDto.getDiceQuantity());
        }
        if (updateCharacterDto.getDiceFaces() != null && updateCharacterDto.getDiceFaces() > 0) {
            existingCharacter.setDiceFaces(updateCharacterDto.getDiceFaces());
        }

        return characterRepository.save(existingCharacter);

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

    private boolean isValidCategory(String category) {
        try {
            CharacterCategory.valueOf(category.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
