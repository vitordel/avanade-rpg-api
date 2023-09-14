package br.com.vitordel.avanaderpg.battles.service;

import br.com.vitordel.avanaderpg.battles.dto.StartBattleDto;
import br.com.vitordel.avanaderpg.battles.model.Battle;
import br.com.vitordel.avanaderpg.battles.model.BattleLog;
import br.com.vitordel.avanaderpg.battles.repository.BattleLogRepository;
import br.com.vitordel.avanaderpg.battles.repository.BattleRepository;
import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.service.CharacterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<BattleLog> getBattleLogs(Long battleId) {
        return battleLogRepository.findByBattleId(battleId);
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
                opponentCharacter = getRandomOpponent(allCharacters, myCharacter);
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

        Long attack = attackRoll
                + battleLog.getCharacterAttacking().getStrength()
                + battleLog.getCharacterAttacking().getAgility();

        battleLog.setAttack(attack);

        return battleLogRepository.save(battleLog);
    }

    @Override
    public BattleLog performDefense(Battle battle) {
        Long defenseRoll = roll(12);

        BattleLog battleLog = battleLogRepository.findByBattleIdAndTurn(battle.getId(), battle.getTurn());

        Long defense = defenseRoll
                + battleLog.getCharacterDefending().getDefense()
                + battleLog.getCharacterDefending().getAgility();
        battleLog.setDefense(defense);

        return battleLogRepository.save(battleLog);
    }

    @Override
    public BattleLog calculateDamage(Battle battle) {
        BattleLog battleLog = battleLogRepository.findByBattleIdAndTurn(battle.getId(), battle.getTurn());

        if (battleLog.getAttack() == null || battleLog.getDefense() == null) {
            throw new NullPointerException();
        }

        battle.setTurn(battle.getTurn()+1);
        createBattleLog(battle);

        if(battleLog.getAttack() <= battleLog.getDefense()) {
            battleLog.setDamage(0L);
            return battleLogRepository.save(battleLog);
        }

        Character character = battleLog.getCharacterAttacking();
        long damageRoll = 0L;

        for(int i = 0; i < character.getDiceQuantity(); i++) {
            long roll = roll(battleLog.getCharacterAttacking().getDiceFaces());
            damageRoll += roll;
        }

        long damage = battleLog.getAttack()
                - battleLog.getDefense()
                + battleLog.getCharacterAttacking().getStrength()
                + damageRoll;

        battleLog.setDamage(damage);

        if(battle.getMainCharacter() == battleLog.getCharacterDefending()) {
            battle.setMainCharacterLife(battle.getMainCharacterLife()-damage);
        } else {
            battle.setOpponentLife(battle.getOpponentLife()-damage);
        }

        if (battle.getMainCharacterLife() < 0 || battle.getOpponentLife() < 0) {
            battle.setWinner(battleLog.getCharacterAttacking());
        }

        return battleLogRepository.save(battleLog);
    }

    private long roll(long n) {
        Random random = new Random();
        return random.nextLong(n) + 1;
    }

    private Character getRandomOpponent(List<Character> allCharacters, Character yourCharacter) {

        List<Character> potentialOpponents = allCharacters.stream()
                .filter(character -> !character.equals(yourCharacter))
                .toList();

        if (potentialOpponents.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(potentialOpponents.size());
        return potentialOpponents.get(randomIndex);
    }
}
