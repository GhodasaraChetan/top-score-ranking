package org.chetan.topscoreranking.controllers;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.chetan.topscoreranking.services.ScoreService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class ScoreController {

	private ScoreService scoreService;

	@PostMapping("/scores")
	public ScoreBean createScore(@Valid @RequestBody ScoreBean scoreBean) {
		log.info("Post Score request: " + scoreBean);
		ScoreBean savedScore = scoreService.saveScore(scoreBean);
		log.info("Post Score done. " + scoreBean);
		return savedScore;
	}

	@GetMapping("/scores/{id}")
	public ScoreBean getScore(@PathVariable long id) {
		log.info("Get Score request for id:" + id);
		ScoreBean result = scoreService.getScoreById(id);
		log.info("Get Score result: " + result);
		return result;
	}

	@DeleteMapping("/scores/{id}")
	public void deleteScore(@PathVariable long id) {
		log.info("Delete Score request for id:" + id);
		scoreService.deleteScoreById(id);

	}

	@GetMapping("/scores")
	public List<ScoreBean> searchScore(
			@RequestParam(defaultValue = "") List<String> players,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime beforeZdt,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) ZonedDateTime afterZdt,
			@RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int size
	) {
		log.info("Search Score request: players = {}, beforeZdt = {}, afterZdt = {}, offset = {}, size = {}",
				players, beforeZdt, afterZdt, offset, size);
		List<ScoreBean> result = scoreService.searchScore(players, beforeZdt, afterZdt, offset, size);
		log.info("Search Score result: " + result);
		return result;
	}
}
