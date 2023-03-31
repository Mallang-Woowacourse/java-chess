package chess.infrastructure.persistence.mapper;

import chess.domain.piece.Color;
import chess.domain.piece.MovementType;
import chess.domain.piece.Piece;
import chess.domain.piece.movestrategy.BishopMovementStrategyDefault;
import chess.domain.piece.movestrategy.KingMovementStrategyDefault;
import chess.domain.piece.movestrategy.KnightMovementStrategy;
import chess.domain.piece.movestrategy.PieceMovementStrategy;
import chess.domain.piece.movestrategy.QueenMovementStrategyDefault;
import chess.domain.piece.movestrategy.RookMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.BlackPawnMovementStrategyDefault;
import chess.domain.piece.movestrategy.pawn.WhitePawnMovementStrategyDefault;
import chess.domain.piece.position.File;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;
import chess.infrastructure.persistence.entity.PieceEntity;

import java.util.Map;

public class PieceMapper {

    private static final Map<MovementType, PieceMovementStrategy> strategyMap = Map.of(
            MovementType.KING, new KingMovementStrategyDefault(),
            MovementType.QUEEN, new QueenMovementStrategyDefault(),
            MovementType.BISHOP, new BishopMovementStrategyDefault(),
            MovementType.KNIGHT, new KnightMovementStrategy(),
            MovementType.ROOK, new RookMovementStrategyDefault(),
            MovementType.BLACK_PAWN, new BlackPawnMovementStrategyDefault(),
            MovementType.WHITE_PAWN, new WhitePawnMovementStrategyDefault()
    );

    public static PieceEntity fromDomain(final Piece piece, final Long chessGameId) {
        return new PieceEntity(
                piece.piecePosition().rank().value(),
                piece.piecePosition().file().value(),
                piece.color().name(),
                piece.type().name(),
                chessGameId
        );
    }

    public static Piece toDomain(final PieceEntity pieceEntity) {
        final Color color = Color.valueOf(pieceEntity.color());
        return new Piece(color,
                PiecePosition.of(
                        Rank.from(pieceEntity.rank()),
                        File.from(pieceEntity.file())
                ),
                strategyMap.get(MovementType.valueOf(pieceEntity.movementType())));
    }
}
