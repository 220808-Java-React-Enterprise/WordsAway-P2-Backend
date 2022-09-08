# Intellijents-P2-Backend
## Proposal
A combination of battleship and scrabble.  Uses a dictionary API to check for valid words.  1 vs 1 battleship style game that uses a scrabble board for each player. Players take turns using their tiles to make words and place them on the board. If their word overlaps an opponents battleship, it is considered hit.

## Overview

### Features
 - Players can register with a username, email, and password.
 - Players will be able to start a game with a CPU.
 - Each player has a “tray” of 7 letters that they can make words from.
 - Each time a letter is played, a new letter is drawn to replace it.
 - A player can skip their turn to shuffle their letters back in and receive 7 new letters.
 - Once all the opponent’s ships have been sunk, the remaining player is the winner.
 - Letters will have a rarity value 1-3. 1's only hit their spot when placed. 2's hit their spot and adjacent spots. 3's make the whole word hit adjacent spots.
### MVP
 - User signup
 - A game can be instantiated against a CPU when the Player is ready.
 - During Game...
   - Player can chose the location of their ships at the start of game.
   - Player can attempt move using letters from tray.
   - Server will check if move is legal. 
     - If so, log move, update game state, update player tray. 
     - If not, prompt player to try again.
   - Game can be marked as completed if all ships for a player destroyed or if player forfeits. 
### Stretch Goals
 - Multiplayer functionality (Game initialized when two players are logged in and "ready").
 - Player ELO will be calculated after game conclusion.
 - Difficulty options.
 - Emote communication 😀😠😅😭💀🤷💅
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
