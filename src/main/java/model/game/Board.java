package model.game;

import javafx.geometry.Pos;
import model.statuses.CellStatus;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class Board {
    private final Cell[][] board;

    public Board(int size){
        board = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell(new Position(i, j));
            }
        }
    }


    /**
     *  calls hit on an appropriate cell
     * @param position position on the board to be hit
     *
     */
    public CellStatus hit(Position position){
        return getCell(position).hit();
    }

    /**
     * place the ship on the board
     * @param positions list of positions where to place the ship
     * @param ship ship that's supposed to be places on positions
     */
    public void place(Collection<Position> positions, Ship ship){
        ship.setCells(positions.stream().map(this::getCell).collect(Collectors.toList()));
    }

    /**
     * @param position position of the cell
     * @return cell at position
     */
    public Cell getCell(Position position){
        return board[position.getX()][position.getY()];
    }

    public Cell[][] getCells() {
        return board;
    }

    public CellStatus getCellStatus(Position position){
        return getCell(position).getStatus();
    }

    public int getBoardSize(){
        return board.length;
    }

    public boolean cellFree(Position position){
        return getCell(position).getStatus() == CellStatus.WATER;
    }

    public boolean cellCanBeHit(Position position){
        CellStatus status = getCellStatus(position);
        return status == CellStatus.WATER || status == CellStatus.SHIP;
    }

    public boolean isValidPosition(Position position){
        return position.getX() >=0 && position.getX() < board.length && position.getY() >=0 && position.getY() < board.length;
    }
}
