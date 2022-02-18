package org.chetan.topscoreranking.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chetan.topscoreranking.beans.PlayerScoreHistory;
import org.chetan.topscoreranking.services.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class PlayerController {

	private PlayerService playerService;

	@GetMapping("/players/{playerName}/scorehistory")
	public PlayerScoreHistory getPlayerScoreHistory(@Valid @NotEmpty @PathVariable String playerName) {
		log.info("Get player's score history for playerName: " + playerName);
		PlayerScoreHistory result = playerService.getScoreHistory(playerName);
		log.info("Player's score history: " + result);
		return result;
	}
}
