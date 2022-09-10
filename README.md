# WordsAway-P2-Backend

<a href="https://github.com/220808-Java-React-Enterprise/WordsAway-P2-Frontend">Frontend</a>

## Team Members
- Chris Waters (Lead)
- Nathan Gilbert (Frontend)
- Nicholas Fielder (Frontend)
- Robert James (Backend)

## Proposal
Words Away is a web based game combining the mechanics of Battleship and Scrabble. This game will use a anagram API for checking words and determining possible words for computer players. Players will set up a game by laying out their ships on a grid. On their turn a player can place either a word or a bomb. All instances of 2 or more letters in a grid must be valid words. A player receives a bomb for each new cross word that they make. The first player to place letters in every ship cell of their oppenent's grid wins.
## Overview

### Features
 - Players can register with a username, email, and password.
 - Players will be able to start a game with a CPU.
 - Each player has a “tray” of 7 letters that they can make words from.
 - Each time a letter is played, a new letter is drawn to replace it.
 - A player can skip their turn to shuffle their letters back in and receive 7 new letters.
 - A player can play a bomb if they have one instead of a word.
 - Once all the opponent’s ships have been sunk, the remaining player is the winner.
 - Letters will have a rarity value 1-3. 1's only hit their spot when placed. 2's hit their spot and adjacent spots. 3's make the whole word hit adjacent spots.
### MVP
 - User signup
 - A game can be instantiated against a CPU when the Player is ready.
 - During Game...
   - Player can chose the location of their ships at the start of game.
   - Player can attempt a move using letters from tray.
   - Server will check if move is legal. 
     - If so, log move, update game state, update player tray. 
     - If not, prompt player to try again.
   - Game can be marked as completed if all ships for a player are destroyed or if player forfeits.
### Stretch Goals
 - Multiplayer functionality (Game initialized when two players are logged in and "ready").
 - Player ELO will be calculated after game conclusion.
 - Difficulty options.
 - Emote communication 😀😠😅😭💀🤷💅
 - Profile Viewing
### API Functionality
 - API will reference external API to check player move validity.
### Tech Stack
 - Java 8
 - React
 - PostgreSQL
 - JUnit
 - Mockito
 - Spring Boot
 - AWS Elastic Beanstalk
