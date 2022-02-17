package org.chetan.topscoreranking.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.chetan.topscoreranking.exceptions.ScoreNotFoundException;
import org.chetan.topscoreranking.repositories.ScoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

	@InjectMocks
	ScoreService scoreService;

	@Mock
	ScoreRepository scoreRepository;

	@Test
	void test_saveScore() {
		ScoreBean inputScore = getScoreBean();
		when(scoreRepository.save(any(ScoreEntity.class))).thenReturn(getScoreEntity());
		ScoreBean savedScore = scoreService.saveScore(inputScore);
		verify(scoreRepository, times(1)).save(any(ScoreEntity.class));
		assertThat(savedScore.getId()).isNotNull();
	}

	@Test
	void test_getScoreById_found() {
		ScoreEntity scoreEntity = getScoreEntity();
		when(scoreRepository.findById(anyLong())).thenReturn(Optional.of(scoreEntity));
		ScoreBean returnedScoreBean = scoreService.getScoreById(scoreEntity.getId());
		verify(scoreRepository, times(1)).findById(1L);
		assertThat(returnedScoreBean.getId()).isEqualTo(scoreEntity.getId());
	}

	@Test
	void test_getScoreById_not_found() {
		when(scoreRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(ScoreNotFoundException.class, () -> scoreService.getScoreById(1));
		verify(scoreRepository, times(1)).findById(1L);
	}

	@Test
	void test_deleteScoreById_present() {
		when(scoreRepository.findById(anyLong())).thenReturn(Optional.of(getScoreEntity()));
		scoreService.deleteScoreById(1);
		verify(scoreRepository, times(1)).findById(1L);
		verify(scoreRepository, times(1)).deleteById(1L);
	}

	@Test
	void test_deleteScoreById_not_present() {
		when(scoreRepository.findById(anyLong())).thenReturn(Optional.empty());
		scoreService.deleteScoreById(1);
		verify(scoreRepository, times(1)).findById(1L);
		verify(scoreRepository, times(0)).deleteById(1L);
	}

	private ScoreBean getScoreBean() {
		return ScoreBean.builder()
				.player("testOne")
				.score(10)
				.time(ZonedDateTime.now())
				.build();
	}

	private ScoreEntity getScoreEntity() {
		return ScoreEntity.builder()
				.id(1)
				.playerName("testOne")
				.score(10)
				.time(ZonedDateTime.now())
				.build();
	}
}