# Jarista Guide

**Graph-native project management for Java.**
For humans and AI agents building or maintaining Jarista feature graphs.

---

## What Jarista Is

Jarista models a project as a **typed, compiled, validated graph** of specs and pieces.
No YAML. No dashboards. No tickets. The project structure lives in Java source code,
validated by the compiler and the graph validator on every build.

- **Spec** = what needs to exist (a feature, a study, a capability)
- **Piece** = evidence that work happened (an implementation, a deliverable)
- **Detail** = typed content attached to any node (prose, code, diagrams, equations)

The graph is not a plan. It has no timeline, no velocity, no blame.
It reveals structure: what exists, what depends on what, what is done.

---

## Project Setup

A Jarista project is a multi-module Maven project:

```
my-project/
  pom.xml                          # parent POM, declares 3 modules
  my-project-spec/                 # specs: what the project IS
    pom.xml
    src/main/java/.../spec/
  my-project-piece/                # pieces: what has been DONE
    pom.xml
    src/main/java/.../piece/
    src/test/java/                 # graph validation tests live here
  my-project-ui/                   # FX viewer (optional)
    pom.xml
    src/main/java/.../ui/
```

### Parent POM

```xml
<modules>
    <module>my-project-spec</module>
    <module>my-project-piece</module>
    <module>my-project-ui</module>
</modules>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.captainssingapura-hue.jarista</groupId>
            <artifactId>jarista-core</artifactId>
            <version>${jarista.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.captainssingapura-hue.jarista</groupId>
            <artifactId>jarista-graph</artifactId>
            <version>${jarista.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.captainssingapura-hue.jarista</groupId>
            <artifactId>jarista-fx</artifactId>
            <version>${jarista.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Spec module depends on jarista-core + jarista-graph
### Piece module depends on spec module + jarista-core (+ jarista-graph for tests)
### UI module depends on spec + piece + jarista-fx

---

## The Node Hierarchy

### Specs: L1 through L18

Each level is parameterized on its parent:

```java
public interface L1_Spec extends Stateless {}            // root, one per project
public interface L2_Spec<P extends L1_Spec> extends Stateless {}   // epics
public interface L3_Spec<P extends L2_Spec<?>> extends Stateless {} // features
public interface L4_Spec<P extends L3_Spec<?>> extends Stateless {} // sub-features
// ... through L18_Spec
```

The generic parameter **encodes the tree structure** at compile time.
A `L3_Spec<CoreADTs>` is a child of `CoreADTs` by type, not by configuration.

### Pieces: L1 through L8

Pieces are the **worker-facing** side of Jarista. They serve two roles:

1. **Evidence** — a completed piece (`done()=true`) proves work happened
2. **TODO** — an open piece (`done()=false`) is a concrete task to be picked up

Parameterized on the spec they advance:

```java
public class ImplCoreADTs implements L1_Piece<CoreADTs> {
    public static final ImplCoreADTs INSTANCE = new ImplCoreADTs();
    @Override public boolean done() { return true; }
}
```

Every piece inherits three capabilities from `PieceBase`:

- `done()` — is this work complete? (default: false)
- `outOfScope()` — what should the worker NOT touch? (default: empty)
- `verificationTarget()` — how to verify completion? (default: empty)

### Every node must have

```java
public static final MyNode INSTANCE = new MyNode();
```

The graph scanner finds nodes by this convention. Missing INSTANCE = validation error.

---

## LoadBearing vs Organizational

This is the most important design decision for each spec.

### Leaf (LoadBearing)

A spec with **no child specs underneath it**. It IS the deliverable.
Must implement `LoadBearing` and declare its status:

```java
public class PhraseADT implements L3_Spec<CoreADTs>, LoadBearing {
    public static final PhraseADT INSTANCE = new PhraseADT();

    @Override public SpecStatus status() { return SpecStatus.DONE; }
}
```

No default on `status()` — if you declare yourself load-bearing, you must say where you stand.

### Organizational (non-leaf)

A spec that **groups child specs**. Does NOT implement `LoadBearing`.
Its status is derived from descendant leaves (indicative only):

```java
public class CoreADTs implements L2_Spec<NotationGoal> {
    public static final CoreADTs INSTANCE = new CoreADTs();
    // NO LoadBearing, NO status() — derived from children
}
```

Derivation rule:
- All descendant leaves DONE -> DONE
- Any descendant not PLANNED -> IN_PROGRESS
- Otherwise -> PLANNED

### Rules the validator enforces

1. **Pieces can only target LoadBearing specs.** Attaching a piece to an organizational node is a violation.
2. **Status consistency.** If a piece says `done()=true`, its target spec must say `DONE`, and vice-versa.

### How to decide

Ask: **will this spec ever have child specs?**
- Yes -> organizational, no LoadBearing
- No -> leaf, implement LoadBearing, declare status

If you add child specs later, remove LoadBearing from the parent.
It becomes organizational; its status now derives from the new children.

---

## The Detail System

Every node can carry typed content via `details()`.

### The ADT (what the content IS)

```java
sealed interface Detail {
    record Text(String content)                          // prose
    record Code(String language, String content)         // source code
    record Html(String content)                          // HTML/SVG
    record Resource(String path, ResourceType type)      // static file reference
}
```

### Roles (WHY the content exists)

```java
enum DetailRole {
    DESCRIPTION,   // what this node is about
    EXPECTATION,   // acceptance criteria
    RATIONALE,     // why this approach
    DESIGN,        // how it works
    EXAMPLE,       // concrete usage
    REFERENCE,     // links to external material
    PRODUCT,       // deliverable artifact
    NOTE           // transient commentary
}
```

Detail type and role are **orthogonal**. A Text can be a DESCRIPTION or an EXAMPLE.
An SVG Resource can be a DESIGN diagram or a PRODUCT deliverable.

### The Builder DSL

```java
private static final DetailBuilder _db = new DetailBuilder();

