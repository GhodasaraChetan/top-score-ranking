package org.chetan.topscoreranking.repositories;

import org.springframework.data.domain.Pageable;
import java.time.ZonedDateTime;
import java.util.List;
import org.chetan.topscoreranking.beans.ScoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScoreRepository extends PagingAndSortingRepository<ScoreEntity, Long> {

	//withoutPlayers
	List<ScoreEntity> findAllByTimeAfter(ZonedDateTime timeAfter, Pageable pageable);

	List<ScoreEntity> findAllByTimeBefore(ZonedDateTime timeBefore, Pageable pageable);

	List<ScoreEntity> findAllByTimeAfterAndTimeBefore(ZonedDateTime timeAfter, ZonedDateTime timeBefore, Pageable pageable);

	//withPlayers
	List<ScoreEntity> findAllByPlayerNameIn(List<String> playerNames, Pageable pageable);

	List<ScoreEntity> findAllByPlayerNameInAndTimeAfter(List<String> playerNames, ZonedDateTime timeAfter, Pageable pageable);

	List<ScoreEntity> findAllByPlayerNameInAndTimeBefore(List<String> playerNames, ZonedDateTime timeBefore, Pageable pageable);

	List<ScoreEntity> findAllByPlayerNameInAndTimeAfterAndTimeBefore(List<String> playerNames, ZonedDateTime timeAfter, ZonedDateTime timeBefore, Pageable pageable);
}
