package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.movestrategy.BishopMovementStrategyDefault;
import chess.domain.piece.movestrategy.KingMovementStrategyDefault;
import chess.domain.piece.movestrategy.KnightMovementStrategy;
import chess.domain.piece.movestrategy.QueenMovementStrategyDefault;
import chess.domain.piece.movestrategy.RookMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.BlackPawnMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.PawnMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.WhitePawnMovementStrategyDefault;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ChessBoard 은")
class ChessBoardTest {

    /**
     * RNBQKBNR  8 (rank 8)
     * PPPPPPPP  7
     * ........  6
     * ........  5
     * ........  4
     * ........  3
     * pppppppp  2
     * rnbqkbnr  1 (rank 1)
     * <p>
     * abcdefgh
     */
    private ChessBoard chessBoard;

    @BeforeEach
    void setup() {
        chessBoard = new ChessBoardFactory().create();
    }

    @Test
    void 기본_체스_규칙에_맞게_생성된다() {
        // then
        assertPiece(chessBoard, 1, 'a', WHITE, RookMovementStrategyDefault.class);
        assertPiece(chessBoard, 1, 'b', WHITE, KnightMovementStrategy.class);
        assertPiece(chessBoard, 1, 'c', WHITE, BishopMovementStrategyDefault.class);
        assertPiece(chessBoard, 1, 'd', WHITE, QueenMovementStrategyDefault.class);
        assertPiece(chessBoard, 1, 'e', WHITE, KingMovementStrategyDefault.class);
        assertPiece(chessBoard, 1, 'f', WHITE, BishopMovementStrategyDefault.class);
        assertPiece(chessBoard, 1, 'g', WHITE, KnightMovementStrategy.class);
        assertPiece(chessBoard, 1, 'h', WHITE, RookMovementStrategyDefault.class);
        assertPiece(chessBoard, 8, 'a', Color.BLACK, RookMovementStrategyDefault.class);
        assertPiece(chessBoard, 8, 'b', Color.BLACK, KnightMovementStrategy.class);
        assertPiece(chessBoard, 8, 'c', Color.BLACK, BishopMovementStrategyDefault.class);
        assertPiece(chessBoard, 8, 'd', Color.BLACK, QueenMovementStrategyDefault.class);
        assertPiece(chessBoard, 8, 'e', Color.BLACK, KingMovementStrategyDefault.class);
        assertPiece(chessBoard, 8, 'f', Color.BLACK, BishopMovementStrategyDefault.class);
        assertPiece(chessBoard, 8, 'g', Color.BLACK, KnightMovementStrategy.class);
        assertPiece(chessBoard, 8, 'h', Color.BLACK, RookMovementStrategyDefault.class);
        for (char file = 'a'; file <= 'h'; file++) {
            assertPiece(chessBoard, 2, file, WHITE, PawnMovementStrategyDefault.class);
            assertPiece(chessBoard, 7, file, Color.BLACK, PawnMovementStrategyDefault.class);
        }
    }

    private static void assertPiece(final ChessBoard chessBoard,
                                    final int rank,
                                    final char file,
                                    final Color color,
                                    final Class<?> type) {
        final Piece piece = chessBoard.findByPosition(PiecePosition.of(rank, file));
        assertThat(piece.color()).isEqualTo(color);
        assertThat(piece.pieceMovementStrategy()).isInstanceOf(type);
    }

    @Test
    void 위치를_받아_주어진_위치의_말을_이동시킨다() {
        // when
        final ChessBoard moved =
                chessBoard.movePiece(new Turn(WHITE), PiecePosition.of(2, 'b'), PiecePosition.of(3, 'b'));

        // then
        final Map<PiecePosition, Piece> pieceMap = moved.pieces()
                .stream()
                .collect(toMap(Piece::piecePosition, Function.identity()));
        assertThat(pieceMap.get(PiecePosition.of(2, 'b'))).isNull();
        assertThat(pieceMap.get(PiecePosition.of(3, 'b')).pieceMovementStrategy())
                .isInstanceOf(PawnMovementStrategyDefault.class);
    }

