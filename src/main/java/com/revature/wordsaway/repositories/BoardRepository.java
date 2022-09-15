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

    @Transactional
    @Modifying
    @Query(value = "UPDATE boards SET letters = ?2 WHERE id = ?1", nativeQuery = true)
    void updateBoardLettersByID(UUID gameID, char[] letters);
}