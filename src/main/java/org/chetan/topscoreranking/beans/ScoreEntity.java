package org.chetan.topscoreranking.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "D_SCORES")
public class ScoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_SCORE")
	private long id;
	@Column(name = "W_PLAYER_NAME", nullable = false)
	private String playerName;
	@Column(name = "N_SCORE", nullable = false)
	private int score;
	@Column(name = "TSZ_TIME", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime time;

	@JsonIgnore
	public ScoreBean toScoreBean() {
		return ScoreBean.builder()
				.id(id)
				.player(playerName)
				.score(score)
				.time(time)
				.build();
	}

	@JsonIgnore
	public PlayerScoreHistory.ScoreData toPlayerHistoryScoreData() {
		return PlayerScoreHistory.ScoreData.builder()
				.score(score)
				.time(time)
				.build();
	}
}
