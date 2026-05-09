package jarista.state;

import java.time.Instant;
import java.util.Optional;

public record Freshness(Instant lastTouched, Optional<String> acknowledgment) {

    public static Freshness at(Instant when) {
        return new Freshness(when, Optional.empty());
    }

    public static Freshness acknowledged(Instant when, String reason) {
        return new Freshness(when, Optional.of(reason));
    }
}
