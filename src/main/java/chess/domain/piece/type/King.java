package chess.domain.piece.type;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;

public class King extends Piece {

    public King(final Color color, final PiecePosition piecePosition) {
        super(color, piecePosition);
    }
}