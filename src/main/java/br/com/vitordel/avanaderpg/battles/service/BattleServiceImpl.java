package br.com.vitordel.avanaderpg.battles.service;

import br.com.vitordel.avanaderpg.battles.dto.StartBattleDto;
import br.com.vitordel.avanaderpg.battles.model.Battle;
import br.com.vitordel.avanaderpg.battles.model.BattleLog;
import br.com.vitordel.avanaderpg.battles.repository.BattleLogRepository;
import br.com.vitordel.avanaderpg.battles.repository.BattleRepository;
import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import br.com.vitordel.avanaderpg.characters.service.CharacterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class BattleServiceImpl implements BattleService{

    private final CharacterService characterService;
    private final BattleRepository battleRepository;
    private final BattleLogRepository battleLogRepository;

    @Override
    public List<Battle> getAllBattles() {
        return battleRepository.findAll();
    }

    @Override
    public List<BattleLog> getBattleLogsByBattleId(Long battleId) {
        return battleLogRepository.findByBattleIdOrderByTurn(battleId);
    }

    public Battle getBattleById(Long battleId) {
        Optional<Battle> battle = battleRepository.findById(battleId);
        if (battle.isPresent()) {
            return battle.get();
        } else {
            throw new EntityNotFoundException("Battle with ID " + battleId + " not found");
        }
    }

    @Override
    public Battle startBattle(StartBattleDto startBattleDto) {
        Long characterId = startBattleDto.getMyCharacterId();
        Long opponentId = startBattleDto.getMyOpponentId();

        Character myCharacter;
        Character opponentCharacter;

        try {
            myCharacter = characterService.getCharacterById(characterId);
            if (opponentId != null) {
                opponentCharacter = characterService.getCharacterById(opponentId);
            } else {
                List<Character> allCharacters = characterService.getAllCharacters();
                opponentCharacter = getRandomMonsterOpponent(allCharacters, myCharacter);
            }

            if (opponentCharacter == null) {
                throw new EntityNotFoundException("No available opponents for battle.");
            }

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Character with ID " + characterId + " not found");
        }

        long myFirstTurnRoll = roll(20);
        long opponentFirstTurnRoll = roll(20);

        Character firstTurn;

        if (myFirstTurnRoll > opponentFirstTurnRoll) {
            firstTurn = myCharacter;
        } else {
            firstTurn = opponentCharacter;
        }

        long mainCharacterLife = myCharacter.getLife();
        long opponentLife = opponentCharacter.getLife();

        Battle battle = new Battle(myCharacter, mainCharacterLife, opponentCharacter, opponentLife, firstTurn);
        return battleRepository.save(battle);
    }

    @Override
    public BattleLog createBattleLog(Battle battle) {
        Long turn = battle.getTurn();
        Character characterAttacking;
        Character characterDefending;

        if (battle.getFirstTurn() == battle.getMainCharacter()) {
            characterAttacking = battle.getMainCharacter();
            characterDefending = battle.getOpponent();
        } else {
            characterAttacking = battle.getOpponent();
            characterDefending = battle.getMainCharacter();
        }

        if (turn % 2 == 0) {
            Character temp = characterAttacking;
            characterAttacking = characterDefending;
            characterDefending = temp;
        }

        BattleLog battleLog = new BattleLog(battle, characterAttacking, characterDefending, battle.getTurn());
        return battleLogRepository.save(battleLog);
    }

    @Override
    public BattleLog performAttack(Battle battle) {
        Long attackRoll = roll(12);

        BattleLog battleLog = battleLogRepository.findByBattleIdAndTurn(battle.getId(), battle.getTurn());

        Long attackResult = attackRoll
                + battleLog.getCharacterAttacking().getStrength()
                + battleLog.getCharacterAttacking().getAgility();

        battleLog.setAttackRoll(attackRoll);
        battleLog.setAttackResult(attackResult);

        return battleLogRepository.save(battleLog);
    }

    @Override
    public BattleLog performDefense(Battle battle) {
        Long defenseRoll = roll(12);

        BattleLog battleLog = battleLogRepository.findByBattleIdAndTurn(battle.getId(), battle.getTurn());

        Long defenseResult = defenseRoll
                + battleLog.getCharacterDefending().getDefense()
                + battleLog.getCharacterDefending().getAgility();

        battleLog.setDefenseRoll(defenseRoll);
        battleLog.setDefenseResult(defenseResult);

        return battleLogRepository.save(battleLog);
    }

    @Override
    public BattleLog calculateDamage(Battle battle) {
        BattleLog battleLog = battleLogRepository.findByBattleIdAndTurn(battle.getId(), battle.getTurn());

        validateBattleLog(battleLog);

        if (battleLog.getAttackResult() > battleLog.getDefenseResult()) {
            calculateDamageAndApply(battle, battleLog);
        } else {
            battle.setTurn(battle.getTurn() + 1);
            createBattleLog(battle);
            battleLog.setDamageRoll("[0]");
            battleLog.setDamageResult(0L);
        }

        return battleLogRepository.save(battleLog);
    }

    private void validateBattleLog(BattleLog battleLog) {
        if (battleLog.getAttackResult() == null || battleLog.getDefenseResult() == null) {
            throw new NullPointerException();
        }
    }

    private void calculateDamageAndApply(Battle battle, BattleLog battleLog) {
        Character character = battleLog.getCharacterAttacking();
        List<Long> damageRoll = calculateDamageRoll(character);

        long damageResult = battleLog.getAttackResult()
                - battleLog.getDefenseResult()
                + character.getStrength();

        for (Long roll : damageRoll) {
            damageResult += roll;
        }

        battleLog.setDamageRoll(damageRoll.toString());
        battleLog.setDamageResult(damageResult);

        updateCharacterLife(battle, battleLog, damageResult);
        updateBattleState(battle, battleLog);
    }

    private List<Long> calculateDamageRoll(Character character) {
        List<Long> damageRoll = new ArrayList<>();

        for (int i = 0; i < character.getDiceQuantity(); i++) {
            long roll = roll(character.getDiceFaces());
            damageRoll.add(roll);
        }

        return damageRoll;
    }

    private void updateCharacterLife(Battle battle, BattleLog battleLog, long damage) {
        Character defender = battleLog.getCharacterDefending();
        if (battle.getMainCharacter() == defender) {
            battle.setMainCharacterLife(battle.getMainCharacterLife() - damage);
        } else {
            battle.setOpponentLife(battle.getOpponentLife() - damage);
        }
    }

    private void updateBattleState(Battle battle, BattleLog battleLog) {
        if (battle.getMainCharacterLife() < 0 || battle.getOpponentLife() < 0) {
            battle.setWinner(battleLog.getCharacterAttacking());
        } else {
            battle.setTurn(battle.getTurn() + 1);
            createBattleLog(battle);
        }
    }

    private long roll(long n) {
        Random random = new Random();
        return random.nextLong(n) + 1;
    }

    private Character getRandomMonsterOpponent(List<Character> allCharacters, Character yourCharacter) {

        List<Character> potentialOpponents = allCharacters.stream()
                .filter(character -> character.getCategory()
                        .equals(CharacterCategory.MONSTER) && !character.equals(yourCharacter))
                .toList();

        if (potentialOpponents.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(potentialOpponents.size());
        return potentialOpponents.get(randomIndex);
    }

}
