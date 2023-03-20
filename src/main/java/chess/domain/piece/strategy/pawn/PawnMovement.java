package chess.domain.piece.strategy.pawn;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceMovement;
import chess.domain.piece.position.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PawnMovement implements PieceMovement {

    private final List<PawnMoveConstraint> constraints;

    public PawnMovement(final List<PawnMoveConstraint> constraints) {
        this.constraints = new ArrayList<>(constraints);
    }

    public PawnMovement(final PawnMoveConstraint... constraints) {
        this(Arrays.asList(constraints));
    }

    @Override
    public void validateMove(final Path path, final Piece nullableEnemy) throws IllegalArgumentException {
        validateDefaultMove(path);
        validateKill(path, nullableEnemy);
        validateJustMove(path, nullableEnemy);

        for (final PawnMoveConstraint constraint : constraints) {
            constraint.validateConstraint(path);
        }
    }

    private void validateDefaultMove(final Path path) {
        if (path.isHorizontal()) {
            throw new IllegalArgumentException("폰은 수평으로 움직일 수 없습니다.");
        }
        if (!path.isUnitDistance() && !path.isTwoVerticalMove()) {
            throw new IllegalArgumentException("폰은 그렇게 움직일 수 없습니다.");
        }
    }

    private void validateKill(final Path path, final Piece nullableEnemy) {
        if (nullableEnemy != null && path.isStraight()) {
            throw new IllegalArgumentException("폰은 적이 있는 경우 직선으로 이동할 수 없습니다.");
        }
    }

    private void validateJustMove(final Path path, final Piece nullableEnemy) {
        if (nullableEnemy == null && path.isDiagonal()) {
            throw new IllegalArgumentException("폰은 적이 없는 경우 대각선으로 이동할 수 없습니다.");
        }
    }
}
