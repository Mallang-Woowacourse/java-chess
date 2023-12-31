package chess.infrastructure.persistence.mapper;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.game.ChessGame;
import chess.domain.game.GameState;
import chess.domain.piece.Color;
import chess.infrastructure.persistence.entity.ChessGameEntity;
import chess.infrastructure.persistence.entity.PieceEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ChessGameMapper {

    public static ChessGameEntity fromDomain(final ChessGame chessGame) {
        final Long id = chessGame.id();
        return new ChessGameEntity(id,
                chessGame.state().name(),
                chessGame.turnColor().name());
    }

    public static ChessGame toDomain(final ChessGameEntity chessGame, final List<PieceEntity> pieces) {
        final ChessBoard board = ChessBoard.from(pieces
                .stream()
                .map(PieceMapper::toDomain)
                .collect(toList()));
        if (GameState.valueOf(chessGame.state()) == GameState.RUN) {
            return ChessGame.resume(
                    chessGame.id(),
                    board,
                    new Turn(Color.valueOf(chessGame.turn())));
        }
        return ChessGame.end(chessGame.id(),
                board,
                new Turn(Color.valueOf(chessGame.turn())));
    }
}
