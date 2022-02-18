package org.chetan.topscoreranking.beans;

import java.time.ZonedDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreEntityTest {

	@Test
	public void test_toScoreBean() {

		ZonedDateTime now = ZonedDateTime.now();
		ScoreEntity scoreEntity = ScoreEntity.builder()
				.id(1000)
				.playerName("TESTONE")
				.score(10)
				.time(now)
				.build();
		ScoreBean expectedScoreBean = ScoreBean.builder()
				.id(1000)
				.player("TESTONE")
				.score(10)
				.time(now)
				.build();

		ScoreBean actualScoreBean = scoreEntity.toScoreBean();
		Assertions.assertThat(actualScoreBean)
				.usingRecursiveComparison()
				.isEqualTo(expectedScoreBean);
	}

	@Test
	void test_toPlayerHistoryScoreData() {
		ZonedDateTime now = ZonedDateTime.now();
		ScoreEntity scoreEntity = ScoreEntity.builder()
				.id(1000)
				.playerName("TESTONE")
				.score(10)
				.time(now)
				.build();
		PlayerScoreHistory.ScoreData expectedScoreData = PlayerScoreHistory.ScoreData.builder()
				.score(10)
				.time(now)
				.build();

		PlayerScoreHistory.ScoreData actualScoreData = scoreEntity.toPlayerHistoryScoreData();
		Assertions.assertThat(actualScoreData)
				.usingRecursiveComparison()
				.isEqualTo(expectedScoreData);
	}
}