package Parser;

import ChessLogic.DataSet;
import ChessLogic.DataSetEntry;
import ChessLogic.GameDetails;
import GameArchitecture.Move;
import GameArchitecture.Player;

import javax.xml.crypto.Data;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
                (player, gamesList) -> {
                    this.allGames.addAll(gamesList);
                }
        );

    }

    public Integer computeTotalMoveCount(){
        AtomicInteger gameCount = new AtomicInteger();
        this.playerToGamesMap.forEach(
                (player, gamesList) -> {
                    gamesList.forEach(
                            gameDetails -> {
                                gameCount.addAndGet(gameDetails.whiteMovesString.size());
                                gameCount.addAndGet(gameDetails.blackMovesString.size());
                            }
                    );
                }
        );

        return gameCount.get();
    }

    public DataSet computePositionToMoveMap(){
        DataSet dataSet = new DataSet();

        this.allGames.forEach(
                gameDetails -> {
                   for(int i=0; i<gameDetails.whiteMoves.size(); ++i){
                       dataSet.data.add(new DataSetEntry(gameDetails.game.getPositions().get(2*i), gameDetails.whiteMoves.get(i)));
                   }
                   for(int j=0; j<gameDetails.blackMoves.size(); ++j){
                       dataSet.data.add(new DataSetEntry(gameDetails.game.getPositions().get(2*j+1), gameDetails.blackMoves.get(j)));
                   }
                }
        );
        return dataSet;
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
