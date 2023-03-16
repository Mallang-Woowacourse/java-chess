package chess.domain.piece.position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Path 는")
class PathTest {

    @Test
    void 생성시_출발지와_목적지가_동일하면_예외() {
        // given
        final PiecePosition source = PiecePosition.of(3, 'c');
        final PiecePosition destination = PiecePosition.of(3, 'c');

        // when & then
        assertThatThrownBy(() -> Path.of(source, destination))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출발지와_목적지의_File_거리_차이를_구할_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of(3, 'c');
        final PiecePosition destination = PiecePosition.of(4, 'a');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.fileDisplacement()).isEqualTo(-2);
    }

    @Test
    void 출발지와_목적지의_Rank_거리_차이를_구할_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of(3, 'c');
        final PiecePosition destination = PiecePosition.of(4, 'a');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.rankDisplacement()).isEqualTo(1);
    }

    @Test
    void 목적지가_상대적으로_남쪽에_있는지_확인할_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of(3, 'c');
        final PiecePosition southDestination = PiecePosition.of(1, 'f');
        final PiecePosition northDestination = PiecePosition.of(5, 'a');
        final Path southPath = Path.of(source, southDestination);
        final Path northPath = Path.of(source, northDestination);

        // when & then
        assertThat(southPath.isDestinationRelativelySouth()).isTrue();
        assertThat(northPath.isDestinationRelativelySouth()).isFalse();
    }

    @Test
    void 목적지가_상대적으로_북쪽에_있는지_확인할_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of(3, 'c');
        final PiecePosition southDestination = PiecePosition.of(1, 'f');
        final PiecePosition northDestination = PiecePosition.of(5, 'a');
        final Path southPath = Path.of(source, southDestination);
        final Path northPath = Path.of(source, northDestination);

        // when & then
        assertThat(southPath.isDestinationRelativelyNorth()).isFalse();
        assertThat(northPath.isDestinationRelativelyNorth()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "a,4",
            "f,4",
            "d,4",
            "h,4",
            "e,1",
            "e,8",
            "e,3",
            "e,5",
    })
    void isStraight_는_직선_경로이면_true(final char file, final int rank) {
        // given
        final PiecePosition destination = PiecePosition.of(rank, file);
        final PiecePosition source = PiecePosition.of(4, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.isStraight()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "a,5",
            "f,3",
            "d,5",
            "h,3",
            "a,1",
            "f,5",
    })
    void isStraight_는_직선_경로가_아니면_false(final char file, final int rank) {
        // given
        final PiecePosition destination = PiecePosition.of(rank, file);
        final PiecePosition source = PiecePosition.of(4, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.isStraight()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "f,5",  // 북동
            "f,3",  // 남동
            "d,5",  // 북서
            "d,3",  // 남서
            "g,6",  // 북동
            "g,2",  // 남동
            "c,6",  // 북서
            "c,2"   // 남서
    })
    void isDiagonal_은_대각선_경로이면_true(final char file, final int rank) {
        // given
        final PiecePosition destination = PiecePosition.of(rank, file);
        final PiecePosition source = PiecePosition.of(4, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.isDiagonal()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "a,4",
            "f,4",
            "d,4",
            "h,4",
            "e,1",
            "e,8",
            "e,3",
            "e,5",
    })
    void isDiagonal_은_대각선_경로가_아니면_false(final char file, final int rank) {
        // given
        final PiecePosition destination = PiecePosition.of(rank, file);
        final PiecePosition source = PiecePosition.of(4, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.isDiagonal()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "e,5",
            "e,3",
            "d,4",
            "f,4",
            "f,5",
            "d,5",
            "f,3",
            "d,3",
    })
    void isUnitDistance_는_한칸_거리면_true(final char file, final int rank) {
        // given
        final PiecePosition destination = PiecePosition.of(rank, file);
        final PiecePosition source = PiecePosition.of(4, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.isUnitDistance()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // 기준 e, 4
            "e,6",
            "e,2",
            "c,4",
            "g,4",
            "g,6",
            "c,6",
            "g,2",
            "c,2",
    })
    void isUnitDistance_는_한칸_거리가_아니면_false(final char file, final int rank) {
        // given
        final PiecePosition destination = PiecePosition.of(rank, file);
        final PiecePosition source = PiecePosition.of(4, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.isUnitDistance()).isFalse();
    }

    @Test
    void 직선_경로에_대한_경유지를_구할_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(7, 'e');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.waypoints())
                .containsExactlyInAnyOrder(
                        PiecePosition.of(5, 'e'),
                        PiecePosition.of(6, 'e')
                );
    }

    @Test
    void 대각선_경로에_대한_경유지를_구할_수_있다() {
        // given
        final PiecePosition source = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(7, 'h');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.waypoints())
                .containsExactlyInAnyOrder(
                        PiecePosition.of(5, 'f'),
                        PiecePosition.of(6, 'g')
                );
    }

    @Test
    void 출발지와_목적지가_한칸_차이면_경유지가_없다() {
        // given
        final PiecePosition source = PiecePosition.of(4, 'e');
        final PiecePosition destination = PiecePosition.of(5, 'f');
        final Path path = Path.of(source, destination);

        // when & then
        assertThat(path.waypoints()).isEmpty();
    }
}
