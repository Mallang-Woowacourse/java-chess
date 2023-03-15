package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.position.PiecePosition;

public interface GameState {

    GameState movePiece(final PiecePosition source, final PiecePosition destination);

    boolean isEnd();

    GameState toEnd();

    GameState initialize(final ChessBoard chessBoard, final Color first);
}
