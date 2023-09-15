package br.com.vitordel.avanaderpg.characters.controller;

import br.com.vitordel.avanaderpg.characters.dto.CharacterDto;
import br.com.vitordel.avanaderpg.characters.dto.UpdateCharacterDto;
import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import br.com.vitordel.avanaderpg.characters.service.CharacterService;
import br.com.vitordel.avanaderpg.response.ResponseHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping()
    public ResponseEntity<Object> getAllCharacters() {
        List<Character> characters = characterService.getAllCharacters();
        return ResponseHandler.generateResponse("Successfully retrieved the characters!", HttpStatus.OK, characters);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Object> getCharacterByCategory(@PathVariable("category") String category) {
        try {
            List<Character> characters = characterService.getCharactersByCategory(category);
            return ResponseHandler.generateResponse("Successfully retrieved the characters!", HttpStatus.OK, characters);
        } catch (IllegalArgumentException e) {
            CharacterCategory[] availableCategories = CharacterCategory.values();
            String errorMessage = "Invalid category: " + category +
                    ". Available categories: " + Arrays.toString(availableCategories);
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.BAD_REQUEST, null);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCharacterById(@PathVariable("id") Long id) {
        try {
            Character character = characterService.getCharacterById(id);
            return ResponseHandler.generateResponse("Successfully retrieved the character!", HttpStatus.OK, character);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping()
    public ResponseEntity<Object> createCharacter(@Valid @RequestBody CharacterDto characterDto) {
        Character character = new Character(characterDto);
        Character createdCharacter = characterService.createCharacter(character);
        return ResponseHandler.generateResponse("Successfully created the character!", HttpStatus.CREATED, createdCharacter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCharacter(@PathVariable Long id, @RequestBody UpdateCharacterDto updateCharacterDto) {
        try {
            Character updatedCharacter = characterService.updateCharacter(id, updateCharacterDto);
            return ResponseHandler.generateResponse("Successfully updated the character!", HttpStatus.OK, updatedCharacter);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCharacter(@PathVariable Long id) {
        try {
            Character existingCharacter = characterService.getCharacterById(id);
            characterService.deleteCharacter(id);
            return ResponseHandler.generateResponse("Successfully deleted the character!", HttpStatus.OK, existingCharacter);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }
}
