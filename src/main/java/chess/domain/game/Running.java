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

        if (chessBoard.checkmatedBy(turn.change())) {
            chessBoard.movePiece(turn, destination, source);
            throw new IllegalArgumentException("해당 위치는 체크메이트되기 때문에 이동할 수 없습니다.");
        }

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
