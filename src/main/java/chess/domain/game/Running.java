package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.type.King;

public class Running extends InitializedGameState {

    private final Turn turn;

    public Running(final ChessBoard chessBoard, final Turn turn) {
        super(chessBoard);
        this.turn = turn;
    }

    @Override
    public GameState movePiece(final PiecePosition source, final PiecePosition destination) {
        chessBoard.movePiece(turn, source, destination);

        if (chessBoard.checkmatedBy(turn.change())) {
            chessBoard.restore();
            throw new IllegalArgumentException("해당 위치는 체크메이트되기 때문에 이동할 수 없습니다.");
        }

        return judgeNext(destination);
    }

    private GameState judgeNext(final PiecePosition destination) {
        if (chessBoard.kingDie()) {
            return new End(chessBoard);
        }
        if (chessBoard.checkmatedBy(turn)) {
            final King king = chessBoard.findKing(turn.change().color());

            if (isKingAvoidKill(destination, king)) {
                return new Checkmate(chessBoard, turn.change());
            }
            return new End(chessBoard);
        }
        return new Running(chessBoard, turn.change());
    }

    private boolean isKingAvoidKill(final PiecePosition destination, final King king) {
        for (final PiecePosition position : king.movablePaths()) {
            try {
                chessBoard.movePiece(turn, position, destination);
                if (!chessBoard.checkmatedBy(turn.change())) {
                    chessBoard.restore();
                    return true;
                }

                chessBoard.restore();
            } catch (Exception e) {
            }
        }
        return false;
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
