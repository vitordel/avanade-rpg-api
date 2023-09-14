package br.com.vitordel.avanaderpg.battles.controller;

import br.com.vitordel.avanaderpg.battles.dto.BattleDto;
import br.com.vitordel.avanaderpg.battles.dto.BattleLogDto;
import br.com.vitordel.avanaderpg.battles.dto.StartBattleDto;
import br.com.vitordel.avanaderpg.battles.model.Battle;
import br.com.vitordel.avanaderpg.battles.model.BattleLog;
import br.com.vitordel.avanaderpg.battles.service.BattleService;
import br.com.vitordel.avanaderpg.response.ResponseHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/battles")
public class BattleController {
    private final BattleService battleService;

    @GetMapping()
    public ResponseEntity<Object> getBattles() {
        List<Battle> battles = battleService.getAllBattles();
        return ResponseHandler.generateResponse("Successfully retrieved all battles!", HttpStatus.OK, battles);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Object> getBattleLogs(@PathVariable Long id) {

        try {
            List<BattleLog> battleLogs = battleService.getBattleLogsByBattleId(id);
            List<BattleLogDto> battleLogDtos = battleLogs.stream()
                    .map(BattleLogDto::new)
                    .toList();

            return ResponseHandler.generateResponse("Successfully retrieved battle logs!", HttpStatus.OK, battleLogDtos);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            String errorMessage = "Character with ID " + id + " not found.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }



    @PostMapping("/start")
    public ResponseEntity<Object> startBattle(@RequestBody StartBattleDto startBattleDto) {
        Long myCharacterId = startBattleDto.getMyCharacterId();
        Long opponentId = startBattleDto.getMyOpponentId();

        try {
            Battle battle = battleService.startBattle(startBattleDto);
            battleService.createBattleLog(battle);

            BattleDto battleDto = new BattleDto(battle);

            return ResponseHandler.generateResponse("Successfully started a battle!", HttpStatus.CREATED, battleDto);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Characters with IDs " + myCharacterId + " or " + opponentId + " not found.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/{id}/attack")
    public ResponseEntity<Object> performAttack(@PathVariable Long id) {
        try {
            Battle battle = battleService.getBattleById(id);

            if (battle == null || battle.getWinner() != null) {
                String errorMessage = "Battle ID " + id + " not found or finished.";
                return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
            }

            BattleLog battleLog = battleService.performAttack(battle);
            return ResponseHandler.generateResponse("Successfully performed an Attack!",HttpStatus.OK, battleLog);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Battle ID " + id + " not found or finished.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/{id}/defense")
    public ResponseEntity<Object> performDefense(@PathVariable("id") Long id) {
        try {
            Battle battle = battleService.getBattleById(id);

            if (battle == null || battle.getWinner() != null) {
                String errorMessage = "Battle ID " + id + " not found or finished.";
                return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
            }

            BattleLog battleLog = battleService.performDefense(battle);
            return ResponseHandler.generateResponse("Successfully performed a Defense!",HttpStatus.OK, battleLog);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Battle ID " + id + " not found or finished.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/{id}/calculate-damage")
    public ResponseEntity<Object> calculateDamage(@PathVariable("id") Long id) {
        try {
            Battle battle = battleService.getBattleById(id);

            if (battle == null || battle.getWinner() != null) {
                String errorMessage = "Battle ID " + id + " not found or finished.";
                return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
            }

            BattleLog battleLog = battleService.calculateDamage(battle);

            return ResponseHandler.generateResponse("Successfully calculated the damage!",HttpStatus.OK, battleLog);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Battle ID " + id + " not found or finished.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);

        } catch (NullPointerException e) {
            String errorMessage = "No attack or defense registered!";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.BAD_REQUEST, null);
        }
    }


}
