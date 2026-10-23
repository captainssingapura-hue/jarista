# Case Study: Notation Studio × Jarista

## The Project

Notation Studio is a Java music-notation system — a typed ADT for representing
music as code, with MIDI/MusicXML import, playback, auto-accompaniment (drums,
pedal, velocity), and a cookbook-based arrangement engine. Pieces are hand-authored
Java code in Maven modules, versioned with Git.

The project is solo-developed with AI assistance, has shipped substantial
infrastructure (side-channel expressivity model, 7+ auto-drum strategies, MXL
import with repeat expansion, voice separation pipeline, audio export), and
carries a deep roadmap across 30+ design documents.

## Current PM State

The project already practices several things Jarista advocates — by deliberate discipline:

| Practice | How it works today |
|---|---|
| Specs as code-adjacent artifacts | Design docs live in `.jarista/`-equivalent: `notations-docs/src/main/resources/docs/` — a Java resources directory, compiled into the classpath |
| Roadmap as single source of truth | `roadmap.md` — one canonical backlog with shipped/queued/speculative tiers |
| Per-feature plan docs | 20+ topical plan documents linked from the roadmap |
| Status tracking | Manual status headers at the top of each plan doc |
| Cross-doc linking | Relative paths between docs, maintained by hand |
| Architectural patterns | Documented and enforced by convention (side-channel doctrine, Auto-X helpers, tempo-aware math) |

**What's working:** The documentation is exceptionally well-structured. Each plan
doc is self-contained, has a clear status, and links to related docs. The roadmap
is honest about what's shipped, queued, and speculative.

**What's painful:** Everything that Jarista exists to fix.

## Where the Cracks Are

### 1. Specs and tasks are disconnected

The roadmap lists features. The plan docs describe designs. But the link between
"this feature is queued" and "here are the concrete pieces of work" is prose, not
structure.

Example: The [Jiang Cheng Zi plan](pieces/jiang-cheng-zi-plan.md) has four phases
(A through D), each with clear scope and effort estimates. But:

- Phase A (pitch expression) is a prerequisite for Phase D (authoring the song)
  AND a standalone infrastructure deliverable useful to other future songs.
- Phase B (variable tempo) is similarly dual-purpose.
- Phase C (module bootstrap) blocks Phase D but is also a roadmap item in its own right.

These relationships are described in prose. A reader can figure them out. But no
tool can query "what blocks Jiang Cheng Zi?" or "what does shipping pitch expression
unblock?" — the dependency graph is in the author's head.

### 2. Coverage gaps are invisible

The roadmap has ~10 queued small items, ~6 queued medium items, ~4 queued bigger
items, and ~5 speculative items. Some link to plan docs. Some don't. Some plan docs
describe features not on the roadmap. There's no structural way to ask:

- Which queued features have a plan doc? Which don't?
- Which plan docs describe work that's fully shipped? Partially shipped?
- Which architectural patterns are documented but have no code enforcing them?

A human reading all 30 docs can answer these. A tool cannot. The answers drift
silently as docs and code evolve independently.

### 3. Cross-doc dependencies are prose

The [studio-plan.md](architecture/studio-plan.md) says "Plans absorbed" and lists
which other plan docs feed into it. The [jiang-cheng-zi-plan.md](pieces/jiang-cheng-zi-plan.md)
references tempo-arrangement-plan and velocity-model-plan as upstream dependencies.
The [arrangement-and-voicing.md](architecture/arrangement-and-voicing.md) describes
6 implementation phases that layer on each other.

All of these cross-references are manual. Rename a doc, move a section, and the
links rot. There's a documented convention for maintaining cross-doc links — which
itself proves the problem: if it needs a convention, it's fragile.

### 4. Status tracking is manual and per-doc

Each plan doc has a status header. The roadmap has shipped/queued tiers. Keeping
them in sync is a documented rule:

> **Maintenance rule**: when a feature ships, move its row from "Queued" to
> "Shipped" and update the linked plan doc's status header in the same change.

