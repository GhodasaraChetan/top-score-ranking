package org.chetan.topscoreranking.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.chetan.topscoreranking.beans.PlayerScoreHistory;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.chetan.topscoreranking.repositories.ScoreRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerService {
	private ScoreRepository scoreRepository;

	public PlayerScoreHistory getScoreHistory(String playerName) {
		List<ScoreEntity> playersScoreEntities = scoreRepository.findAllByPlayerName(playerName.trim().toUpperCase());

		if (playersScoreEntities.isEmpty()) {
			return PlayerScoreHistory.builder()
					.bestScore(null)
					.lowestScore(null)
					.averageScore(0)
					.scoreHistory(Collections.emptyList())
					.build();
		}

		List<PlayerScoreHistory.ScoreData> playerScoreData = playersScoreEntities.stream()
				.map(ScoreEntity::toPlayerHistoryScoreData)
				.collect(Collectors.toList());

		return PlayerScoreHistory.builder()
				.bestScore(playerScoreData.stream().max(Comparator.comparing(PlayerScoreHistory.ScoreData::getScore)).orElse(null))
				.lowestScore(playerScoreData.stream().min(Comparator.comparing(PlayerScoreHistory.ScoreData::getScore)).orElse(null))
				.averageScore((double) playerScoreData.stream().mapToInt(PlayerScoreHistory.ScoreData::getScore).sum() / playerScoreData.size())
				.scoreHistory(playerScoreData)
				.build();
	}
}
