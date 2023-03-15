package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.piece.position.PiecePosition;

public class End extends InitializedGameState {

    protected End(final ChessBoard chessBoard) {
        super(chessBoard);
    }

    @Override
    public GameState movePiece(final PiecePosition source, final PiecePosition destination) {
        throw new IllegalArgumentException("게임이 끝났습니다");
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public GameState toEnd() {
        throw new IllegalStateException("이미 끝났습니다");
    }
}
