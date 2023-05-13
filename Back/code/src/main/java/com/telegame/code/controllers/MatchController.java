package com.telegame.code.controllers;

import com.telegame.code.exceptions.*;
import com.telegame.code.exceptions.match.FilledMatchException;
import com.telegame.code.exceptions.match.MatchInfoException;
import com.telegame.code.exceptions.match.MatchNoExistsException;
import com.telegame.code.exceptions.match.PlayerAlreadyInMatchException;
import com.telegame.code.exceptions.player.PlayerNameException;
import com.telegame.code.forms.JoinMatchForm;
import com.telegame.code.forms.MatchForm;
import com.telegame.code.services.MatchService;
import com.telegame.code.services.PlayerService;
import com.telegame.code.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@CrossOrigin()
@RestController
@AllArgsConstructor
public class MatchController {
    MatchService matchService;
    PlayerService playerService;
    TokenService tokenService;

    @PostMapping("/match")
    public ResponseEntity<String> createMatch(@RequestBody MatchForm matchForm, HttpServletRequest request) throws NoSuchAlgorithmException {
        try {
            return new ResponseEntity<>(matchService.createMatch(matchForm, request.getAttribute("playerName").toString()), HttpStatus.OK);
        } catch (InputFormException e) {
            return new ResponseEntity<>("Match form error", HttpStatus.BAD_REQUEST);
        } catch (PlayerNameException e) {
            return new ResponseEntity<>("Player no exists", HttpStatus.BAD_REQUEST);
        } catch (MatchInfoException e) {
            return new ResponseEntity<>("Game Metadata Error", HttpStatus.CONFLICT);
        } catch (GameNoExistsException e) {
            return new ResponseEntity<>("Game no exists", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/match/{matchId}")
    public ResponseEntity<String> joinMatch(@RequestBody JoinMatchForm joinMatchForm, @PathVariable Long matchId, HttpServletRequest request) throws NoSuchAlgorithmException {
        try {
            return new ResponseEntity<>(matchService.joinMatch(matchId, joinMatchForm, request.getAttribute("playerName").toString()), HttpStatus.OK);
        } catch (FilledMatchException e) {
            return new ResponseEntity<>("The match has no empty seats", HttpStatus.LOCKED);
        } catch (MatchNoExistsException e) {
            return new ResponseEntity<>("Match not found", HttpStatus.NOT_FOUND);
        } catch (PlayerNameException e) {
            return new ResponseEntity<>("Player can no join match because he no exists", HttpStatus.CONFLICT);
        } catch (PlayerAlreadyInMatchException e) {
            return new ResponseEntity<>("You are already in this match", HttpStatus.CONFLICT);
        } catch (MatchInfoException e){
            return new ResponseEntity<>("Wrong password", HttpStatus.CONFLICT);
        }
    }
}
