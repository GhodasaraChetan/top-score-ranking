package org.chetan.topscoreranking.repositories;

import org.chetan.topscoreranking.beans.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Long> {
}
