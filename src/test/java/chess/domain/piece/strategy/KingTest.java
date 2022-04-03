package chess.domain.piece.strategy;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.board.position.Column;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import chess.domain.piece.King;
import chess.domain.piece.attribute.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KingTest {

    @ParameterizedTest
    @CsvSource(value = {
            "E,ONE,D,ONE",
            "E,ONE,F,ONE",
            "E,ONE,E,TWO",
            "E,TWO,E,ONE",
            "E,ONE,F,TWO",
            "E,ONE,D,TWO",
            "E,TWO,F,ONE",
            "E,TWO,D,TWO"
    })
    @DisplayName("킹이 갈 수 있는 위치 중 하나여야 한다.")
    void canValidMove(Column columnA, Rank rankA, Column columnB, Rank rankB) {
        assertDoesNotThrow(() -> new King(Team.WHITE)
                .canMove(new King(Team.WHITE),
                        new Position(columnA, rankA),
                        new Position(columnB, rankB)
                )
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "E,ONE,E,THREE",
            "E,ONE,F,THREE"
    })
    @DisplayName("킹이 갈 수 있는 위치가 아니면 에러가 발생한다.")
    void canInvalidMove(Column columnA, Rank rankA, Column columnB, Rank rankB) {
        assertThatThrownBy(() -> new King(Team.WHITE)
                .canMove(new King(Team.WHITE),
                        new Position(columnA, rankA),
                        new Position(columnB, rankB)
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("킹이 이동할 수 없는 위치입니다.");
    }
}