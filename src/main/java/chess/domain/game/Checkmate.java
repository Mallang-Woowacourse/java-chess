package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.type.King;

public class Checkmate extends InitializedGameState {

    private final Turn turn;

    protected Checkmate(final ChessBoard chessBoard, final Turn turn) {
        super(chessBoard);
        this.turn = turn;
    }

    @Override
    public GameState movePiece(final PiecePosition source, final PiecePosition destination) {
        final King king = chessBoard.findKing(turn.color());

        if (!isKingAvoidKill(destination, king)) {
            return new End(chessBoard);
        }

        if (!king.existIn(source)) {
            throw new IllegalArgumentException("체크메이트 상태에서는 왕을 움직여야 합니다.");
        }
        chessBoard.movePiece(turn, source, destination);
        return judgeNext(source, destination);
    }

    private boolean isKingAvoidKill(final PiecePosition destination, final King king) {
        for (final PiecePosition position : king.movablePaths()) {
            chessBoard.movePiece(turn, position, destination);

            if (!chessBoard.checkmatedBy(turn.change())) {
                chessBoard.movePiece(turn, destination, position);
                return true;
            }

            chessBoard.movePiece(turn, destination, position);
        }
        return false;
    }

    private InitializedGameState judgeNext(final PiecePosition source, final PiecePosition destination) {
        if (chessBoard.kingDie()) {
            return new End(chessBoard);
        }
        if (chessBoard.checkmatedBy(turn.change())) {
            chessBoard.movePiece(turn, destination, source);  // 원상복구
            throw new IllegalArgumentException("여전히 체크메이트입니다.");
        }
        return new Running(chessBoard, turn.change());
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public GameState toEnd() {
        return new End(chessBoard);
    }
}
