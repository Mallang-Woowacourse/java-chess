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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static chess.domain.piece.position.PiecePositionFixture.piecePositions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("RookMoveStrategy 은")
class RookMoveStrategyTest {

    private final MoveStrategy strategy = new RookMoveStrategy();
    private final PiecePosition source = PiecePosition.of("e4");

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 직선_경로로_움직이는_경우 {

        @ParameterizedTest(name = "움직일 수 있다. [e4] -> [{1}]")
        @CsvSource({
                "f4",
                "d4",
                "e5",
                "e3",
                "g4",
                "c4",
                "e6",
                "e2",
                "h4",
                "a4",
                "e8",
                "e1",
        })
        void 움직일_수_있다(final PiecePosition destination) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThat(strategy.movable(path)).isTrue();
        }

        @ParameterizedTest(name = "경유지를 반환한다. 출발: [e4] -> 경유지: [{1}] -> 도착: [{0}]")
        @MethodSource("rookDestinations")
        void 경유지를_반환한다(final PiecePosition destination, final List<PiecePosition> waypoints) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThat(strategy.waypoints(path)).containsExactlyInAnyOrderElementsOf(waypoints);
        }

        Stream<Arguments> rookDestinations() {
            return Stream.of(
                    Arguments.of("f4", Named.of("없다", Collections.emptyList())),
                    Arguments.of("d4", Named.of("없다", Collections.emptyList())),
                    Arguments.of("e5", Named.of("없다", Collections.emptyList())),
                    Arguments.of("e3", Named.of("없다", Collections.emptyList())),
                    Arguments.of("g4", Named.of("f4", piecePositions("f4"))),
                    Arguments.of("c4", Named.of("d4", piecePositions("d4"))),
                    Arguments.of("e6", Named.of("e5", piecePositions("e5"))),
                    Arguments.of("e2", Named.of("e3", piecePositions("e3"))),
                    Arguments.of("h4", Named.of("f4, g4", piecePositions("f4", "g4"))),
                    Arguments.of("a4", Named.of("d4, c4, b4", piecePositions("d4", "c4", "b4"))),
                    Arguments.of("e8", Named.of("e5, e6, e7", piecePositions("e5", "e6", "e7"))),
                    Arguments.of("e1", Named.of("e3, e2", piecePositions("e3", "e2")))
            );
        }
    }

    @Nested
    class 직선이_아닌_경로로_움직이는_경우 {

        @ParameterizedTest(name = "움직일 수 없다. [e4] -> [{1}]")
        @CsvSource({
                "f5",
                "f3",
                "d5",
                "d3",
        })
        void 움직일_수_없다(final PiecePosition destination) {
            // given
            final Path path = Path.of(source, destination);

            // when & then
            assertThat(strategy.movable(path)).isFalse();
        }

        @ParameterizedTest(name = "경유지를 조회하면 예외. [e4] -> [{1}]")
        @CsvSource({
                "f5",
                "f3",
                "d5",
                "d3",
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
