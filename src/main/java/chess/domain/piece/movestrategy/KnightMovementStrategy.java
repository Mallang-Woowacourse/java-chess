package chess.domain.piece.movestrategy;

import chess.domain.piece.MovementType;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;

public class KnightMovementStrategy extends JumbleAbstractPieceMovementStrategy {

    public KnightMovementStrategy() {
        super(MovementType.KNIGHT);
    }

    @Override
    protected void validateMoveWithNoAlly(final PiecePosition source, final PiecePosition destination, final Piece nullableEnemy) {
        if (Math.abs(source.fileInterval(destination)) == 1
                && Math.abs(source.rankInterval(destination)) == 2) {
            return;
        }
        if (Math.abs(source.fileInterval(destination)) == 2
                && Math.abs(source.rankInterval(destination)) == 1) {
            return;
        }
        throw new IllegalArgumentException("나이트는 그렇게 이동할 수 없습니다.");
    }
}