@Override
public List<RoledDetail> details() {
    return List.of(
        _db.text()
            .ln("First line of prose.")
            .ln("Second line.")
        .as(DESCRIPTION),

        _db.code("java")
            .ln("public sealed interface Phrase {}")
        .as(EXAMPLE),

        _db.resource("docs/architecture.svg", ResourceType.SVG)
        .as(DESIGN)
    );
}
```

Each `_db.text()...as(ROLE)` produces one immutable `RoledDetail`.
The builder itself is stateless and reusable.

### Two layers of documentation

1. **Skeleton** — typed Java in `details()`. Essential shape. Always compiled, always in sync.
2. **Flesh** — static resources (markdown, SVG, PNG, LaTeX, PDF). Rich detail in the format native to the content. Referenced via `Detail.Resource`.

The skeleton travels with the node. The flesh travels with the module.
Neither rots in a wiki.

---

## SpecStatus and the Living Organism

```java
public enum SpecStatus { PLANNED, IN_PROGRESS, DONE }
```

DONE does not mean "finished forever." Software is a living organism.
Even a DONE spec may grow new sub-features later — at which point it
becomes organizational, sheds its LoadBearing, and derives status from
its new children.

The status answers: **where does this stand today?**
Not: where will it stand on a date someone promised.

---

## Graph Validation

The validator runs on every build. It checks:

| Rule | Violation |
|---|---|
| Every node has `public static final INSTANCE` | `MissingInstance` |
| No circular dependencies | `CyclicDependency` |
| Pieces only target LoadBearing specs | `PieceTargetsNonLeaf` |
| Piece `done()` agrees with spec `status()` | `StatusMismatch` |

### Test pattern

```java
@Test
void graphHasNoViolations() {
    var validator = new GraphValidator();
    List<Violation> violations = validator.validate(
            "my.project.spec", "my.project.piece");
    assertTrue(violations.isEmpty(),
            () -> "Graph has violations:\n" +
                  violations.stream().map(Object::toString)
                          .reduce("", (a, b) -> a + "  - " + b + "\n"));
}
```

---

## Best Practices

### Naming

- **L1 root**: `ProjectGoal`, `NotationGoal`, `JaristaGoal` — the mission statement
- **L2 epics**: noun phrases — `CoreADTs`, `Playback`, `CulturalStudies`
- **L3 features**: specific — `PhraseADT`, `MidiEngine`, `DeliverableFallacy`
- **L4 sub-features**: focused — `VelocityTrap`, `CartoonNanguoSolo`
- **Pieces**: `Impl` + spec name — `ImplCoreADTs`, `ImplMidiEngine`

### When to split levels

- L2 = major capability area (5-10 per project)
- L3 = concrete feature or study (3-8 per L2)
- L4 = sub-aspect when an L3 has distinct parts (0-5 per L3)
- If an L3 has more than 8 children, consider an intermediate L2

### Dependencies

Use `dependencies()` to declare cross-branch relationships:

```java
public Set<Stateless> dependencies() {
    return Set.of(CoreTypeSystem.INSTANCE, GraphValidation.INSTANCE);
}
```

The parent-child relationship is structural (via generics).
Dependencies express additional edges — "this also needs that."

### Details: less is more

The skeleton (typed Java details) should capture the **essential shape**:
- What is this? (DESCRIPTION)
- Why this way? (RATIONALE)
- How does it work? (DESIGN)

Detailed explanations, diagrams, and math go in static resources.
The skeleton should be readable in 30 seconds.

---

## Pieces as TODOs

A piece with `done()=false` is a TODO. Its details are the work order.

### Task decomposition

Multiple pieces per spec decompose work naturally:

```java
// Three tasks for one spec — different levels signal granularity
public class ParseMxl implements L1_Piece<MxlParsing> {
    public static final ParseMxl INSTANCE = new ParseMxl();
    @Override public boolean done() { return true; }   // completed
}

