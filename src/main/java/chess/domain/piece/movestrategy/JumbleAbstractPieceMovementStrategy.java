package chess.domain.piece.movestrategy;

import chess.domain.piece.MovementType;
import chess.domain.piece.position.PiecePosition;

import java.util.Collections;
import java.util.List;

public abstract class JumbleAbstractPieceMovementStrategy extends AbstractPieceMovementStrategy {

    protected JumbleAbstractPieceMovementStrategy(final MovementType type) {
        super(type);
    }

    @Override
    protected List<PiecePosition> waypointsAsType(final PiecePosition source,
                                                  final PiecePosition destination) {
        return Collections.emptyList();
    }
}
