package com.revature.wordsaway.models;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    private UUID id;
    @OneToOne
    @JoinColumn(name="username", referencedColumnName = "username")
    private User user;
    @Column(name = "tray", nullable = false, length = 7)
    private char[] tray;
    @Column(name = "bombs", nullable = false)
    private int bombs;
    @Column(name = "ships", nullable = false, length = 215)
    private char[] ships;
    @Column(name = "letters", nullable = false, length = 215)
    private char[] letters;
    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    @OneToOne
    @JoinColumn(name = "opponent_board", nullable = false)
    private Board opponentBoard;
}
