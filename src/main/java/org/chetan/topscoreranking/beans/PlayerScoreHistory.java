package org.chetan.topscoreranking.beans;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerScoreHistory {
	private ScoreData bestScore;
	private ScoreData lowestScore;
	private double averageScore;
	private List<ScoreData> scoreHistory;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ScoreData {
		private int score;
		private ZonedDateTime time;
	}
}
