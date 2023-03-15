package chess.controller;

import chess.domain.board.ChessBoard;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.position.File;
import chess.domain.piece.position.PiecePosition;
import chess.domain.piece.position.Rank;
import chess.view.InputView;
import chess.view.OutputView;

public class ChessGameController {

    public void run() {
        final ChessBoard chessBoard = ChessBoard.create();
        final ChessGame chessGame = new ChessGame(chessBoard, Color.WHITE);
        OutputView.printStartMessage();

        while (chessGame.runnable()) {
            withException(chessGame);
        }

        System.out.println("게임이 끝났습니다.");
    }

    private void withException(final ChessGame chessGame) {
        try {
            execute(chessGame);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());

            OutputView.showBoard(chessGame.pieces());
            System.out.println();
            System.out.println();
        }
    }

    private void execute(final ChessGame chessGame) {
        final String input = InputView.readCommand();
        final Command command = Command.match(input);

        if (command == Command.START) {
            chessGame.initialized();
        }

        if (command == Command.END) {
            chessGame.toEnd();
        }

        if (command == Command.MOVE) {
            final String[] split = input.split(" ");
            final PiecePosition source = parsePosition(split[1]);
            final PiecePosition dest = parsePosition(split[2]);
            chessGame.movePiece(source, dest);
        }

        OutputView.showBoard(chessGame.pieces());
    }

    private PiecePosition parsePosition(final String position) {
        final File file = File.from(position.charAt(0));
        final Rank rank = Rank.from(Integer.parseInt(position.split("")[1]));
        return PiecePosition.of(rank, file);
    }

    public enum Command {
        START,
        END,
        MOVE;

        public static Command match(final String command) {
            return Command.valueOf(command.split(" ")[0].toUpperCase());
        }
    }
}
