package chess.domain.piece.type;

import chess.domain.piece.Color;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Waypoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("King 은")
class KingTest {

    @ParameterizedTest(name = "모든 방향으로 한 칸 이동 가능하며, 경우지는 없다. [{0}으로 한 칸 이동이 가능하다]")
    @MethodSource("unitDestinations")
    void 모든_방향으로_한_칸_이동_가능하다(final PiecePosition destination, final Waypoints expected) {
        // given
        final PiecePosition currentPosition = PiecePosition.of(4, 'e');
        final King king = new King(Color.WHITE, currentPosition);

        // when & then
        final Waypoints condition = king.waypoints(destination);
        assertThat(condition.wayPoints()).containsExactlyInAnyOrderElementsOf(expected.wayPoints());
    }

    static Stream<Arguments> unitDestinations() {
        return Stream.of(
                Arguments.of(
                        Named.of("동쪽", PiecePosition.of(4, 'f')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("서쪽", PiecePosition.of(4, 'd')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("북쪽", PiecePosition.of(5, 'e')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("남쪽", PiecePosition.of(3, 'e')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("북동쪽", PiecePosition.of(5, 'f')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("남동쪽", PiecePosition.of(3, 'f')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("북서쪽", PiecePosition.of(5, 'd')),
                        Waypoints.from(Collections.emptyList())
                ),
                Arguments.of(
                        Named.of("남서쪽", PiecePosition.of(3, 'd')),
                        Waypoints.from(Collections.emptyList())
                )
        );
    }

    @ParameterizedTest(name = "두 칸 이상은 이동할 수 없다 [e, 4 -> {0}, {1}]")
    @CsvSource({
            // 기준 e, 4
            "g,4",
            "c,4",
            "e,6",
            "e,2",
            "g,6",
            "g,2",
            "c,6",
            "c,2"
    })
    void 두칸_이상은_이동할_수_없다(final char file, final int rank) {
        // given
        final PiecePosition currentPosition = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(rank, file);
        final King king = new King(Color.WHITE, currentPosition);

        // when & then
        assertThatThrownBy(() -> king.waypoints(destination))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
