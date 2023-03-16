package chess.controller;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardFactory;
import chess.domain.state.ChessState;
import chess.domain.state.Initialize;
import chess.domain.state.command.Command;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.ArrayList;
import java.util.List;

public class ChessController {

    public void start() {
        OutputView.printStartMessage();
        final ChessBoard chessBoard = ChessBoardFactory.create();
        final ChessState state = new Initialize(chessBoard);
        run(chessBoard, state);
    }

    private void run(final ChessBoard chessBoard, ChessState state) {
        while (state.runnable()) {
            state = command(chessBoard, state);
        }
    }

    private ChessState command(final ChessBoard chessBoard, ChessState state) {
        try {
            final List<String> command = InputView.readCommand();
            state = state.command(Command.parse(new ArrayList<>(command)));
            OutputView.showBoard(chessBoard.pieces());
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return state;
    }
}
