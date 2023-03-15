package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.WayPointsWithCondition;
import chess.domain.piece.type.King;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChessBoard {

    private List<Piece> pieces;
    private List<Piece> snapshot;

    private ChessBoard(final List<Piece> pieces) {
        this.pieces = pieces;
    }

    public static ChessBoard create() {
        return new ChessBoard(ChessBoardFactory.create());
    }

    public void movePiece(final Turn turn, final PiecePosition source, final PiecePosition destination) {
        final Piece piece = findByPosition(source).orElseThrow(() -> new IllegalArgumentException("해당 위치에 존재하는 피스가 없습니다."));
        validate(turn, destination, piece);
        snapshot = pieces();
        removeAndMove(destination, piece);
    }

    private void validate(final Turn turn, final PiecePosition destination, final Piece piece) {
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
        findByPosition(destination).ifPresent(des -> {
            if (piece.isAlly(des)) {
                throw new IllegalArgumentException("아군이 있는 위치로는 이동할 수 없습니다");
            }
        });
    }

    private void validateSatisfyCondition(final PiecePosition destination, final Piece piece) {
        final WayPointsWithCondition wayPointsWithCondition = piece.wayPointsWithCondition(destination);
        if (!wayPointsWithCondition.satisfy(this, piece, destination)) {
            throw new IllegalArgumentException("말을 움직일 수 없는 상태입니다");
        }
    }

    private void removeAndMove(final PiecePosition destination, final Piece piece) {
        findByPosition(destination).ifPresent(pieces::remove);
        piece.move(destination);
    }

    public boolean kingDie() {
        return pieces.stream().filter(it -> it instanceof King).count() != 2;
    }

    public boolean checkmatedBy(final Turn turn) {
        final King kingPosition = findKing(turn.nextColor());
        return existKiller(kingPosition);
    }

    private boolean existKiller(final King enemy) {
        return pieces.stream()
                .anyMatch(it -> killable(it, enemy));
    }

    public boolean killable(final Piece piece, final Piece target) {
        if (piece.isAlly(target)) {
            return false;
        }
        final WayPointsWithCondition wayPointsWithCondition = piece.wayPointsWithCondition(target.piecePosition());
        return wayPointsWithCondition.satisfy(this, piece, target.piecePosition());
    }

    public King findKing(final Color color) {
        return pieces.stream().filter(it -> it instanceof King)
                .filter(it -> it.color() == color)
                .map(it -> (King) it)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Optional<Piece> findByPosition(final PiecePosition piecePosition) {
        return pieces.stream()
                .filter(piece -> piece.existIn(piecePosition))
                .findAny();
    }

    public boolean existByPosition(final PiecePosition destination) {
        return pieces.stream()
                .anyMatch(piece -> piece.existIn(destination));
    }

    public void restore() {
        pieces = snapshot;
    }

    public List<Piece> pieces() {
        return pieces.stream().map(Piece::clone)
                .collect(Collectors.toList());
    }
}
