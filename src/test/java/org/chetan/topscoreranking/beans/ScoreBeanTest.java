package org.chetan.topscoreranking.beans;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class ScoreBeanTest {

	@Test
	public void test_toEntity() {
		ZonedDateTime now = ZonedDateTime.now();
		ScoreBean scoreBean = ScoreBean.builder()
				.id(1)
				.player("testOne")
				.score(10)
				.time(now)
				.build();

		ScoreEntity expectedEntity = ScoreEntity.builder()
				.playerName("TESTONE")
				.score(10)
				.time(now)
				.build();
		ScoreEntity actualEntity = scoreBean.toEntity();
		org.assertj.core.api.Assertions.assertThat(expectedEntity)
				.usingRecursiveComparison()
				.isEqualTo(actualEntity);
	}

}