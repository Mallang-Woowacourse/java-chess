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

        if (!king.existIn(source)) {
            throw new IllegalArgumentException("체크메이트 상태에서는 왕을 움직여야 합니다.");
        }
        chessBoard.movePiece(turn, source, destination);
        return judgeNext();
    }

    private InitializedGameState judgeNext() {
        if (chessBoard.kingDie()) {
            return new End(chessBoard);
        }
        if (chessBoard.checkmatedBy(turn.change())) {
            chessBoard.restore();
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
