package org.chetan.topscoreranking.services;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.chetan.topscoreranking.exceptions.ScoreNotFoundException;
import org.chetan.topscoreranking.repositories.ScoreRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ScoreService {

	private ScoreRepository scoreRepository;

	public ScoreBean saveScore(ScoreBean score) {
		ScoreEntity scoreEntity = score.toEntity();
		ScoreEntity savedEntity = scoreRepository.save(scoreEntity);
		ScoreBean savedBean = savedEntity.toScoreBean();
		log.info("Score Saved: {}", savedBean);
		return savedBean;
	}

	public ScoreBean getScoreById(long id) {
		Optional<ScoreEntity> optionalScoreEntity = scoreRepository.findById(id);
		if (optionalScoreEntity.isEmpty()) {
			throw new ScoreNotFoundException("The score with id " + id + ", is not present in DB.");
		}
		ScoreBean scoreBean = optionalScoreEntity.get().toScoreBean();
		log.info("Score found: {}", scoreBean);
		return scoreBean;
	}

	public void deleteScoreById(long id) {
		if (scoreRepository.findById(id).isPresent()) {
			scoreRepository.deleteById(id);
			log.info("Score deleted with id: {}", id);
		} else {
			log.info("Score did not exist with id: {}", id);
		}
	}
}
