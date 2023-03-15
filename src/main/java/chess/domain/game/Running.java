package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.piece.position.PiecePosition;

public class Running extends InitializedGameState {

    private final Turn turn;

    public Running(final ChessBoard chessBoard, final Turn turn) {
        super(chessBoard);
        this.turn = turn;
    }

    @Override
    public GameState movePiece(final PiecePosition source, final PiecePosition destination) {
        chessBoard.movePiece(turn, source, destination);

        return judgeNext();
    }

    private GameState judgeNext() {
        if (chessBoard.kingDie()) {
            return new End(chessBoard);
        }
        if (chessBoard.checkmatedBy(turn)) {
            return new Checkmate(chessBoard, turn.change());
        }
        return new Running(chessBoard, turn.change());
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public GameState toEnd() {
        return new End(chessBoard);
    }
}
