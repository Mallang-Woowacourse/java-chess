package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.piece.Color;
import chess.domain.piece.position.PiecePosition;

public class Uninitialized implements GameState {
    @Override
    public GameState movePiece(final PiecePosition source, final PiecePosition destination) {
        throw new IllegalStateException("아직 게임이 시작되지 않았습니다.");
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public GameState toEnd() {
        throw new IllegalStateException("초기화하기전 끝낼 수 없습니다");
    }

    @Override
    public GameState initialize(final ChessBoard chessBoard, final Color first) {
        return new Running(chessBoard, new Turn(first));
    }
}
