package org.chetan.topscoreranking.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ScoreRepositoryTest {

	@Autowired
	private ScoreRepository scoreRepository;

	//save
	@Test
	public void should_create_new_score() {
		ScoreEntity scoreEntity = getScoreEntity();
		scoreRepository.save(scoreEntity);
		List<ScoreEntity> allScores = (List<ScoreEntity>) scoreRepository.findAll();
		assertThat(allScores.size()).isEqualTo(1);
		assertThat(allScores).extracting(ScoreEntity::getPlayerName).containsOnly("testOne");
	}

	@Test
	public void should_create_unique_id_every_time() {
		List<ScoreEntity> scoreList = IntStream.range(0, 3)
				.mapToObj(x -> getScoreEntity())
				.collect(Collectors.toList());
		scoreRepository.saveAll(scoreList);
		List<ScoreEntity> allScores = (List<ScoreEntity>) scoreRepository.findAll();
		assertThat(allScores.size()).isEqualTo(3);
		assertThat(allScores).extracting(ScoreEntity::getPlayerName).containsOnly("testOne");
		assertThat(allScores).extracting(ScoreEntity::getScore).containsOnly(10);
		assertThat(allScores).extracting(ScoreEntity::getId).doesNotHaveDuplicates();
	}

	//findById
	@Test
	public void should_find_by_id() {
		ScoreEntity scoreEntity = getScoreEntity();
		ScoreEntity savedScore = scoreRepository.save(scoreEntity);
		Optional<ScoreEntity> foundScore = scoreRepository.findById(savedScore.getId());
		assertTrue(foundScore.isPresent());
		assertAll(() -> {
			assertThat(foundScore.get().getPlayerName()).isEqualTo("testOne");
			assertThat(foundScore.get().getScore()).isEqualTo(10);
			assertThat(foundScore.get().getTime()).isBefore(ZonedDateTime.now());
		});

	}

	//deleteById
	@Test
	public void should_delete_by_id() {
		ScoreEntity scoreEntity = getScoreEntity();
		ScoreEntity savedScore = scoreRepository.save(scoreEntity);
		List<ScoreEntity> allScoresAfterSave = (List<ScoreEntity>) scoreRepository.findAll();
		assertThat(allScoresAfterSave.size()).isEqualTo(1);
		assertThat(allScoresAfterSave).extracting(ScoreEntity::getId).containsOnly(savedScore.getId());

		scoreRepository.deleteById(savedScore.getId());
		List<ScoreEntity> allScoresAfterDelete = (List<ScoreEntity>) scoreRepository.findAll();
		assertThat(allScoresAfterDelete.size()).isEqualTo(0);
	}

	private ScoreEntity getScoreEntity() {
		return ScoreEntity.builder()
				.playerName("testOne")
				.score(10)
				.time(ZonedDateTime.now())
				.build();
	}

}