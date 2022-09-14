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
    @Column(name = "fireballs", nullable = false)
    private int fireballs;
    @Column(name = "worms", nullable = false, length = 15*15)
    private char[] worms;
    @Column(name = "letters", nullable = false, length = 15*15)
    private char[] letters;
    @Column(name = "game_id", nullable = false)
    private UUID gameID;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public char[] getTray() {
        return tray;
    }

    public int getFireballs() {
        return fireballs;
    }

    public char[] getWorms() {
        return worms;
    }

    public char[] getLetters() {
        return letters;
    }

    public UUID getGameID() {
        return gameID;
    }

    public boolean isActive() {
        return isActive;
    }
}
