package chess.domain.piece.strategy;

import chess.domain.piece.MoveStrategy;
import chess.domain.piece.position.Path;
import chess.domain.piece.position.PiecePosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static chess.domain.piece.position.PiecePositionFixture.piecePositions;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("BishopMoveStrategy 은")
class BishopMoveStrategyTest {

    private final MoveStrategy strategy = new BishopMoveStrategy();
    private final PiecePosition source = PiecePosition.of("e4");

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 대각선_경로로_움직이는_경우 {

        @ParameterizedTest(name = "움직일 수 있다. [e4] -> [{1}]")
        @CsvSource({
                "d3",
                "d5",
                "f3",
                "f5",
                "c2",
                "c6",
                "g2",
                "g6",
        })
        void 움직일_수_있다(final PiecePosition destination) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThat(strategy.movable(path)).isTrue();
        }

        @ParameterizedTest(name = "경유지를 반환한다. 출발: [e4] -> 경유지: [{1}] -> 도착: [{0}]")
        @MethodSource("bishopDestinations")
        void 경유지를_반환한다(final PiecePosition destination, final List<PiecePosition> waypoints) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThat(strategy.waypoints(path)).containsExactlyInAnyOrderElementsOf(waypoints);
        }

        Stream<Arguments> bishopDestinations() {
            return Stream.of(
                    Arguments.of("f5", Named.of("없다", emptyList())),
                    Arguments.of("f3", Named.of("없다", emptyList())),
                    Arguments.of("d5", Named.of("없다", emptyList())),
                    Arguments.of("d3", Named.of("없다", emptyList())),
                    Arguments.of("g6", Named.of("f5", piecePositions("f5"))),
                    Arguments.of("g2", Named.of("f3", piecePositions("f3"))),
                    Arguments.of("c6", Named.of("d5", piecePositions("d5"))),
                    Arguments.of("c2", Named.of("d3", piecePositions("d3")))
            );
        }
    }

    @Nested
    class 대각선이_아닌_경로로_움직이는_경우 {

        @ParameterizedTest(name = "움직일 수 없다. [e4] -> [{1}]")
        @CsvSource({
                "e5",
                "e3",
                "d4",
                "f4",
        })
        void 움직일_수_없다(final PiecePosition destination) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThat(strategy.movable(path)).isFalse();
        }

        @ParameterizedTest(name = "경유지를 조회하면 예외. [e4] -> [{1}]")
        @CsvSource({
                "e5",
                "e3",
                "d4",
                "f4",
        })
        void 경유지를_조회하면_예외(final PiecePosition destination) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThatThrownBy(() -> strategy.waypoints(path))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}