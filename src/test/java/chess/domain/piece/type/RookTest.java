package chess.domain.piece.type;

import chess.domain.piece.Color;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Waypoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Rook 은")
class RookTest {

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "f,4",  // 동
            "d,4",  // 서
            "e,5",  // 북
            "e,3",  // 남
    })
    void 동서남북_방향으로_한칸은_무조건_이동_가능하다(final char file, final int rank) {
        // given
        final PiecePosition currentPosition = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(rank, file);
        final Rook rook = new Rook(Color.WHITE, currentPosition);

        // when & then
        final Waypoints condition = rook.waypoints(destination);
        assertThat(condition.wayPoints()).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "g,4",  // 동
            "c,4",  // 서
            "e,6",  // 북
            "e,2",  // 남
            "h,4",  // 동
            "a,4",  // 서
            "e,8",  // 북
            "e,1",  // 남
    })
    void 동서남북_방향으로_두칸_이상_움직이려면_경로에_피스가_없어야한다(final char file, final int rank) {
        // given
        final PiecePosition currentPosition = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(rank, file);
        final Rook rook = new Rook(Color.WHITE, currentPosition);

        // when & then
        final Waypoints condition = rook.waypoints(destination);
        assertThat(condition.wayPoints()).isNotEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "f,5",  // 북동
            "f,3",  // 남동
            "d,5",  // 북서
            "d,3"   // 남서
    })
    void 대각선_방향으로_이동할_수_없다(final char file, final int rank) {
        // given
        final PiecePosition currentPosition = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(rank, file);
        final Rook rook = new Rook(Color.WHITE, currentPosition);

        // when & then
        assertThatThrownBy(() -> rook.waypoints(destination))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
