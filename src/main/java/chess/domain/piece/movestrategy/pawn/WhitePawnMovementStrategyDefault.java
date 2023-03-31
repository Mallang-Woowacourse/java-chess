package chess.domain.piece.movestrategy.pawn;

import chess.domain.piece.MovementType;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;

public class WhitePawnMovementStrategyDefault extends PawnMovementStrategyDefault {

    private static final Rank DEFAULT_PERMIT_TWO_RANK = Rank.from(2);

    public WhitePawnMovementStrategyDefault() {
        super(MovementType.WHITE_PAWN, DEFAULT_PERMIT_TWO_RANK);
    }

    public WhitePawnMovementStrategyDefault(final Rank permitTwoMoveRank) {
        super(MovementType.WHITE_PAWN, permitTwoMoveRank);
    }

    @Override
    protected void validateAdditionalConstraint(final PiecePosition source,
                                                final PiecePosition destination) {
        if (!isDestinationRelativelyNorth(source, destination)) {
            throw new IllegalArgumentException("흰 폰은 북쪽을 향해서만 이동할 수 있습니다.");
        }
    }
}
