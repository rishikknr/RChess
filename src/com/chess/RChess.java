package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

import java.sql.SQLOutput;

public class RChess {
    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.println(board);

        Table table = new Table();
    }
}
