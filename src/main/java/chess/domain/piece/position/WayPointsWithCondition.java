package chess.domain.piece.position;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Piece;

import java.util.Collections;
import java.util.List;

public class WayPointsWithCondition {

    private final List<PiecePosition> wayPoints;
    private final Condition condition;

    private WayPointsWithCondition(final List<PiecePosition> wayPoints, final Condition condition) {
        this.wayPoints = wayPoints;
        this.condition = condition;
    }

    public static WayPointsWithCondition impossible() {
        return new WayPointsWithCondition(Collections.emptyList(), Condition.IMPOSSIBLE);
    }

    public static WayPointsWithCondition possible(final List<PiecePosition> wayPoints) {
        return new WayPointsWithCondition(wayPoints, Condition.POSSIBLE);
    }

    public static WayPointsWithCondition onlyEnemy() {
        return new WayPointsWithCondition(Collections.emptyList(), Condition.ONLY_DESTINATION_ENEMY);
    }

    public boolean satisfy(final ChessBoard chessBoard, final Piece piece, final PiecePosition destination) {
        if (condition.impossible()) {
            return false;
        }
        if (blocking(chessBoard)) {
            return false;
        }
        if (condition.onlyDestinationEnemy() && chessBoard.findByPosition(destination).map(piece::isAlly).orElse(false)) {
            return false;
        }
        return true;
    }

    private boolean blocking(final ChessBoard chessBoard) {
        return wayPoints.stream()
                .anyMatch(chessBoard::existByPosition);
    }

    public List<PiecePosition> wayPoints() {
        return wayPoints;
    }

    public Condition condition() {
        return condition;
    }
}