    @Test
    void 상대_말을_움직이면_오류() {
        // when & then
        assertThatThrownBy(() ->
                chessBoard.movePiece(new Turn(BLACK),
                        PiecePosition.of(2, 'b'),
                        PiecePosition.of(3, 'b'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 말이_없으면_오류() {
        // when & then
        assertThatThrownBy(() ->
                chessBoard.movePiece(new Turn(WHITE),
                        PiecePosition.of(5, 'b'),
                        PiecePosition.of(3, 'b'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 움직일_수_없는_위치면_오류() {
        // when & then
        assertThatThrownBy(() ->
                chessBoard.movePiece(new Turn(BLACK),
                        PiecePosition.of(7, 'b'),
                        PiecePosition.of(4, 'b'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 움직일_수_있으나_경로에_말이_존재하면_오류() {
        // when & then
        assertThatThrownBy(() ->
                chessBoard.movePiece(new Turn(BLACK),
                        PiecePosition.of(8, 'd'),
                        PiecePosition.of(6, 'b'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 움직일_수_있으나_도착지에_아군_말이_존재하면_오류() {
        // when & then
        assertThatThrownBy(() ->
                chessBoard.movePiece(new Turn(BLACK),
                        PiecePosition.of(8, 'd'),
                        PiecePosition.of(7, 'c'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 폰이_일직선으로_한칸_움직이는_경우_적군_말이_있으면_오류() {
        // given
        chessBoard.movePiece(new Turn(WHITE),
                PiecePosition.of(2, 'b'),
                PiecePosition.of(4, 'b'));
        chessBoard.movePiece(new Turn(BLACK),
                PiecePosition.of(7, 'b'),
                PiecePosition.of(5, 'b'));

        // when & then
        assertThatThrownBy(() ->
                chessBoard.movePiece(new Turn(WHITE),
                        PiecePosition.of(4, 'b'),
                        PiecePosition.of(5, 'b'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 폰이_일직선으로_두칸_움직이는_경우_적군_말이_있으면_오류() {
        // given
        final ChessBoard moved
                = chessBoard.movePiece(new Turn(BLACK),
                PiecePosition.of(7, 'b'),
                PiecePosition.of(5, 'b')
        ).movePiece(new Turn(BLACK),
                PiecePosition.of(5, 'b'),
                PiecePosition.of(4, 'b')
        );

        // when & then
        assertThatThrownBy(() ->
                moved.movePiece(new Turn(WHITE),
                        PiecePosition.of(2, 'b'),
                        PiecePosition.of(4, 'b'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 폰이_대각선으로_움직이는_경우_적군_말이_없으면_오류() {
        // given
        final ChessBoard moved = chessBoard.movePiece(new Turn(WHITE),
                PiecePosition.of(2, 'b'),
                PiecePosition.of(4, 'b'));

        // when & then
        assertThatThrownBy(() ->
                moved.movePiece(new Turn(WHITE),
                        PiecePosition.of(4, 'b'),
                        PiecePosition.of(5, 'c'))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 폰이_대각선으로_움직이려면_적군_말이_필요하다() {
        // given
        final ChessBoard moved = chessBoard.movePiece(
                new Turn(WHITE),
                PiecePosition.of(2, 'b'),
                PiecePosition.of(4, 'b')
        ).movePiece(
                new Turn(BLACK),
                PiecePosition.of(7, 'c'),
                PiecePosition.of(5, 'c')
        );

        // when
        final ChessBoard result = moved.movePiece(new Turn(WHITE),
                PiecePosition.of(4, 'b'),
                PiecePosition.of(5, 'c'));

        // then
        assertThat(result.pieces().size()).isEqualTo(31);
    }

    @Test
    void 적군_말을_잡으면_해당_말은_사라진다() {
        // given
        final ChessBoard moved = chessBoard.movePiece(new Turn(WHITE),
                        PiecePosition.of(2, 'e'),
                        PiecePosition.of(4, 'e'))
                .movePiece(new Turn(WHITE),
                        PiecePosition.of(1, 'd'),
                        PiecePosition.of(5, 'h'));

        // when
        assertThat(moved.pieces().size()).isEqualTo(32);
        final ChessBoard afterKill = moved.movePiece(new Turn(WHITE),
                PiecePosition.of(5, 'h'),
                PiecePosition.of(7, 'f'));

        // then
        assertThat(afterKill.pieces().size()).isEqualTo(31);
    }

    @Test
    void existKingByColor_시_색깔에_해당하는_왕이_살아있는_경우_true() {
        // given
        final ChessBoard chessBoard = ChessBoard.from(List.of(
                new Piece(BLACK,
                        PiecePosition.of("d1"),
                        new KingMovementStrategyDefault()),
                new Piece(WHITE,
                        PiecePosition.of("d7"),
                        new KingMovementStrategyDefault())
        ));

        // when & then
        assertAll(
                () -> assertThat(chessBoard.existKingByColor(WHITE)).isTrue(),
                () -> assertThat(chessBoard.existKingByColor(BLACK)).isTrue()
        );
    }

    @Test
    void existKingByColor_시_색깔에_해당하는_왕이_없는_경우_false() {
        // given
        final ChessBoard chessBoard = ChessBoard.from(List.of(
                new Piece(BLACK,
                        PiecePosition.of("d1"),
                        new KingMovementStrategyDefault())
        ));

        // when & then
        assertAll(
                () -> assertThat(chessBoard.existKingByColor(WHITE)).isFalse(),
                () -> assertThat(chessBoard.existKingByColor(BLACK)).isTrue()
        );
    }

    @Test
    void 남아있는_피스들을_대상으로_계산을_진행한다() {
        // when
        final Map<Color, Double> colorDoubleMap = chessBoard.calculateScore();

        // then
        // 퀸(9) + 나이트(2.5 * 2) + 비숍(3 * 2) + 룩(5 * 2) + 폰(1 * 8) = 38
        assertThat(colorDoubleMap.get(WHITE)).isEqualTo(38.0);
        assertThat(colorDoubleMap.get(BLACK)).isEqualTo(38.0);
    }

    /**
     * .KR.....
     * P.PB....
     * .P..Qp..
     * .....p..
     * .....nq.
     * ..P..p..
     * .....pp.
     * ....rk..
     * <p>
     * 흰 : 룩(5) + [File f]의 폰 4개 (2) + 폰(1) + 퀸(9) + 나이트 (2.5) = 19.5
     * 검 : 룩(5) + 폰 2개(2) + [File c]의 폰 2개 (1) + 퀸(9) + 비숍(3)= 20
     */
    @Test
    @DisplayName("같은 색의 폰이 같은 File 에 존재하면 해당 폰들은 0.5점으로 계산한다")
    void sameFilePawnScore() {
        // given
        ChessBoard chessBoard = ChessBoard.from(List.of(
                new Piece(BLACK,
                        PiecePosition.of("b8"),
                        new KingMovementStrategyDefault()),
                new Piece(BLACK,
                        PiecePosition.of("c8"),
                        new RookMovementStrategyDefault()),
                new Piece(BLACK,
                        PiecePosition.of("a7"),
                        new BlackPawnMovementStrategyDefault(Rank.from(7))),
                new Piece(BLACK,
                        PiecePosition.of("c7"),
                        new BlackPawnMovementStrategyDefault(Rank.from(7))),
                new Piece(BLACK,
                        PiecePosition.of("d7"),
                        new BishopMovementStrategyDefault()),
                new Piece(BLACK,
                        PiecePosition.of("b6"),
                        new BlackPawnMovementStrategyDefault(Rank.from(7))),
                new Piece(BLACK,
                        PiecePosition.of("e6"),
                        new QueenMovementStrategyDefault()),
                new Piece(WHITE,
                        PiecePosition.of("f5"),
                        new WhitePawnMovementStrategyDefault(Rank.from(2))),
                new Piece(WHITE,
                        PiecePosition.of("f6"),
                        new WhitePawnMovementStrategyDefault(Rank.from(2))),
                new Piece(WHITE,
                        PiecePosition.of("f4"),
                        new KnightMovementStrategy()),
                new Piece(WHITE,
                        PiecePosition.of("g4"),
                        new QueenMovementStrategyDefault()),
                new Piece(BLACK,
                        PiecePosition.of("c3"),
                        new BlackPawnMovementStrategyDefault(Rank.from(7))),
                new Piece(WHITE,
                        PiecePosition.of("f3"),
                        new WhitePawnMovementStrategyDefault(Rank.from(2))),
                new Piece(WHITE,
                        PiecePosition.of("f2"),
                        new WhitePawnMovementStrategyDefault(Rank.from(2))),
                new Piece(WHITE,
                        PiecePosition.of("g2"),
                        new WhitePawnMovementStrategyDefault(Rank.from(2))),
                new Piece(WHITE,
                        PiecePosition.of("e1"),
                        new RookMovementStrategyDefault()),
                new Piece(WHITE,
                        PiecePosition.of("f1"),
                        new KingMovementStrategyDefault())
        ));

        // when
        final Map<Color, Double> colorDoubleMap = chessBoard.calculateScore();

        // then
        assertThat(colorDoubleMap.get(WHITE)).isEqualTo(19.5);
        assertThat(colorDoubleMap.get(BLACK)).isEqualTo(20);
    }
}
