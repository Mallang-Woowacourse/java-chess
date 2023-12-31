package chess.domain.piece.movestrategy;

import chess.domain.piece.Piece;
import chess.domain.piece.MovementType;
import chess.domain.piece.position.PiecePosition;

import java.util.List;

public interface PieceMovementStrategy {

    /**
     * @throws IllegalArgumentException 이동할 수 없는 경로가 들어온 경우
     */
    List<PiecePosition> waypoints(final PiecePosition source,
                                  final PiecePosition destination,
                                  final Piece nullableEnemy);

    /**
     * @throws IllegalArgumentException 이동할 수 없는 경로가 들어온 경우
     */
    void validateMove(final PiecePosition source,
                      final PiecePosition destination,
                      final Piece nullableEnemy);

    double judgeValue();

    MovementType type();

    boolean isSameType(final MovementType type);
}
