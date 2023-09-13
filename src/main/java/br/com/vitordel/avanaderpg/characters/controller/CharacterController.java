package br.com.vitordel.avanaderpg.characters.controller;

import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import br.com.vitordel.avanaderpg.characters.service.CharacterService;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<List<Character>> getAllCharacters() {
        List<Character> characters = characterService.getAllCharacters();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getCharacterByCategory(@PathVariable("category") String category) {
        try {
            List<Character> characters = characterService.getCharactersByCategory(category);
            return ResponseEntity.ok(characters);
        } catch (IllegalArgumentException e) {
            CharacterCategory[] availableCategories = CharacterCategory.values();
            String errorMessage = "Invalid category: " + category +
                    ". Available categories: " + Arrays.toString(availableCategories);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable("id") Long id) {
        try {
            Character character = characterService.getCharacterById(id);
            return ResponseEntity.ok(character);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createCharacter(@RequestBody Character character) {
        Character createdCharacter = characterService.createCharacter(character);
        return new ResponseEntity<>(createdCharacter, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(@PathVariable Long id, @RequestBody Character character) {
        try {
            Character updatedCharacter = characterService.updateCharacter(id, character);
            return new ResponseEntity<>(updatedCharacter, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable Long id) {
        try {
            Character existingCharacter = characterService.getCharacterById(id);
            characterService.deleteCharacter(id);
            return ResponseEntity.ok(existingCharacter);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }
}
