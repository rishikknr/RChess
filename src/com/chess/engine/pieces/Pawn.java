package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnJump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE={8,16,7,9};

    public Pawn(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceAlliance, true);
    }
    public Pawn( final Alliance pieceAlliance,
                 final int piecePosition,
                 final boolean isFirstMove){
        super(PieceType.PAWN,piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves=new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection()*currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                //TODO more work to do here
                legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }else if((currentCandidateOffset == 16 && this.isFirstMove() )&&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition]&&this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite()))){

                final int behindCandidateDestinationCoordinate = this.piecePosition + ( this.pieceAlliance.getDirection() * 8 );
                if(board.getTile(behindCandidateDestinationCoordinate)==null && board.getTile(candidateDestinationCoordinate)==null){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));

                }
            } else if (currentCandidateOffset==7 &&
                    /*!*/((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                        //To do work here
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }

            } else if (currentCandidateOffset==9 &&
                    /*!*/((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())||
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                        //to do work here
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
                
            }

        }

        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
