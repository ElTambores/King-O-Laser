package com.telegame.code.repos;

import com.telegame.code.models.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepo extends JpaRepository<Board, Long> {
}