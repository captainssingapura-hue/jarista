package jarista.project.spec;

import jarista.spec.L3_Spec;

/** L3 — Export graph as PNG/SVG image or markdown status report. */
public class ExportReport implements L3_Spec<UILayer> {
    public static final ExportReport INSTANCE = new ExportReport();
}
