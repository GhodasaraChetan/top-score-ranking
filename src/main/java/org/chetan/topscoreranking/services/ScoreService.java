package org.chetan.topscoreranking.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.chetan.topscoreranking.exceptions.ScoreNotFoundException;
import org.chetan.topscoreranking.repositories.ScoreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ScoreService {

	private ScoreRepository scoreRepository;

	public ScoreBean saveScore(ScoreBean score) {
		ScoreEntity scoreEntity = score.toEntity();
		ScoreEntity savedEntity = scoreRepository.save(scoreEntity);
		return savedEntity.toScoreBean();
	}

	public ScoreBean getScoreById(long id) {
		Optional<ScoreEntity> optionalScoreEntity = scoreRepository.findById(id);
		if (optionalScoreEntity.isEmpty()) {
			throw new ScoreNotFoundException("The score with id " + id + ", is not present in DB.");
		}
		return optionalScoreEntity.get().toScoreBean();
	}

	public void deleteScoreById(long id) {
		if (scoreRepository.findById(id).isPresent()) {
			scoreRepository.deleteById(id);
			log.info("Score deleted with id: {}", id);
		} else {
			log.info("Score did not exist with id: {}", id);
		}
	}

	private List<ScoreBean> searchScoreWithOutPlayers(ZonedDateTime beforeZdt, ZonedDateTime afterZdt, int offset, int size) {

		List<ScoreEntity> scoreEntities = null;
		if (null == afterZdt && null == beforeZdt) {
			//no filter on time
			scoreEntities = scoreRepository.findAll(PageRequest.of(offset, size)).toList();
		} else if (null == afterZdt) {
			//only apply filter on beforeZdt
			scoreEntities = scoreRepository.findAllByTimeBefore(beforeZdt, PageRequest.of(offset, size));
		} else if (null == beforeZdt) {
			//only apply filter on afterZdt
			scoreEntities = scoreRepository.findAllByTimeAfter(afterZdt, PageRequest.of(offset, size));
		} else {
			//apply all filters
			scoreEntities = scoreRepository.findAllByTimeAfterAndTimeBefore(afterZdt, beforeZdt, PageRequest.of(offset, size));
		}

		return scoreEntities.stream().map(ScoreEntity::toScoreBean).collect(Collectors.toList());
	}

	private List<ScoreBean> searchScoreWithPlayers(List<String> players, ZonedDateTime beforeZdt, ZonedDateTime afterZdt, int offset, int size) {
		List<String> playersForQuery = players.stream().map(x -> x.trim().toUpperCase(Locale.ROOT)).collect(Collectors.toList());
		List<ScoreEntity> scoreEntities = null;
		if (null == afterZdt && null == beforeZdt) {
			//no filter on time
			scoreEntities = scoreRepository.findAllByPlayerNameIn(playersForQuery, PageRequest.of(offset, size));
		} else if (null == afterZdt) {
			//only apply filter on beforeZdt
			scoreEntities = scoreRepository.findAllByPlayerNameInAndTimeBefore(playersForQuery, beforeZdt, PageRequest.of(offset, size));
		} else if (null == beforeZdt) {
			//only apply filter on afterZdt
			scoreEntities = scoreRepository.findAllByPlayerNameInAndTimeAfter(playersForQuery, afterZdt, PageRequest.of(offset, size));
		} else {
			//apply all filters
			scoreEntities = scoreRepository.findAllByPlayerNameInAndTimeAfterAndTimeBefore(playersForQuery, afterZdt, beforeZdt, PageRequest.of(offset, size));
		}

		return scoreEntities.stream().map(ScoreEntity::toScoreBean).collect(Collectors.toList());
	}

	public List<ScoreBean> searchScore(List<String> players, ZonedDateTime beforeZdt, ZonedDateTime afterZdt, int offset, int size) {
		if (players.isEmpty()) {
			return searchScoreWithOutPlayers(beforeZdt, afterZdt, offset, size);
		}
		return searchScoreWithPlayers(players, beforeZdt, afterZdt, offset, size);
	}
}
