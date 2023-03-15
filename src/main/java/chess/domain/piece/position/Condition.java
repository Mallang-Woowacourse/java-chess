package chess.domain.piece.position;

public enum Condition {
    IMPOSSIBLE,
    POSSIBLE,
    ONLY_DESTINATION_ENEMY,
    ;

    public boolean impossible() {
        return this == IMPOSSIBLE;
    }

    public boolean onlyDestinationEnemy() {
        return this == ONLY_DESTINATION_ENEMY;
    }
}
