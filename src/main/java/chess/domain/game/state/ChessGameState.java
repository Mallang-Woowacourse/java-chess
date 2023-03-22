package chess.domain.game.state;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;

import java.util.List;
import java.util.Map;

public interface ChessGameState {

    boolean playable();

    ChessGameState initialize();

    ChessGameState movePiece(final PiecePosition source, final PiecePosition destination);

    ChessGameState end();

    List<Piece> pieces();

    Color winColor();

    Map<Color, Double> calculateScore();
}