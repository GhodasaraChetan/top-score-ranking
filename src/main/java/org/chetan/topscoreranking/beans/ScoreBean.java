package org.chetan.topscoreranking.beans;

import java.time.ZonedDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreBean {
	private long id;
	@NotBlank(message = "Player Name cannot be blank")
	private String player;
	@NotNull(message = "Player Score cannot be null")
	@Positive(message = "Player Score should only be positive integer")
	private int score;
	@NotNull(message = "Time cannot be null")
	private ZonedDateTime time;

	public ScoreEntity toEntity() {
		return ScoreEntity.builder()
				//id will be created automatically
				//always store data in uppercase
				.playerName(player.trim().toUpperCase())
				.score(score)
				.time(time)
				.build();
	}
}
