package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.board.Turn;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.WayPointsWithCondition;

public class ChessGameValidator {

    private final ChessBoard chessBoard;

    public ChessGameValidator(final ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void validate(final Turn turn, final PiecePosition destination, final Piece piece) {
        validateChoicePiece(turn, piece);
        validateDestinationIsSameTeam(destination, piece);
        validateSatisfyCondition(destination, piece);
    }

    private void validateChoicePiece(final Turn turn, final Piece piece) {
        if (turn.incorrect(piece.color())) {
            throw new IllegalArgumentException("상대 말을 선택했습니다.");
        }
    }

    private void validateDestinationIsSameTeam(final PiecePosition destination, final Piece piece) {
        chessBoard.findByPosition(destination).ifPresent(des -> {
            if (piece.isAlly(des)) {
                throw new IllegalArgumentException("아군이 있는 위치로는 이동할 수 없습니다");
            }
        });
    }

    private void validateSatisfyCondition(final PiecePosition destination, final Piece piece) {
        final WayPointsWithCondition wayPointsWithCondition = piece.wayPointsWithCondition(destination);
        if (!wayPointsWithCondition.satisfy(chessBoard, piece, destination)) {
            throw new IllegalArgumentException("말을 움직일 수 없는 상태입니다");
        }
    }

}
