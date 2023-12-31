package chess.domain.piece.movestrategy;

import chess.domain.piece.MovementType;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;

public class KingMovementStrategyDefault extends DefaultAbstractPieceMovementStrategy {

    public KingMovementStrategyDefault() {
        super(MovementType.KING);
    }

    @Override
    protected void validateMoveWithNoAlly(final PiecePosition source,
                                          final PiecePosition destination,
                                          final Piece nullableEnemy) {
        if (!isUnitDistance(source, destination)) {
            throw new IllegalArgumentException("왕은 한칸만 이동할 수 있습니다.");
        }
    }
}
