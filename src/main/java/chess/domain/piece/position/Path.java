package chess.domain.piece.position;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Stream.iterate;

public class Path {

    private final PiecePosition source;
    private final PiecePosition destination;

    private Path(final PiecePosition source, final PiecePosition destination) {
        validate(source, destination);
        this.source = source;
        this.destination = destination;
    }

    private void validate(final PiecePosition source, final PiecePosition destination) {
        if (source.equals(destination)) {
            throw new IllegalArgumentException("출발지와 목적지가 동일할 수 없습니다.");
        }
    }

    public static Path of(final PiecePosition source, final PiecePosition destination) {
        return new Path(source, destination);
    }

    public int fileDisplacement() {
        return source.fileDisplacement(destination);
    }

    public int rankDisplacement() {
        return source.rankDisplacement(destination);
    }

    public boolean isDestinationRelativelySouth() {
        return source.rankDisplacement(destination) < 0;
    }

    public boolean isDestinationRelativelyNorth() {
        return source.rankDisplacement(destination) > 0;
    }

    public boolean isStraight() {
        return !(Math.abs(source.rankDisplacement(destination)) > 0
                && Math.abs(source.fileDisplacement(destination)) > 0);
    }

    public boolean isDiagonal() {
        return Math.abs(source.rankDisplacement(destination)) == Math.abs(source.fileDisplacement(destination));
    }

    public boolean isUnitDistance() {
        return Math.abs(source.rankDisplacement(destination)) <= 1
                && Math.abs(source.fileDisplacement(destination)) <= 1;
    }

    // 이곳에서 Waypoint 반환 VS 반환받은데서 Waypoint.from(반환) ??
    public List<PiecePosition> waypoints() {
        return iterate(source, current -> !current.equals(destination),
                current -> current.move(current.direction(destination)))
                .skip(1)
                .collect(Collectors.toList());
    }
}
