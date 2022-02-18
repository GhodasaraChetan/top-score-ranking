# top-score-ranking

**Requirements** : https://gist.github.com/le-doude/4ec0fdf71d1e344e2a3b20e3d1b14a3a

# Endpoints

### POST /scores  
---> with Body ---> to save new scores

### GET /scores/{id}
---> to get score by id from db

### DELETE /scores/{id}
---> to delete the score by id 

### GET /scores?players={players}&beforeZdt={beforeZdt}&afterZdt={afterZdt}&offset={offset}&size={size} 
---> to search and filter scores by playerName, time before/after, with pagination.

### GET /players/{playerName}/scorehistory 
---> to get player's score history

# Test

Integration Test are mentioned in Controller Tests.

Unit Tests are mentioned in Service and Repository Tests.

use `./gradlew test` to run all test cases.