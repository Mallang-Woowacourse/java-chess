package chess.domain.piece.strategy.pawn;

import chess.domain.piece.position.Path;

public class WhitePawnMoveStrategy extends PawnMoveStrategy {

    @Override
    protected void validAdditionalConstraint(final Path path) {
        if (!path.isDestinationRelativelyNorth()) {
            throw new IllegalArgumentException("흰 폰은 북쪽을 향해서만 이동할 수 있습니다.");
        }
    }
}
