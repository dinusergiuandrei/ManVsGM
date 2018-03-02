package Parser;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDatabase {

    private List<GameDetails> allGames = new LinkedList<>();

    private Map<String, List<GameDetails>> playerToGamesMap = new LinkedHashMap<>();

    GameDatabase() {
    }

    public void addPlayerToGamesPair(String user, List<GameDetails> gamesDetails){
        if(this.playerToGamesMap.containsKey(user)){
            this.playerToGamesMap.get(user).addAll(gamesDetails);
        }
        else this.playerToGamesMap.put(user, gamesDetails);
    }

    public void computeAllGamesList(){
        this.playerToGamesMap.forEach(
                (player, gamesList) -> this.allGames.addAll(gamesList)
        );
    }

    public Integer getGamesCount(){
        return this.allGames.size();
    }
}
