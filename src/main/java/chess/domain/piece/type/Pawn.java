package chess.domain.piece.type;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Path;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.WayPoints;

import java.util.Collections;
import java.util.List;

public class Pawn extends Piece {

    private boolean isMoved;

    public Pawn(final Color color, final PiecePosition piecePosition) {
        super(color, piecePosition);
        this.isMoved = false;
    }

    @Override
    protected void validateMovable(final Path path) {
        if (!matchDestinationByColor(path)) {
            throw new IllegalArgumentException();
        }
        if (!isMoved && isPawnSpecialDestination(path)) {
            return;
        }
        if (!path.isUnitDistance()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean matchDestinationByColor(final Path path) {
        if (color == Color.BLACK) {
            return path.isDestinationRelativelySouth();
        }
        return path.isDestinationRelativelyNorth();
    }

    private boolean isPawnSpecialDestination(final Path path) {
        if (Math.abs(path.rankDistance()) != 2) {
            return false;
        }
        return Math.abs(path.fileDistance()) == 0;
    }

    @Override
    protected WayPoints wayPointsWithCondition(final Path path) {
        if (!isMoved && isPawnSpecialDestination(path)) {
            final List<PiecePosition> wayPoints = path.wayPoints();
            wayPoints.add(path.destination());
            return WayPoints.from(wayPoints);
        }
        return defaultMove(path);
    }

    private WayPoints defaultMove(final Path path) {
        if (path.isDiagonal()) {
            return WayPoints.from(Collections.emptyList());
        }
        return WayPoints.from(List.of(path.destination()));
    }

    @Override
    public void move(final PiecePosition piecePosition) {
        if (!Path.of(this.piecePosition, piecePosition).isStraight()) {
            throw new IllegalArgumentException("폰은 적이 없는 경우 직선으로만 이동할 수 있습니다.");
        }
        this.piecePosition = piecePosition;
        this.isMoved = true;
    }

    @Override
    public void moveAndKill(final Piece enemy) {
        if (!Path.of(piecePosition, enemy.piecePosition()).isDiagonal()) {
            throw new IllegalArgumentException("폰은 대각선 위치에 있는 적만 죽일 수 있습니다.");
        }
        this.piecePosition = enemy.piecePosition();
        this.isMoved = true;
    }
}
