package br.com.vitordel.avanaderpg.battles.controller;

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

    @GetMapping("/history/{battleId}")
    public ResponseEntity<Object> getBattleLogs(@PathVariable Long battleId) {

        try {
            Battle battle = battleService.getBattleById(battleId);
//            List<BattleLog> battleLogs = battleService.getBattleLogs(battleId);
            return ResponseHandler.generateResponse("Successfully retrieved battle logs!", HttpStatus.OK, battle);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Character with ID " + battleId + " not found.";
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

            return ResponseHandler.generateResponse("Successfully started a battle!", HttpStatus.CREATED, battle);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Characters with IDs " + myCharacterId + " or " + opponentId + " not found.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/{battleId}/attack")
    public ResponseEntity<Object> performAttack(@PathVariable("battleId") Long battleId) {
        try {
            Battle battle = battleService.getBattleById(battleId);

            if (battle == null || battle.getWinner() != null) {
                String errorMessage = "Battle ID " + battleId + " not found or finished.";
                return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
            }

            BattleLog battleLog = battleService.performAttack(battle);
            return ResponseHandler.generateResponse("Successfully performed an Attack!",HttpStatus.OK, battleLog);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Battle ID " + battleId + " not found or finished.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/{battleId}/defense")
    public ResponseEntity<Object> performDefense(@PathVariable("battleId") Long battleId) {
        try {
            Battle battle = battleService.getBattleById(battleId);

            if (battle == null || battle.getWinner() != null) {
                String errorMessage = "Battle ID " + battleId + " not found or finished.";
                return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
            }

            BattleLog battleLog = battleService.performDefense(battle);
            return ResponseHandler.generateResponse("Successfully performed a Defense!",HttpStatus.OK, battleLog);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Battle ID " + battleId + " not found or finished.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/{battleId}/calculate-damage")
    public ResponseEntity<Object> calculateDamage(@PathVariable("battleId") Long battleId) {
        try {
            Battle battle = battleService.getBattleById(battleId);

            if (battle == null || battle.getWinner() != null) {
                String errorMessage = "Battle ID " + battleId + " not found or finished.";
                return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
            }

            BattleLog battleLog = battleService.calculateDamage(battle);

            return ResponseHandler.generateResponse("Successfully calculated the damage!",HttpStatus.OK, battleLog);

        } catch (EntityNotFoundException e) {
            String errorMessage = "Battle ID " + battleId + " not found or finished.";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);

        } catch (NullPointerException e) {
            String errorMessage = "No attack or defense registered!";
            return ResponseHandler.generateResponse(errorMessage, HttpStatus.BAD_REQUEST, null);
        }
    }


}
