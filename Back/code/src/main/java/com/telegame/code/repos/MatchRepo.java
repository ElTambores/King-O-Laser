package com.telegame.code.repos;

import com.telegame.code.models.GameMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepo extends JpaRepository<GameMatch, Long> {
}
