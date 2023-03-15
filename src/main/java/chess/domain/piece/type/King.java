package chess.domain.piece.type;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Direction;
import chess.domain.piece.position.Path;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.WayPointsWithCondition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class King extends Piece {

    public King(final Color color, final PiecePosition piecePosition) {
        super(color, piecePosition);
    }

    @Override
    protected WayPointsWithCondition wayPointsWithCondition(final Path path) {
        if (path.isUnitDistance()) {
            return WayPointsWithCondition.possible(Collections.emptyList());
        }
        return WayPointsWithCondition.impossible();
    }

    public List<PiecePosition> movablePaths() {
        return Arrays.stream(Direction.values())
                .filter(it -> piecePosition.movable(it))
                .map(it -> piecePosition.move(it))
                .collect(Collectors.toList());
    }
}
