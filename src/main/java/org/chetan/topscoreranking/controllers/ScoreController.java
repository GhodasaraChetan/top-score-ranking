package org.chetan.topscoreranking.controllers;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.chetan.topscoreranking.services.ScoreService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ScoreController {

	private ScoreService scoreService;

	@PostMapping("/scores")
	public ScoreBean createScore(@Valid @RequestBody ScoreBean scoreBean) {
		return scoreService.saveScore(scoreBean);
	}

	@GetMapping("/scores/{id}")
	public ScoreBean getScore(@PathVariable long id) {
		return scoreService.getScoreById(id);
	}

	@DeleteMapping("/scores/{id}")
	public void deleteScore(@PathVariable long id) {
		scoreService.deleteScoreById(id);
	}

}
