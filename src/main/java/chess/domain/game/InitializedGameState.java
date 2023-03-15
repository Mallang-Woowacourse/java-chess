package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;

public abstract class InitializedGameState implements GameState {

    protected final ChessBoard chessBoard;

    protected InitializedGameState(final ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public GameState initialize(final ChessBoard chessBoard, final Color first) {
        throw new IllegalStateException("이미 초기화됨");
    }
}
