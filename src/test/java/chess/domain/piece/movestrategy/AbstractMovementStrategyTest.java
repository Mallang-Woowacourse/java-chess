package chess.domain.piece.movestrategy;

import chess.domain.piece.MovementType;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("AbstractMoveStrategy 은")
class AbstractMovementStrategyTest {

    @Test
    void 올바른_경로가_아니면_예외가_발생한다() {
        // given
        final PieceMovementStrategy strategy = new DefaultAbstractPieceMovementStrategy(MovementType.BLACK_PAWN) {

            @Override
            protected void validateMoveWithNoAlly(final PiecePosition source,
                                                  final PiecePosition destination,
                                                  final Piece nullableEnemy) {
                throw new IllegalArgumentException();
            }
        };

        // when & then
        assertThatThrownBy(() ->
                strategy.waypoints(PiecePosition.of("d2"), PiecePosition.of("d4"), null)
        );
    }

    @Test
    void 올바른_경로라면_경유지를_반환한다() {
        // given
        final PieceMovementStrategy strategy = new DefaultAbstractPieceMovementStrategy(MovementType.BLACK_PAWN) {

            @Override
            protected void validateMoveWithNoAlly(final PiecePosition source,
                                                  final PiecePosition destination,
                                                  final Piece nullableEnemy) {
            }
        };

        // when
        final List<PiecePosition> waypoints = strategy.waypoints(PiecePosition.of("d2"), PiecePosition.of("d4"), null);

        // then
        assertThat(waypoints).containsOnly(PiecePosition.of("d3"));
    }
}
