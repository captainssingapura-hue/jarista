package jarista.state;

import jarista.Stateless;

import java.util.Optional;

public interface StateStore {
    PieceStatus statusOf(Stateless piece);
    Optional<String> assigneeOf(Stateless piece);
    Freshness freshnessOf(Stateless piece);
}
