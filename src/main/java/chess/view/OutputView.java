package chess.view;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.position.File;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;
import chess.domain.piece.type.Bishop;
import chess.domain.piece.type.King;
import chess.domain.piece.type.Knight;
import chess.domain.piece.type.Queen;
import chess.domain.piece.type.Rook;
import chess.domain.piece.type.pawn.Pawn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OutputView {

    private static final Map<Color, Map<Class<?>, String>> colorIconMapping = new HashMap<>();
    private static final Map<Class<?>, String> whitePieceIcons = new HashMap<>();
    private static final Map<Class<?>, String> blackPieceIcons = new HashMap<>();
    private static final String EMPTY_PIECE_ICON = ".";

    static {
        whitePieceIcons.put(Rook.class, "r");
        whitePieceIcons.put(Pawn.class, "p");
        whitePieceIcons.put(Bishop.class, "b");
        whitePieceIcons.put(King.class, "k");
        whitePieceIcons.put(Knight.class, "n");
        whitePieceIcons.put(Queen.class, "q");

        blackPieceIcons.put(Rook.class, "R");
        blackPieceIcons.put(Pawn.class, "P");
        blackPieceIcons.put(Bishop.class, "B");
        blackPieceIcons.put(King.class, "K");
        blackPieceIcons.put(Knight.class, "N");
        blackPieceIcons.put(Queen.class, "Q");

        colorIconMapping.put(Color.WHITE, whitePieceIcons);
        colorIconMapping.put(Color.BLACK, blackPieceIcons);
    }

    public static void showBoard(final List<Piece> chessBoard) {
        final Map<PiecePosition, Piece> pieceMap = chessBoard.stream()
                .collect(Collectors.toMap(Piece::piecePosition, Function.identity()));
        for (int rank = Rank.MAX; rank >= Rank.MIN; rank--) {
            System.out.println(makeLineFormat(pieceMap, rank));
        }
        System.out.println();
    }

    private static String makeLineFormat(final Map<PiecePosition, Piece> chessBoard, final int rank) {
        return IntStream.rangeClosed(File.MIN, File.MAX)
                .mapToObj(file -> makeFormat(chessBoard, rank, (char) file))
                .collect(Collectors.joining(""));
    }

    private static String makeFormat(final Map<PiecePosition, Piece> chessBoard, final int rank, final char file) {
        return Optional.ofNullable(chessBoard.get(PiecePosition.of(rank, file)))
                .map(OutputView::convertCaseAccordingToColor)
                .orElse(EMPTY_PIECE_ICON);
    }

    private static String convertCaseAccordingToColor(final Piece piece) {
        final Map<Class<?>, String> pieceIconMap = colorIconMapping.get(piece.color());
        return pieceIconMap.get(piece.getClass());
    }

    public static void printStartMessage() {
        System.out.println("> 체스 게임을 시작합니다.");
        System.out.println("> 게임 시작 : start");
        System.out.println("> 게임 종료 : end");
        System.out.println("> 게임 이동 : move source 위치 target 위치 - 예. move b2 b3");
    }

    public static void error(final String message) {
        System.out.println("[ERROR] " + message);
    }
}
