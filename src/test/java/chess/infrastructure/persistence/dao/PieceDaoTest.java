package chess.infrastructure.persistence.dao;

import chess.domain.board.ChessBoardFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.movestrategy.BishopMovementStrategyDefault;
import chess.domain.piece.movestrategy.KingMovementStrategyDefault;
import chess.domain.piece.movestrategy.KnightMovementStrategy;
import chess.domain.piece.movestrategy.QueenMovementStrategyDefault;
import chess.domain.piece.movestrategy.RookMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.BlackPawnMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.WhitePawnMovementStrategyDefault;
import chess.infrastructure.persistence.entity.ChessGameEntity;
import chess.infrastructure.persistence.entity.PieceEntity;
import chess.infrastructure.persistence.mapper.PieceMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("PieceDao 는")
class PieceDaoTest {

    private Long chessGameId;
    private final JdbcTemplate template = new JdbcTemplate();
    private final PieceDao pieceDao = new PieceDao(template);
    private final ChessGameDao chessGameDao = new ChessGameDao(template);

    @BeforeEach
    void setUp() {
        pieceDao.deleteAll();
        chessGameDao.deleteAll();
        final ChessGameEntity chessGameEntity = new ChessGameEntity(null, "MovePiece", "WHITE");
        chessGameDao.save(chessGameEntity);
        chessGameId = chessGameEntity.id();
    }

    @AfterEach
    void clean() {
        pieceDao.deleteAll();
        chessGameDao.deleteAll();
    }

    @Test
    void Piece_들을_저장할_수_있다() {
        // given
        final List<PieceEntity> pieceEntities = new ChessBoardFactory().create().pieces()
                .stream()
                .map(it -> PieceMapper.fromDomain(it, chessGameId))
                .collect(Collectors.toList());

        // when & then
        pieceDao.saveAll(pieceEntities);
    }

    @Test
    void ChessGameId_를_통해_해당_게임에_존재하는_피스들을_조회할_수_있다() {
        // given
        Piece_들을_저장할_수_있다();

        // when
        final Map<Class<?>, List<Piece>> pieceTypeMap = pieceDao.findByAllChessGameId(chessGameId)
                .stream()
                .map(PieceMapper::toDomain)
                .collect(Collectors.groupingBy(it -> it.pieceMovementStrategy().getClass()));

        // then
        assertAll(
                () -> assertThat(pieceTypeMap.get(WhitePawnMovementStrategyDefault.class).size()).isEqualTo(8),
                () -> assertThat(pieceTypeMap.get(BlackPawnMovementStrategyDefault.class).size()).isEqualTo(8),
                () -> assertThat(pieceTypeMap.get(KnightMovementStrategy.class).size()).isEqualTo(4),
                () -> assertThat(pieceTypeMap.get(RookMovementStrategyDefault.class).size()).isEqualTo(4),
                () -> assertThat(pieceTypeMap.get(BishopMovementStrategyDefault.class).size()).isEqualTo(4),
                () -> assertThat(pieceTypeMap.get(KingMovementStrategyDefault.class).size()).isEqualTo(2),
                () -> assertThat(pieceTypeMap.get(QueenMovementStrategyDefault.class).size()).isEqualTo(2)
        );
    }

    @Test
    void ChessGameId_에_해당하는_피스들을_모두_제거할_수_있다() {
        // given
        Piece_들을_저장할_수_있다();

        // when
        pieceDao.deleteAllByChessGameId(chessGameId);

        // then
        assertThat(pieceDao.findByAllChessGameId(chessGameId)).isEmpty();
    }
}
