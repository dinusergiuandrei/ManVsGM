Games source:
http://www.pgnmentor.com/files.html
http://chessproblem.my-free-games.com/chess/games/Download-PGN.php

bibliography:
https://stackoverflow.com/questions/27641774/how-to-download-zip-file-from-url-using-java
https://stackoverflow.com/questions/8253852/how-to-download-a-zip-file-from-a-url-and-store-them-as-zip-file-only
https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
https://stackoverflow.com/questions/6142901/how-to-create-a-file-in-a-directory-in-java
https://stackoverflow.com/questions/15482423/how-to-list-the-files-in-current-directory

The user is able to play chess against A.I. which simulates the strategy of world’s best Grand Masters. 
There is also the possibility of the application the learn the user’s strategy. The project will be extended 
to experiment the result differences between multiple algorithms, in view of chess level.
The main purpose of this project is not to find the best move, but to simulate a chess player’s strategy 
in choosing a move, which can be explained. This project is still in progress.

The algorithms taken into consideration are: 

-   Deterministic Alpha-beta pruning
o	Fixed depth; (depth = 2 => 1-2 seconds/move; depth = 3 => 30 seconds/move)
o	for finding the best move, considering only the positions after depth moves.

-	Non-deterministic Alpha-beta pruning
o	some variations might be omitted, but deeper move search is possible;
o	high probability of finding the best move, not certainty;

-	Classic Genetic Algorithm:
o	Each individual will have a set of preferences over the features in a chess position.

-	Optimized Genetic algorithm:
o	In order to simulate the real chess environment, the users will be separated at multiple levels. 
    (continent, region, country, town, club). For example, the players from two different regions 
    will influence each-other (participate in genetic operations) at every continent-level competition. 
    As the surface is larger (continent>region>...) there will be less competitions of that level.
o	The best players from a certain zone will influence the other players in that zone;
o	Variable level of direct influence;
o	Variable count of zones of a specific level;
o	Variable number of levels. For example, if there are only two levels, 
    the players will be organized only in towns and clubs.
