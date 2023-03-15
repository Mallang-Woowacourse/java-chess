package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;

import java.util.List;

public class ChessGame {

    private GameState state;
    private final ChessBoard chessBoard;
    private final Color first;

    public ChessGame(final ChessBoard chessBoard, final Color first) {
        this.state = new Uninitialized();
        this.chessBoard = chessBoard;
        this.first = first;
    }

    public void initialized() {
        state = state.initialize(chessBoard, first);
    }

    public boolean runnable() {
        return !state.isEnd();
    }

    public void toEnd() {
        state = state.toEnd();
    }

    public void movePiece(final PiecePosition source, final PiecePosition dest) {
        state = state.movePiece(source, dest);
    }

    public List<Piece> pieces() {
        return chessBoard.pieces();
    }
}
