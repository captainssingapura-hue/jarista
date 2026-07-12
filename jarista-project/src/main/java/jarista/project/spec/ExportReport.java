package jarista.project.spec;

import jarista.spec.L3_Spec;
import jarista.spec.LoadBearing;
import jarista.spec.SpecStatus;

/** L3 — Export graph as PNG/SVG image or markdown status report. */
public class ExportReport implements L3_Spec<UILayer>, LoadBearing {
    public static final ExportReport INSTANCE = new ExportReport();
    @Override public SpecStatus status() { return SpecStatus.PLANNED; }
}
