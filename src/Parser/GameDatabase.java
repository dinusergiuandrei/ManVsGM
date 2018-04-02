package Parser;

import ChessLogic.GameDetails;
import GameArchitecture.Player;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDatabase {

    private List<GameDetails> allGames = new LinkedList<>();

    private Map<String, List<GameDetails>> playerToGamesMap = new LinkedHashMap<>();

    private List<Player> players = new LinkedList<>();


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
        if(this.playerToGamesMap == null || this.playerToGamesMap.size() == 0)
            computeAllGamesList();
        return this.allGames.size();
    }

    public Player addPlayer(String familyName, String givenName, Boolean isAI){
        int id=0;
        for (Player player : this.players) {
            if(player.getId()>id)
                id = player.getId();
        }
        id++;
        Player newPlayer = new Player(id, familyName, givenName, null);
        this.players.add(newPlayer);
        return newPlayer;
    }

    public List<GameDetails> getAllGames() {
        return allGames;
    }

}
