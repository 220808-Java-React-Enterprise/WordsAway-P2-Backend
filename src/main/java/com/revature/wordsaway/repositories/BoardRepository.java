package com.revature.wordsaway.repositories;

import com.revature.wordsaway.models.Board;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends CrudRepository<Board, UUID> {
    @Query(value = "SELECT * FROM boards WHERE id = ?1", nativeQuery = true)
    Board findBoardByID(UUID id);

    @Query(value = "SELECT * FROM boards WHERE game_id = ?1", nativeQuery = true)
    List<Board> findBoardByGameID(UUID gameID);
}