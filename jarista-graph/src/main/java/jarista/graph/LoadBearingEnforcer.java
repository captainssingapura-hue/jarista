package jarista.graph;

import jarista.Stateless;
import jarista.piece.PieceBase;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validates LoadBearing constraints:
 * <ol>
 *   <li>Pieces may only target specs that implement {@link LoadBearing}.</li>
 *   <li>When a piece says {@code done()=true}, the spec must say {@code DONE},
 *       and vice-versa.</li>
 * </ol>
 */
public final class LoadBearingEnforcer {

    private static final Pattern PIECE_PAT = Pattern.compile("L\\d+_Piece");

    public List<Violation> check(List<Class<? extends Stateless>> classes) {
        var violations = new ArrayList<Violation>();

        for (var cls : classes) {
            Class<?> targetSpec = extractPieceTarget(cls);
            if (targetSpec == null) continue; // not a piece

            Stateless specInstance = getInstance(targetSpec);
            Stateless pieceInstance = getInstance(cls);
            if (specInstance == null || pieceInstance == null) continue;

            // Rule 1: piece must target a LoadBearing spec
            if (!(specInstance instanceof LoadBearing lb)) {
                violations.add(new Violation.PieceTargetsNonLeaf(cls, targetSpec.asSubclass(Stateless.class)));
                continue;
            }

            // Rule 2: status consistency
            boolean pieceDone = (pieceInstance instanceof PieceBase pb) && pb.done();
            SpecStatus specStatus = lb.status();

            if (pieceDone && specStatus != SpecStatus.DONE) {
                violations.add(new Violation.StatusMismatch(
                        targetSpec.asSubclass(Stateless.class), specStatus, cls, true));
            }
            if (!pieceDone && specStatus == SpecStatus.DONE) {
                violations.add(new Violation.StatusMismatch(
                        targetSpec.asSubclass(Stateless.class), specStatus, cls, false));
            }
        }

        return violations;
    }

    /** Returns the spec class targeted by a piece, or null if cls is not a piece. */
    private static Class<?> extractPieceTarget(Class<?> cls) {
        for (Type iface : cls.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType pt) {
                Type raw = pt.getRawType();
                if (raw instanceof Class<?> r && PIECE_PAT.matcher(r.getSimpleName()).matches()) {
                    Type[] args = pt.getActualTypeArguments();
                    if (args.length > 0 && args[0] instanceof Class<?> target) return target;
                }
            }
        }
        return null;
    }

    private static Stateless getInstance(Class<?> cls) {
        try {
            Field f = cls.getField("INSTANCE");
            return (Stateless) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
