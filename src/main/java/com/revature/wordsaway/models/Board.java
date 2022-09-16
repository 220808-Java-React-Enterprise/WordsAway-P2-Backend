package com.revature.wordsaway.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.UUID;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

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
    @Column(name = "worms", nullable = false, length = BOARD_SIZE*BOARD_SIZE)
    private char[] worms;
    @Column(name = "letters", nullable = false, length = BOARD_SIZE*BOARD_SIZE)
    private char[] letters;
    @Column(name = "game_id", nullable = false)
    private UUID gameID;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Transient
    private char[][] lettersRows;
    @Transient
    private char[][] lettersColumns;

    protected Board() {
    }

    public Board(UUID id, User user, char[] tray, int fireballs, char[] worms, char[] letters, UUID gameID, boolean isActive) {
        this.id = id;
        this.user = user;
        this.tray = tray;
        this.fireballs = fireballs;
        this.worms = worms;
        this.letters = letters;
        this.gameID = gameID;
        this.isActive = isActive;
    }

    public Board(Board oldBoard, char[] move){
        this.id = oldBoard.getId();
        this.user = oldBoard.getUser();
        this.tray = oldBoard.getTray();
        this.fireballs = oldBoard.getFireballs();
        this.worms = oldBoard.getWorms();
        this.letters = move;
        this.gameID = oldBoard.getGameID();
        this.isActive = oldBoard.isActive();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public char[] getTray() {
        return tray;
    }

    public void setTray(char[] tray) {
        this.tray = tray;
    }

    public int getFireballs() {
        return fireballs;
    }

    public void setFireballs(int fireballs) {
        this.fireballs = fireballs;
    }

    public char[] getWorms() {
        return worms;
    }

    public char[] getLetters() {
        return letters;
    }

    public void setLetters(char[] letters) {
        this.letters = letters;
    }

    public UUID getGameID() {
        return gameID;
    }

    public boolean isActive() {
        return isActive;
    }

    public char[] getRow(int index){
        if(index >= BOARD_SIZE) throw new IllegalArgumentException("Can not get row outside of board range.");
        return getRows()[index];
    }

    public char[][] getRows(){
        if(lettersRows != null) return lettersRows;
        char[][] lettersRows = new char[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            lettersRows[i] = new char[BOARD_SIZE];
            for(int j = 0; j < BOARD_SIZE; j++){
                lettersRows[i][j] = letters[i * BOARD_SIZE + j];
            }
        }
        return lettersRows;
    }

    public char[] getColumn(int index){
        if(index >= BOARD_SIZE) throw new IllegalArgumentException("Can not get row outside of board range.");
        return getColumns()[index];
    }

    public char[][] getColumns(){
        if(lettersColumns != null) return lettersColumns;
        char[][] lettersColumns = new char[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            lettersColumns[i] = new char[BOARD_SIZE];
            for(int j = 0; j < BOARD_SIZE; j++){
                lettersColumns[i][j] = letters[i + j * BOARD_SIZE];
            }
        }
        return lettersColumns;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", user=" + user +
                ", tray=" + Arrays.toString(tray) +
                ", fireballs=" + fireballs +
                ", worms=" + Arrays.toString(worms) +
                ", letters=" + Arrays.toString(letters) +
                ", gameID=" + gameID +
                ", isActive=" + isActive +
                '}';
    }
}
