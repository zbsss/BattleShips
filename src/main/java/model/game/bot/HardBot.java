package model.game.bot;

import model.game.AbstractPlayer;
import model.game.Position;
import model.statuses.CellStatus;

import java.util.*;

/**
 * First picks at random, than after a hit it tries in the vicinity
 */
public class HardBot extends AbstractPlayer {
    private final Random random = new Random();

    private Position firstHit;
    private final Map<Direction, Boolean> triedDirections = new HashMap<>();
    private Direction currentDirection;
    private Position lastMove;
    private Boolean shipOrientationFound;


    public HardBot(int boardSize) {
        super(boardSize);

        clearMoveHistory();
    }

    /**
     * make a random move
     */
    private Position randomMove(){
        Position position;

        do{
            position = new Position(random.nextInt(getBoardSize()),
                                    random.nextInt(getBoardSize()));
        }while(!getEnemyBoard().cellCanBeHit(position));

        lastMove = position;

        return position;
    }

    /**
     * clears the history after a ship has sunk
     */
    private void clearMoveHistory(){
        firstHit = lastMove = null;
        shipOrientationFound = false;
        currentDirection = null;
        for(Direction direction : Direction.values()){
            triedDirections.put(direction, false);
        }
    }

    /**
     *  First picks a move at random.
     *  After a hit it pick the nearby positions same way that a human would
     */
    @Override
    public Position makeTurn() {
        // First move in the game
        if (lastMove == null){
            return randomMove();
        }

        // Read the result of the previous move
        CellStatus status = getEnemyBoard().getCellStatus(lastMove);

        // If the previous move destroyed a ship, pick next move at random
        if (status == CellStatus.DESTROYED) {
            clearMoveHistory();
            return randomMove();
        }

        // If the random move missed the try again
        if(firstHit == null && status == CellStatus.MISS){
            clearMoveHistory();
            return randomMove();
        }

        /*
         If the random move hit a ship, then:
         - if it was the first hit remember it as firstHit
         - otherwise this was nex hit on the same ship and now we know the ships orientation (vertical or horizontal)
         */
        if(status == CellStatus.HIT){
            if(firstHit == null)
                firstHit = lastMove;
            else
                shipOrientationFound = true;
        }

        /*
        If we don't know the ships orientation we need to check all possible directions
         */
        if(!shipOrientationFound) {
            Position next = null;
            for (Direction direction : Direction.values()) {
                next = firstHit.add(direction.toVector());
                currentDirection = direction;
                if (!triedDirections.get(direction) && getEnemyBoard().isValidPosition(next))
                    break;
            }

            triedDirections.put(currentDirection, true);
            lastMove = next;
        }

        /*
        If we know the orientation of then next moves we make are
        on the same:
         - row if the ship was horizontal
         - column if the ship was vertical
         If we make it to the end of the ship and it still hasn't sunk
         go back to the first hit and move in the opposite direction
         */
        else{
            if(status == CellStatus.HIT){
                lastMove = lastMove.add(currentDirection.toVector());
            }

            if (status == CellStatus.MISS || !getEnemyBoard().isValidPosition(lastMove)){
                currentDirection = currentDirection.opposite();
                lastMove = firstHit.add(currentDirection.toVector());
            }
        }


        return lastMove;
    }
}
