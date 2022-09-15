package com.revature.wordsaway.repositories;

import com.revature.wordsaway.models.Board;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends CrudRepository<Board, UUID> {
    @Query(value = "SELECT * FROM boards WHERE id = ?1", nativeQuery = true)
    Board findBoardByID(UUID id);

    @Query(value = "SELECT * FROM boards WHERE game_id = ?1", nativeQuery = true)
    List<Board> findBoardByGameID(UUID gameID);

    @Query(value = "SELECT * FROM boards WHERE game_id = ?2 AND id != ?1", nativeQuery = true)
    Board findOpposingBoardByIDAndGameID(UUID id, UUID gameID);

    @Transactional
    @Modifying
    @Query(value = "UPDATE boards SET fireballs = ?2 , is_active = ?3, letters = ?4, tray = ?5, worms = ?6 WHERE id = ?1", nativeQuery = true)
    void updateBoard(UUID gameID, int fireballs, boolean isActive, char[] letters, char[] tray, char[] worms);
}