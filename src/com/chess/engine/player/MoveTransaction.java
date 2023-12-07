package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransaction {

    private final Board transactionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransaction(final Board transactionBoard, final Move move, final MoveStatus moveStatus) {
        this.transactionBoard = transactionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }
    public Board getTransactionBoard(){
        return this.transactionBoard;
    }

}
