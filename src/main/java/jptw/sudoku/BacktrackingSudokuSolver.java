//////////////////////////////////////////////////////////////////////// Package
package jptw.sudoku;

//////////////////////////////////////////////////////////////////////// Imports
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

/////////////////////////////////////////////////////////////// Class definition
class BacktrackingSudokuSolver
        implements SudokuSolver {
    ////////////////////////////////////////////////////////////////// [Methods]
    //----------------------------------------------------- Main functionality <
    @Override
    public boolean solve(final SudokuBoard board) {

        Pair<Integer, Integer> emptyFieldCoordinates
                = findEmptyField(board);

        if (emptyFieldCoordinates == null) {
            if (board.equals(previousBoard)) {
                return false;
            }
            previousBoard = new SudokuBoard(board);
            return true;
        }

        int i = emptyFieldCoordinates.getKey();
        int j = emptyFieldCoordinates.getValue();

        ArrayList<Integer> randomSudokuNumbers = generateRandomSudokuNumbers();

        for (int randomSudokuNumber : randomSudokuNumbers) {
            if (board.set(i, j, randomSudokuNumber)) {
                if (solve(board)) {
                    return true;
                }
            }
        }

        board.set(i, j, 0);

        return false;
    }

    //------------------------------------------------------ Helper functions <<
    private Pair<Integer, Integer> findEmptyField(final SudokuBoard board) {

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(i, j) == 0) {
                    return new Pair<>(i, j);
                }
            }
        }

        return null;
    }

    private ArrayList<Integer> generateRandomSudokuNumbers() {

        ArrayList<Integer> randomSudokuNumbers = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            randomSudokuNumbers.add(i + 1);
        }

        Collections.shuffle(randomSudokuNumbers);

        return randomSudokuNumbers;
    }

    /////////////////////////////////////////////////////////////////// [Fields]
    private static final int BOARD_SIZE = 9;
    private static SudokuBoard previousBoard = new SudokuBoard();

}

////////////////////////////////////////////////////////////////////////////////