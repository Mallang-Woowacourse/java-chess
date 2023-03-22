package chess.infrastructure.persistence.repository;

import chess.domain.board.ChessBoardFactory;
import chess.domain.game.ChessGame;
import chess.domain.game.ChessGameRepository;
import chess.domain.game.state.EndGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;
import chess.infrastructure.persistence.dao.ChessGameDao;
import chess.infrastructure.persistence.dao.PieceDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("JdbcChessGameRepository 은")
class JdbcChessGameRepositoryTest {

    private final PieceDao pieceDao = new PieceDao();
    private final ChessGameDao chessGameDao = new ChessGameDao();

    private final ChessGameRepository repository = new JdbcChessGameRepository(pieceDao, chessGameDao);

    @BeforeEach
    void setUp() {
        pieceDao.deleteAll();
        chessGameDao.deleteAll();
    }

    @AfterEach
    void clean() {
        pieceDao.deleteAll();
        chessGameDao.deleteAll();
    }

    @Test
    void ChessGame_을_저장할_수_있다() {
        // given
        final ChessGame chessGame = new ChessGame(new ChessBoardFactory().create());

        // when
        final ChessGame save = repository.save(chessGame);

        // then
        assertThat(save.id()).isNotNull();
    }

    @Test
    void id_로_체스_게임을_가져올_수_있다() {
        // given
        final ChessGame chessGame = new ChessGame(new ChessBoardFactory().create());
        final ChessGame save = repository.save(chessGame);
        final List<Long> savedPieceIds = save.pieces()
                .stream()
                .map(Piece::id)
                .collect(Collectors.toList());

        // when
        final Optional<ChessGame> byId = repository.findById(save.id());

        // then
        assertThat(byId.get().pieces())
                .extracting(Piece::id)
                .containsExactlyInAnyOrderElementsOf(savedPieceIds);
    }

    @Test
    void 체스_게임의_상태를_업데이트_할_수_있다() {
        // given
        final ChessGame chessGame = new ChessGame(new ChessBoardFactory().create());
        final ChessGame save = repository.save(chessGame);
        흰색_왕을_죽인다(save);
        final List<Long> savedPieceIds = save.pieces()
                .stream()
                .map(Piece::id)
                .collect(Collectors.toList());

        // when
        repository.update(save);

        // then
        final ChessGame result = repository.findById(save.id()).get();
        assertAll(
                () -> assertThat(result.state()).isInstanceOf(EndGame.class),
                () -> assertThat(result.winColor()).isEqualTo(Color.BLACK),
                () -> assertThat(result.pieces())
                        .extracting(Piece::id)
                        .containsExactlyInAnyOrderElementsOf(savedPieceIds)
        );
    }

    private void 흰색_왕을_죽인다(final ChessGame chessGame) {
        chessGame.movePiece(PiecePosition.of("e2"), PiecePosition.of("e4"));
        chessGame.movePiece(PiecePosition.of("d7"), PiecePosition.of("d5"));
        chessGame.movePiece(PiecePosition.of("e1"), PiecePosition.of("e2"));
        chessGame.movePiece(PiecePosition.of("d5"), PiecePosition.of("d4"));
        chessGame.movePiece(PiecePosition.of("e2"), PiecePosition.of("e3"));
        chessGame.movePiece(PiecePosition.of("d4"), PiecePosition.of("e3"));
    }
}
