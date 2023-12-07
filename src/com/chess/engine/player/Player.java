package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.*;

import java.util.*;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legaMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Move> legaMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing=establishedKing();
        List<Move> combinedMoves = new ArrayList<>(legaMoves);
        combinedMoves.addAll(calculateKingCastles(legaMoves, opponentMoves));
        this.legaMoves = Collections.unmodifiableList(combinedMoves);
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
    }
    public King getPlayerKing(){
        return this.playerKing;
    }
    public Collection<Move> getLegalMoves(){
        return this.legaMoves;
    }
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>(0);
        for(final Move move : moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    private King establishedKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException(("Not a Valid Board!!"));
    }

    public boolean isMoveLegal(final Move move){
        return this.legaMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }
    //need to implement these methods
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    private boolean hasEscapeMoves() {
        for(final Move move : this.legaMoves){
            final MoveTransaction transaction = makeMove(move);
            if(transaction.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }


    public boolean isCastled(){
        return false;
    }
    public MoveTransaction makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveTransaction(this.board,move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
            transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransaction(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransaction(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}