public class TestMxlEdgeCases implements L2_Piece<MxlParsing> {
    public static final TestMxlEdgeCases INSTANCE = new TestMxlEdgeCases();
    @Override public boolean done() { return false; }  // open TODO

    private static final DetailBuilder _db = new DetailBuilder();
    @Override public List<RoledDetail> details() {
        return List.of(
            _db.text().ln("Test all 27 MXL files parse without exception.").as(EXPECTATION),
            _db.text().ln("Focus on malformed voice elements and nested tuplets.").as(DESCRIPTION)
        );
    }
    @Override public List<String> outOfScope() {
        return List.of("Do not modify the parser — tests only");
    }
    @Override public String verificationTarget() {
        return "notation.mxl.MxlParserTest";
    }
}
```

### Scope boundaries

`outOfScope()` tells the worker what NOT to touch — critical for agents:

```java
@Override public List<String> outOfScope() {
    return List.of(
        "Do not modify public API signatures",
        "Stay within the notation-core module",
        "Do not change the parser; only add new strategies"
    );
}
```

### Self-verification

`verificationTarget()` points to how to check the work:

```java
@Override public String verificationTarget() {
    return "mvn verify -pl notation-core";
}
```

Agent completes work → runs verification → confirms green → marks `done()=true` → commits.

### What pieces do NOT carry

- No assignee — git blame tells you who did it
- No due date — the worker works until done
- No priority — dependencies express ordering
- No story points — the piece's details describe scope
- No rich status — done or not done, that's it

This is deliberate. Every field that COULD be used to blame someone IS removed.
The piece helps the worker. The observer gets their view for free — derived, not curated.

---

## For AI Agents

When an AI agent builds or maintains a Jarista graph:

### Building a new graph

1. **Read the project first.** Understand what exists before creating nodes. Scan module structure, read key classes, understand the domain.
2. **Start with L1 + L2.** The root goal and 5-10 major epics. Get the shape right before going deeper.
3. **Add L3 leaves with LoadBearing.** Each feature that is a concrete deliverable. Set `status()` honestly — PLANNED for future work, DONE for shipped work, IN_PROGRESS for active work.
4. **Add details to L2 and key L3 nodes.** DESCRIPTION and RATIONALE at minimum. Not every node needs details — prefer a few well-documented nodes over many empty ones.
5. **Build and test.** The graph validator catches structural errors. Run `mvn install` after each batch of changes.
6. **Pieces as TODOs.** Create pieces with `done()=false` for concrete tasks. Write DESCRIPTION (what to do), EXPECTATION (what done looks like), and set `outOfScope()` to prevent over-reach. When work completes, flip to `done()=true` and commit.

### Working from open pieces (agentic workflow)

1. **Discover.** Scan the graph for pieces with `done()=false`.
2. **Read.** Study the piece's `details()`, `outOfScope()`, and parent spec context.
3. **Act.** Do the work within the stated scope boundaries.
4. **Verify.** Run `verificationTarget()` if provided, or check against EXPECTATION details.
5. **Complete.** Change `done()` to `return true`, commit. The commit IS the evidence.
6. **Validate.** Run `mvn install` to confirm the graph is still consistent.

### Maintaining an existing graph

1. **Don't change status without reason.** PLANNED -> IN_PROGRESS when work begins. IN_PROGRESS -> DONE when the work ships. Never backslide without explanation.
2. **Promote leaves to organizational when they grow.** If you add child specs under an L3, remove its LoadBearing. It's now a branch, not a leaf.
3. **Keep test counts current.** Node counts in tests must match reality. Update after every addition.
4. **Details are living content.** Update them as understanding deepens. A DONE spec with stale details is worse than a PLANNED spec with no details.

### What NOT to do

- Don't create specs for every class or method. Specs are features, not code.
- Don't set everything to DONE. Honest status is more valuable than green dashboards.
- Don't skip details on L2 epics. These are the nodes people read first.
- Don't treat the graph as a plan. It has no dates. It has no velocity. It reveals structure.
- Don't add assignee, due date, priority, or story points to pieces. If a feature could be used to blame someone, it should not exist in Jarista.

---

## Cultural Foundation

Jarista is named for Java + Barista: the one who brews, who transforms
raw material into something with character. Not a factory. A craft.

The graph is built on three rejections:

1. **No YAML.** The project structure is Java. The compiler validates it.
   Configuration files are where type safety goes to die.

2. **No dashboards.** The graph IS the visualization.
   A dashboard is a lossy projection that someone curates for an audience.
   The graph shows everything to everyone equally.

3. **No accountability theater.** The graph cannot blame because it has
   no expectations. It reveals what exists and what is missing.
   Status is declared by the person doing the work, not assigned by
   a manager watching a burndown chart.

Understanding is a deliverable. Culture is a deliverable. Theory is a deliverable.
The graph does not discriminate — if it compiles, it exists.
If it validates, it matters.

结图为谱，南郭立现，虽猫可代之。
Weave the graph into a score, and the pretender is revealed — even a cat could replace them.