This is exactly the kind of human-maintained state synchronization that drifts.
It's already visible: the MXL import plan shows phases 0 through 1g as done, but
the roadmap doesn't reflect all of these granular completions.

### 5. The agent delegation problem is a PM problem

The [agent-delegation-retrospective.md](meta/agent-delegation-retrospective.md) is
remarkable. It documents a failure where a delegated AI agent replaced 28 songs with
placeholders and deleted 271 tests — because the prompt didn't explicitly forbid it.

The core lesson: "Sub-agents are tools for mechanical execution, not collaborators
for design-sensitive work." But this is fundamentally a **spec coverage problem**.
If the specs had been structural (not prose), and if the traceability between specs
and code had been compiled (not manual), the agent's deviation would have been
caught structurally:

- "Spec X says 28 songs exist. Only 1 class implements `@ForFeature(X)` now."
  Build failure.
- "Test count dropped below threshold." Build failure — if the threshold were
  encoded in the graph, not just a mental note.

## What Jarista Would Look Like Here

### The spec graph

```java
// L1 — project-level goals
class MusicAsCode implements L1_Spec {}
class SmartAccompaniment implements L1_Spec {}
class ExternalImport implements L1_Spec {}

// L2 — feature areas
class ExpressionModel implements L2_Spec<SmartAccompaniment> {
    public static final ExpressionModel INSTANCE = new ExpressionModel();
    Set<Stateless> dependencies() {
        return Set.of(SideChannelDoctrine.INSTANCE);
    }
}

class AutoDrum implements L2_Spec<SmartAccompaniment> {
    public static final AutoDrum INSTANCE = new AutoDrum();
}

class MxlImport implements L2_Spec<ExternalImport> {
    public static final MxlImport INSTANCE = new MxlImport();
}

class VoiceSeparation implements L2_Spec<ExternalImport> {
    public static final VoiceSeparation INSTANCE = new VoiceSeparation();
}

// L3 — specific features
class PitchExpression implements L3_Spec<ExpressionModel> {
    public static final PitchExpression INSTANCE = new PitchExpression();
    Set<Stateless> dependencies() {
        return Set.of(SideChannelDoctrine.INSTANCE);
    }
}

class TempoArrangement implements L3_Spec<ExpressionModel> {
    public static final TempoArrangement INSTANCE = new TempoArrangement();
}

class AutoPedal implements L3_Spec<ExpressionModel> {
    public static final AutoPedal INSTANCE = new AutoPedal();
}

class VelocityModel implements L3_Spec<ExpressionModel> {
    public static final VelocityModel INSTANCE = new VelocityModel();
}
```

### @ForFeature on implementation code

```java
@ForFeature(AutoPedal.class)
public class AutoPedaling { ... }

@ForFeature(AutoPedal.class)
public class PedalInjector { ... }

@ForFeature(VelocityModel.class)
public record Velocities(...) { ... }

@ForFeature(MxlImport.class)
public class MusicXmlParser { ... }

@ForFeature(VoiceSeparation.class)
public class PitchBandSplitter { ... }
```

Now you can ask:
- **"What implements MxlImport?"** — scan for `@ForFeature(MxlImport.class)`. Get
  the full list of implementing classes.
- **"What specs have no implementation?"** — PitchExpression and TempoArrangement
  have no `@ForFeature` references. They're queued, not shipped.
- **"What would be affected by changing the ExpressionModel spec?"** — all L3 specs
  under it, plus all `@ForFeature`-annotated code for each.

### Pieces for the Jiang Cheng Zi work

