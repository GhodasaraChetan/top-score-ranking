package org.chetan.topscoreranking.services;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.chetan.topscoreranking.beans.PlayerScoreHistory;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.chetan.topscoreranking.repositories.ScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

	@InjectMocks
	PlayerService playerService;

	@Mock
	ScoreRepository scoreRepository;

	@Test
	public void test_player_history_empty() {
		when(scoreRepository.findAllByPlayerName(anyString())).thenReturn(Collections.emptyList());
		PlayerScoreHistory actual = playerService.getScoreHistory("abc");
		Assertions.assertThat(actual).usingRecursiveComparison()
				.isEqualTo(PlayerScoreHistory.builder().averageScore(0).scoreHistory(Collections.emptyList()).build());
	}

	@Test
	public void test_player_history_single() {
		ZonedDateTime zdt = ZonedDateTime.now();

		//prepare expected
		PlayerScoreHistory.ScoreData score = PlayerScoreHistory.ScoreData.builder().score(0).time(zdt).build();
		PlayerScoreHistory expected = PlayerScoreHistory.builder()
				.bestScore(score)
				.lowestScore(score)
				.averageScore(0)
				.scoreHistory(List.of(score))
				.build();

		//prepare data
		when(scoreRepository.findAllByPlayerName(anyString())).thenReturn(List.of(ScoreEntity.builder().id(1).playerName("test").score(0).time(zdt).build()));

		//actual
		PlayerScoreHistory actual = playerService.getScoreHistory("test");

		//assert
		Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}


	@Test
	public void test_player_history_multiple() {
		ZonedDateTime zdt = ZonedDateTime.now();

		//prepare expected
		PlayerScoreHistory.ScoreData score1 = PlayerScoreHistory.ScoreData.builder().score(10).time(zdt).build();
		PlayerScoreHistory.ScoreData score2 = PlayerScoreHistory.ScoreData.builder().score(20).time(zdt).build();
		PlayerScoreHistory.ScoreData score3 = PlayerScoreHistory.ScoreData.builder().score(30).time(zdt).build();
		PlayerScoreHistory expected = PlayerScoreHistory.builder()
				.bestScore(score3)
				.lowestScore(score1)
				.averageScore(20)
				.scoreHistory(List.of(score1, score2, score3))
				.build();

		//prepare data
		List<ScoreEntity> scoreEntities = List.of(ScoreEntity.builder().id(1).playerName("test").score(10).time(zdt).build(),
				ScoreEntity.builder().id(2).playerName("test").score(20).time(zdt).build(),
				ScoreEntity.builder().id(3).playerName("test").score(30).time(zdt).build());
		when(scoreRepository.findAllByPlayerName(anyString())).thenReturn(scoreEntities);

		//actual
		PlayerScoreHistory actual = playerService.getScoreHistory("test");

		//assert
		Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}
}