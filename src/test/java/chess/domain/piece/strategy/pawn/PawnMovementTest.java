package chess.domain.piece.strategy.pawn;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Path;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.strategy.RookMovementStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("PawnMovementStrategy 은")
class PawnMovementTest {

    private final PawnMovementStrategy pawnMovement = new PawnMovementStrategy();

    public Piece piece(final Color color, final PiecePosition piecePosition) {
        return new Piece(color, piecePosition, new PawnMovementStrategy());
    }

    @Test
    void 이동할_수_있으면_경유지_조회_가능() {
        // given
        final Path path = Path.of(PiecePosition.of("d2"), PiecePosition.of("d4"));
        assertDoesNotThrow(() -> pawnMovement.validateMove(Color.WHITE, path, null));

        // when
        final List<PiecePosition> waypoints = pawnMovement.waypoints(Color.WHITE, path, null);

        // then
        assertThat(waypoints).isNotEmpty();
    }

    @Test
    void 이동할_수_없으면_경유지_조회_불가() {
        // given
        final PiecePosition dest = PiecePosition.of("d4");
        final Path path = Path.of(PiecePosition.of("d2"), dest);
        final Piece enemy = piece(Color.BLACK, dest);
        assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, enemy));

        // when & then
        assertThatThrownBy(() -> pawnMovement.waypoints(Color.WHITE, path, enemy))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 도착지에_적이_있다면 {

        @Test
        void 대각선_한_칸_이동_가능() {
            // given
            final PiecePosition dest = PiecePosition.of("c3");
            final Path path = Path.of(PiecePosition.of("d2"), dest);
            final Piece piece = piece(Color.WHITE, dest);

            // when & then
            assertDoesNotThrow(() -> pawnMovement.validateMove(Color.BLACK, path, piece));
        }

        @Test
        void 대각선_두칸_이상_이동_불가() {
            // given
            final PiecePosition dest = PiecePosition.of("f4");
            final Path path = Path.of(PiecePosition.of("d2"), dest);
            final Piece piece = piece(Color.BLACK, dest);

            // when & then
            assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, piece))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 수직_이동_불가() {
            // given
            final PiecePosition dest = PiecePosition.of("d3");
            final Path path = Path.of(PiecePosition.of("d2"), dest);
            final Piece piece = piece(Color.BLACK, dest);

            // when & then
            assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, piece))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 수평_이동_불가() {
            // given
            final PiecePosition dest = PiecePosition.of("c2");
            final Path path = Path.of(PiecePosition.of("d2"), dest);

            // when & then
            assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 도착지에_피스가_없다면 {

        @Test
        void 수직_한_칸_이동_가능() {
            // given
            final PiecePosition dest = PiecePosition.of("d3");
            final Path path = Path.of(PiecePosition.of("d2"), dest);

            // when & then
            assertDoesNotThrow(() -> pawnMovement.validateMove(Color.WHITE, path, null));
        }

        @Test
        void 수직_두칸_이동_가능() {
            // given
            final PiecePosition dest = PiecePosition.of("d1");
            final Path path = Path.of(PiecePosition.of("d3"), dest);

            // when & then
            assertDoesNotThrow(() -> pawnMovement.validateMove(Color.WHITE, path, null));
        }

        @Test
        void 대각선_이동_불가() {
            // given
            final PiecePosition dest = PiecePosition.of("e3");
            final Path path = Path.of(PiecePosition.of("d2"), dest);
            final PawnMovementStrategy pawnMovement = new PawnMovementStrategy();

            // when & then
            assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 수평_이동_불가() {
            // given
            final PiecePosition dest = PiecePosition.of("c2");
            final Path path = Path.of(PiecePosition.of("d2"), dest);
            final PawnMovementStrategy pawnMovement = new PawnMovementStrategy();

            // when & then
            assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 추가_제약조건을_지키지_않았다면_예외() {
        // given
        final PawnMovementStrategy pawnMovement = new PawnMovementStrategy(path -> {
            throw new IllegalArgumentException();
        });
        final PiecePosition dest = PiecePosition.of("d3");
        final Path path = Path.of(PiecePosition.of("d2"), dest);

        // when & then
        assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 추가_제약조건을_지켰다면_기본_이동에_대해서는_가능() {
        // given
        final PawnMovementStrategy pawnMovement = new PawnMovementStrategy(path -> {
        });
        final PiecePosition dest = PiecePosition.of("d3");
        final Path path = Path.of(PiecePosition.of("d2"), dest);

        // when & then
        assertDoesNotThrow(() -> pawnMovement.validateMove(Color.WHITE, path, null));
    }

    @Test
    void 추가_제약조건을_지켰더라도_기본_이동_수칙을_지키지_않으면_예외() {
        // given
        final PawnMovementStrategy pawnMovement = new PawnMovementStrategy(path -> {
            throw new IllegalArgumentException();
        });
        final PiecePosition dest = PiecePosition.of("d5");
        final Path path = Path.of(PiecePosition.of("d2"), dest);

        // when & then
        assertThatThrownBy(() -> pawnMovement.validateMove(Color.WHITE, path, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 아군을_죽일_수_없다() {
        // given
        final PiecePosition source = PiecePosition.of("d2");
        final PiecePosition dest = PiecePosition.of("c3");
        final Path path = Path.of(source, dest);
        final Piece ally = new Piece(Color.BLACK, dest, new RookMovementStrategy());

        // when & then
        assertThatThrownBy(() -> pawnMovement.validateMove(Color.BLACK, path, ally))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 적군을_죽일_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of("d2");
        final PiecePosition dest = PiecePosition.of("c3");
        final Path path = Path.of(source, dest);
        final Piece ally = new Piece(Color.BLACK, dest, new RookMovementStrategy());

        // when & then
        assertDoesNotThrow(() -> pawnMovement.validateMove(Color.WHITE, path, ally));
    }
}