```java
// Root piece — this song needs to be built
class ImplJiangChengZi implements L1_Piece<JiangChengZiSpec> {
    public static final ImplJiangChengZi INSTANCE = new ImplJiangChengZi();
}

// Phase A — pitch expression infrastructure
class ImplPitchExpression implements L1_Piece<PitchExpression> {
    public static final ImplPitchExpression INSTANCE = new ImplPitchExpression();
}

// Phase B — variable tempo infrastructure
class ImplTempoArrangement implements L1_Piece<TempoArrangement> {
    public static final ImplTempoArrangement INSTANCE = new ImplTempoArrangement();
}

// Phase C — module bootstrap
class BootstrapChinaFolk implements L1_Piece<CollectionsArchitecture> {
    public static final BootstrapChinaFolk INSTANCE = new BootstrapChinaFolk();
}
```

The dependency "Jiang Cheng Zi is blocked" is now structural — its spec (`JiangChengZiSpec`)
depends on `PitchExpression` and `TempoArrangement`, which have no fulfilling code yet.
No prose needed. The graph says it.

### What the AI can now answer

With the graph compiled and the `StateStore` populated:

- **"What's the status of the expression model?"** → 2 of 4 L3 specs fulfilled
  (AutoPedal: done, VelocityModel: done, PitchExpression: no pieces active,
  TempoArrangement: no pieces active).
- **"What blocks the Jiang Cheng Zi song?"** → PitchExpression and TempoArrangement
  specs have no fulfilling implementation code.
- **"What shipped recently?"** → Freshness signals on pieces marked done in the
  state store.
- **"What's stale?"** → Pieces with no status change beyond the freshness threshold.

## What Would Change in Practice

### Before Jarista (current state)

1. Developer reads `roadmap.md` to figure out what's next
2. Reads the linked plan doc to understand the design
3. Mentally traces dependencies to other plan docs
4. Works on the code
5. Updates the plan doc status header
6. Updates the roadmap
7. Hopes they didn't miss a cross-reference

### After Jarista

1. Developer queries the graph: "What's unblocked and has no active pieces?"
2. The graph shows the spec, its dependencies (all fulfilled), and its depth
3. Developer creates a Piece class (3 lines), starts working
4. `@ForFeature` annotation links code to spec automatically
5. Status updates go to the StateStore — one click in the UI, or a CLI command
6. The roadmap IS the graph. There is no separate document to maintain.

### The big win for this specific project

This project's documentation is already better than 95% of software projects.
The pain isn't "we have no plan" — it's "the plan is manually maintained prose
that requires human effort to keep synchronized with reality."

Jarista would turn the existing 30+ plan documents into **compiled, queryable,
type-checked structure** — while keeping the design prose as documentation
alongside the nodes, not as the source of truth.

The agent delegation problem goes away structurally. An AI agent working on the
codebase would operate against compiled specs. If it deletes code that fulfills a
spec, the `@ForFeature` reference count drops. If it stubs song content, the piece
tree's status signals a regression. The build catches what prose conventions cannot.

## What Wouldn't Change

- The quality of the design documents. Jarista doesn't replace good writing.
- The solo-developer workflow. Jarista adds value even for a team of one — the
  graph is queryable by AI tools, which is the primary "stakeholder" interface.
- The Maven module structure. `.jarista/` sits alongside existing modules.
- The arrangement cookbook. The cookbook is authoring guidance, not PM structure.

## Honest Assessment

| Aspect | Benefit for this project |
|---|---|
| Compiled spec graph | **High** — 30+ plan docs become navigable structure |
| `@ForFeature` traceability | **High** — "what implements this?" is currently grep |
| Piece breakdown | **Medium** — solo developer knows what they're working on, but AI query value is real |
| Cross-team proxy nodes | **Not applicable** — solo project |
| Status tracking via StateStore | **Medium** — eliminates manual roadmap/plan-doc sync |
| Freshness protocol | **Low** — solo developer knows what's stale |
| Stakeholder view | **Not applicable yet** — but becomes relevant if business-thinking angles are pursued |

The highest-value Jarista features for this project are the **spec graph** and
**@ForFeature traceability** — turning the existing rich documentation into
compiled, queryable, IDE-navigable structure. The operational features (pieces,
status, freshness) add less value for a solo project but become significant if
the business directions in [business-thinking.md](meta/business-thinking.md)
lead to a team.
