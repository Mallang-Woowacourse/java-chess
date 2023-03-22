package chess.infrastructure.persistence.entity;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.game.ChessGame;
import chess.domain.game.state.EndGame;
import chess.domain.game.state.MovePiece;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ChessGameEntity {

    private static final AtomicLong idGen = new AtomicLong(1L);

    private final Long id;
    private final String state;
    private final String turn;
    private final String winner;

    public ChessGameEntity(final Long id, final String state, final String turn, final String winner) {
        this.id = id;
        this.state = state;
        this.turn = turn;
        this.winner = winner;
    }

    public static ChessGameEntity fromDomain(final ChessGame chessGame) {
        if (chessGame.state() instanceof MovePiece) {
            final MovePiece state = (MovePiece) chessGame.state();
            return new ChessGameEntity(
                    genId(chessGame),
                    state.getClass().getSimpleName(),
                    state.turn().color().name(),
                    null
            );
        }
        final EndGame state = (EndGame) chessGame.state();
        return new ChessGameEntity(
                genId(chessGame),
                state.getClass().getSimpleName(),
                null,
                state.winColor().name()
        );
    }

    private static Long genId(final ChessGame chessGame) {
        Long id = chessGame.id();
        if (id != null) {
            return id;
        }
        return idGen.getAndIncrement();
    }

    public ChessGame toDomain(final List<PieceEntity> pieceEntities) {
        final List<Piece> pieces = pieceEntities.stream()
                .map(PieceEntity::toDomain)
                .collect(Collectors.toList());

        if (state.equals(EndGame.class.getSimpleName())) {
            final EndGame endGame = new EndGame(Color.valueOf(winner));
            return new ChessGame(id, ChessBoard.from(pieces), endGame);
        }

        final MovePiece movePiece = new MovePiece(new Turn(Color.valueOf(turn)));
        return new ChessGame(id, ChessBoard.from(pieces), movePiece);
    }

    public Long id() {
        return id;
    }

    public String state() {
        return state;
    }

    public String turn() {
        return turn;
    }

    public String winner() {
        return winner;
    }
}
