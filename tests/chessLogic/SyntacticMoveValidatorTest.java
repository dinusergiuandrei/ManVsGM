package chessLogic;

import gameArchitecture.Move;
import gameArchitecture.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class SyntacticMoveValidatorTest {
    Integer tablesCount = 100;
    List<Table> tables = new LinkedList<>();
    List<Move> moves = new LinkedList<>();
    Boolean[] expectedResults = new Boolean[tablesCount];

    @Before
    public void setUp(){
        for(int i=0; i<tablesCount; ++i){

        }
    }

    @Test
    public void isValidMoveSyntacticallyTest(){

    }
}
