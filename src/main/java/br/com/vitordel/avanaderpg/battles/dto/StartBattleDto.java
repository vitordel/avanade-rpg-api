package br.com.vitordel.avanaderpg.battles.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StartBattleDto {

    @NotEmpty(message = "The character is required")
    private Long myCharacterId;

    private Long myOpponentId;
}

