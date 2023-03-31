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
import chess.domain.piece.position.File;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChessBoardFactory {

    public ChessBoard create() {
        List<Piece> pieces = new ArrayList<>(32);
        pieces.addAll(createPawns(2, Color.WHITE));
        pieces.addAll(createPawns(7, Color.BLACK));
        pieces.addAll(createExcludePawn(1, Color.WHITE));
        pieces.addAll(createExcludePawn(8, Color.BLACK));
        return ChessBoard.from(pieces);
    }

    private List<Piece> createPawns(final int rank, final Color color) {
        return IntStream.rangeClosed(File.MIN, File.MAX)
                .mapToObj(file -> new Piece(color,
                        PiecePosition.of(rank, (char) file),
                        byColor(color, rank)
                )).collect(Collectors.toList());
    }

    private PawnMovementStrategyDefault byColor(final Color color, final int rank) {
        if (color.isWhite()) {
            return new WhitePawnMovementStrategyDefault(Rank.from(rank));
        }
        return new BlackPawnMovementStrategyDefault(Rank.from(rank));
    }

    private List<Piece> createExcludePawn(final int rank, final Color color) {
        List<Piece> pieces = new ArrayList<>(8);
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'a'),
                new RookMovementStrategyDefault()
        ));
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'b'),
                new KnightMovementStrategy()
        ));
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'c'),
                new BishopMovementStrategyDefault())
        );
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'd'),
                new QueenMovementStrategyDefault())
        );
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'e'),
                new KingMovementStrategyDefault())
        );
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'f'),
                new BishopMovementStrategyDefault())
        );
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'g'),
                new KnightMovementStrategy())
        );
        pieces.add(new Piece(color,
                PiecePosition.of(rank, 'h'),
                new RookMovementStrategyDefault())
        );
        return pieces;
    }
}
