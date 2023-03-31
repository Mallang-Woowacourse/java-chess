package chess.domain.piece.movestrategy;

import chess.domain.piece.MovementType;
import chess.domain.piece.position.PiecePosition;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultAbstractPieceMovementStrategy extends AbstractPieceMovementStrategy {

    protected DefaultAbstractPieceMovementStrategy(final MovementType type) {
        super(type);
    }

    @Override
    protected List<PiecePosition> waypointsAsType(final PiecePosition source,
                                                  final PiecePosition destination) {
        final List<PiecePosition> waypoints = new ArrayList<>();
        PiecePosition current = source;
        while (!current.equals(destination)) {
            current = current.move(current.direction(destination));
            waypoints.add(current);
        }
        waypoints.remove(destination);
        return waypoints;
    }
}
