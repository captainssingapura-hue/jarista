package jarista.state;

import jarista.Stateless;

public sealed interface PieceStatus {
    record Open() implements PieceStatus {}
    record Active() implements PieceStatus {}
    record Done() implements PieceStatus {}
    record Blocked(Stateless blockedBy) implements PieceStatus {}
    record Other(String description) implements PieceStatus {}
}
