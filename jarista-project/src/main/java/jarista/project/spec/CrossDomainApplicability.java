package jarista.project.spec;

import jarista.detail.DetailBuilder;
import jarista.detail.RoledDetail;
import jarista.spec.L3_Spec;

import java.util.List;

import static jarista.detail.DetailRole.*;

/** L3 — How the 南郭 concept transfers beyond code: finance, education, org design. */
public class CrossDomainApplicability implements L3_Spec<CulturalStudies> {
    public static final CrossDomainApplicability INSTANCE = new CrossDomainApplicability();

    private static final DetailBuilder _db = new DetailBuilder();

    @Override
    public List<RoledDetail> details() {
        return List.of(
            _db.text()
                .ln("Wherever people hide behind process, Jarista applies.")
                .ln("The 南郭 pattern is universal — it appears in any system")
                .ln("where ensemble performance masks individual contribution.")
            .as(DESCRIPTION),

            _db.text()
                .ln("Potential domains:")
                .ln("• Education — curriculum maps where courses exist on paper only")
                .ln("• Finance — compliance structures that check boxes but miss risks")
                .ln("• Org design — role hierarchies where responsibilities are unclear")
                .ln("• Research — grant deliverables that look complete but aren't")
            .as(NOTE),

            _db.text()
                .ln("The graph structure is domain-agnostic.")
                .ln("L1–L18 specs can model any decomposition.")
                .ln("The insight is not 'use Java for PM' —")
                .ln("it is 'make structure compile-checkable, and fakers cannot hide.'")
            .as(RATIONALE)
        );
    }
}
