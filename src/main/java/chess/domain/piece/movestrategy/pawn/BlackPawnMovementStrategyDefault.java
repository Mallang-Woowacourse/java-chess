package chess.domain.piece.movestrategy.pawn;

import chess.domain.piece.MovementType;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;

public class BlackPawnMovementStrategyDefault extends PawnMovementStrategyDefault {

    private static final Rank DEFAULT_PERMIT_TWO_RANK = Rank.from(7);

    public BlackPawnMovementStrategyDefault() {
        super(MovementType.BLACK_PAWN, DEFAULT_PERMIT_TWO_RANK);
    }

    public BlackPawnMovementStrategyDefault(final Rank permitTwoMoveRank) {
        super(MovementType.BLACK_PAWN, permitTwoMoveRank);
    }

    @Override
    protected void validateAdditionalConstraint(final PiecePosition source,
                                                final PiecePosition destination) {
        if (!isDestinationRelativelySouth(source, destination)) {
            throw new IllegalArgumentException("검정 폰은 남쪽을 향해서만 이동할 수 있습니다.");
        }
    }
}
