# WordsAway-P2-Backend

<a href="https://github.com/220808-Java-React-Enterprise/WordsAway-P2-Frontend">Frontend</a>

## Team Members
- Chris Waters (Lead)
- Nathan Gilbert (Frontend)
- Nicholas Fielder (Frontend)
- Robert James (Backend)

## Index
- Proposal
- Features
- Rules
- Technologies
- MVP
- Stretch Goals
- API
- ERD
- Contributors

### Proposal
Words Away is a web based game combining the mechanics of Battleship and Scrabble. 
This game will use a anagram API for checking words and determining possible words for computer players. 
Players will start a game by challenging another user or bot. On their turn a player can place either a 
word or a fireball. All instances of 2 or more letters in a grid must be valid words. A player receives a fireball 
for each new cross word that they make. The first player to place letters in every worm cell of their opponent's 
grid wins.

### Features
 - Players can register with a username, email, and password.
 - Players will be able to start a game with a CPU.
 - Players are able to start a game with another user
 - Each player has a “tray” of 7 letters that they can make words from.
 - Each time a letter is played, a new letter is drawn to replace it.
 - A player can skip their turn to shuffle their letters back in and receive 7 new letters.
 - A player can play a fireball if they have one instead of a word.
 - Once all the opponent’s worms have been destroyed, the remaining player is the winner.
 - Letters will have a rarity value.

### Rules

![Rules](src/main/resources/rules.png)

### Game Link
<a href="http://words-away.s3-website.us-east-2.amazonaws.com">Words Away</a>


### Tech Stack
#### Backend
- Java 8
- Spring Boot
- JWT
- Maven
- JUnit
- Mockito

#### Frontend
- HTML/Typescript
- React

#### Hosting
- Anagramica
- PostgreSQL
- AWS Elastic Beanstalk
- AWS S3 Bucket



### MVP
 - User signup
 - A game can be instantiated against a CPU when the player is ready.
 - A game can be instantiated against another user when a player is ready.
 - During Game...
   - Player can attempt a move using letters from tray.
   - Server will check if move is legal. 
     - If so, log move, update game state, update player tray. 
     - If not, prompt player to try again.
   - Game can be marked as completed if all worms for a player are destroyed.


### Stretch Goals
 - User profile/leaderboard
 - AI difficulty options.
 - Allow user to place worms
 - Notify user of destroyed worm
 - Update appearance of webpage
 - Player Communication
 - Notify user of turn
 - Profile Viewing
 - Tutorial
 - Mobile Support


### API Functionality
 - API will reference external API to check player move validity.


### ERD
![ERD](src/main/resources/ERD.png)

### Contributors
 - Chris Waters
   - Backend user Signup/Login
   - Majority of Game functionality
   - API Interaction
   - DB Interaction
   - Deployment to server
   - Unit Testing
 ####
 - Nathan Gilbert
   - Frontend user Signup/Login
   - Frontend password hashing
   - Frontend Asynchronous Updating
 ####
 - Nicholas Fielder
   - Frontend user interaction with game
   - Frontend challenger screen
   - Frontend Styling
 ####
 - Robert James
   - Backend AI
   - Some Game Functionality
   - API Interaction
   - Unit Testing
   - ReadMe
