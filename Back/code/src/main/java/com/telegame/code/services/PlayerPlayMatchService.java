package com.telegame.code.services;

import com.telegame.code.exceptions.InputFormException;
import com.telegame.code.exceptions.match.MatchInfoException;
import com.telegame.code.exceptions.match.MatchNoExistsException;
import com.telegame.code.exceptions.match.PlayerNotInMatchException;
import com.telegame.code.exceptions.player.PlayerNameException;
import com.telegame.code.forms.games.LaserBoardMoveForm;
import com.telegame.code.models.Board;
import com.telegame.code.models.GameMatch;
import com.telegame.code.models.Player;
import com.telegame.code.models.PlayerPlayMatch;
import com.telegame.code.models.games.laserboard.LaserBoard;
import com.telegame.code.repos.BoardRepo;
import com.telegame.code.repos.GameMatchRepo;
import com.telegame.code.repos.PlayerPlayMatchRepo;
import com.telegame.code.repos.PlayerRepo;
import com.telegame.code.services.games.LaserBoardService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PlayerPlayMatchService {
    private ValidatorFactory validatorFactory;
    private GameMatchRepo gameMatchRepo;
    private PlayerRepo playerRepo;
    private PlayerPlayMatchRepo playerPlayMatchRepo;
    private LaserBoardService laserBoardService;
    private BoardRepo boardRepo;

    public String doAction(LaserBoardMoveForm actionForm, Long matchId, String playerName) {
        Set<ConstraintViolation<LaserBoardMoveForm>> formErrorList = validatorFactory.getValidator().validate(actionForm);
        if (!formErrorList.isEmpty()) throw new InputFormException();

        Optional<GameMatch> gameMatchOptional = gameMatchRepo.findById(matchId);
        if (gameMatchOptional.isEmpty()) throw new MatchNoExistsException();
        GameMatch gameMatch = gameMatchOptional.get();

        Optional<Player> playerOptional = playerRepo.findByPlayerNameEquals(playerName);
        if (playerOptional.isEmpty()) throw new PlayerNameException();
        Player player = playerOptional.get();

        if (!playerInGame(player, gameMatch)) throw new PlayerNotInMatchException();

        Optional<Board> boardOptional = boardRepo.findBoardByGameMatch(gameMatch);
        if (boardOptional.isEmpty()) throw new MatchInfoException();
        Board board = boardOptional.get();

        String message;
        if (board instanceof LaserBoard) {
            message = laserBoardService.movePiece((LaserBoard) board, actionForm, gameMatch);
        } else throw new MatchInfoException();

        return message;
    }

    private boolean playerInGame(Player player, GameMatch gameMatch) {
        List<PlayerPlayMatch> playersInMatch = playerPlayMatchRepo.findByGameMatchEquals(gameMatch);
        for (PlayerPlayMatch playerInMatch : playersInMatch) {
            if (playerInMatch.getPlayer().getId().equals(player.getId())) return true;
        }
        return false;
    }
}
