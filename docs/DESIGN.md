# Jarista — Design Document

## Core Principle

Make project management easy and relaxed. Make professional project managers obsolete.

## Founding Document

See [The Agile Salvation](MANIFESTO.md).

## Four Core Ideas

### 1. Codified Multi-Level Spec/Task Graph

Specifications and tasks are **stateless Java classes**. The graph is expressed purely through
the type system — parent-child relationships via generics, dependencies via method return values.

A pre-built skeleton of 18 levels provides structural scaffolding:

```java
interface Stateless {}

interface L1_Spec extends Stateless {}
interface L2_Spec<P extends L1_Spec> extends Stateless {}
interface L3_Spec<P extends L2_Spec<?>> extends Stateless {}
// ... up to L18
```

Teams choose how deep to go. The level interfaces are structural — class names carry the meaning:

```java
class ProjectGoal implements L1_Spec {}
class AuthEpic implements L2_Spec<ProjectGoal> {}
class OAuthFlow implements L3_Spec<AuthEpic> {}
class TokenHandling implements L4_Spec<OAuthFlow> {}
```

Wrong parent type → compile error. Skipped level → compile error.

### 2. PM and Code as Siblings

The graph lives in `.jarista/` as a source root next to `src/`. It compiles with the project.
It branches, merges, and is reviewed in PRs alongside code. The plan cannot drift from the
code because it is subject to the same version control discipline.

```
my-service/
├── src/           ← implementation code
├── .jarista/      ← spec graph (compiled source)
└── pom.xml
```

### 3. Proxy Nodes for Cross-Team Collaboration

A dependency on another team's spec is a proxy node in your graph that references a node
in their graph. No shared server, no shared database, no shared process. Just a typed
reference that caches remote state and syncs on demand.

### 4. Spec Annotations on Implementation Code

Specs link to implementation code via a simple annotation:

```java
@ForFeature(OAuthFlow.class)
public class TokenRefreshService {

    @ForFeature(TokenExpiryHandling.class)
    public Token refresh(Token expired) { ... }
}
```

Because the annotation references a compiled class (not a string), broken links are
compile errors. IDE navigation, refactoring, and find-usages work out of the box.

## Data Model

### Spec Nodes Are Stateless Java Classes

A spec node is a class with:
- No mutable state
- No runtime behavior
- A `public static final INSTANCE` field (enforced by build-time scan)
- A `dependencies()` method returning other spec instances

```java
class OAuthFlow implements L3_Spec<AuthEpic> {
    public static final OAuthFlow INSTANCE = new OAuthFlow();

    Set<Stateless> dependencies() {
        return Set.of(UserStorage.INSTANCE, SessionManagement.INSTANCE);
    }
}
```

### Graph Relationships

| Relationship | Mechanism | Validation |
|---|---|---|
| Parent-child (spec hierarchy) | Generics on level interfaces | Compiler — type bounds |
| Dependencies between specs | `dependencies()` method | Compiler — class must exist |
| Spec → implementation code | `@ForFeature(Spec.class)` annotation | Compiler — class must exist |
| Cross-team dependency | Proxy node referencing remote graph | Build-time sync + cache |

### Graph Traversal

No reflection. No framework. Just method calls:

```java
var spec = OAuthFlow.INSTANCE;
for (var dep : spec.dependencies()) {
    // dep's class IS the node identity
    // dep.dependencies() gives the next level
    // walk the graph with plain Java
}
```

### Build-Time Enforcement

- Every `Stateless` class must have `public static final INSTANCE`
- `@ForFeature` must reference an existing spec class
- Orphan detection: specs with no `@ForFeature` = unimplemented
- Staleness detection: specs modified without corresponding code changes
- Circular dependency detection: graph walk at compile time

## Pieces — Work Breakdown

### Pieces Are Stateless Java Classes

A Piece is a unit of work toward fulfilling a spec. Like specs, pieces are stateless Java
classes. Unlike specs, pieces have **no dependencies** — dependency is tracked at the spec
level. A root piece is blocked if its spec depends on an unfulfilled spec. Any sub-level
dependency is either fake or needs to bubble up to the spec graph.

Pieces use the same level skeleton pattern as specs, with less depth:

```java
interface L1_Piece<S extends Stateless> extends Stateless {}
interface L2_Piece<P extends L1_Piece<?>> extends Stateless {}
interface L3_Piece<P extends L2_Piece<?>> extends Stateless {}
// ... up to L6 or L8
```

```java
class ImplOAuth implements L1_Piece<OAuthFlow> {
    public static final ImplOAuth INSTANCE = new ImplOAuth();
}

class WriteCallback implements L2_Piece<ImplOAuth> {
    public static final WriteCallback INSTANCE = new WriteCallback();
}
```

A deep piece tree is a smell that the spec graph is too coarse. The two hierarchies
keep each other honest.

### No Piece-Level Dependencies

If a piece appears to have a dependency, one of two things is true:
1. The dependency is actually between specs — it belongs in the spec graph.
2. The dependency is sequencing within the same spec — it lives in the developer's head,
   not in formal structure.

This eliminates the Jira busywork of "link this ticket as blocked by that ticket."

## Externalized State

### The Piece Is the Token

Pieces carry no state. Status, assignment, priority, freshness — all externalized behind
standard interfaces. The piece is the lookup key. The state is an immutable value object
returned by a store.

```java
interface StateStore {
    PieceStatus statusOf(Stateless piece);
    Optional<String> assigneeOf(Stateless piece);
    Freshness freshnessOf(Stateless piece);
}
```

### Status as ADT with Freeform Fallback

```java
sealed interface PieceStatus {
    record Open() implements PieceStatus {}
    record Active() implements PieceStatus {}
    record Done() implements PieceStatus {}
    record Blocked(Stateless blockedBy) implements PieceStatus {}
    record Other(String description) implements PieceStatus {}
}
```

### Implementation Is Not Jarista's Concern

Jarista ships the types (specs, pieces, levels) and the interfaces (StateStore and friends).
It does not ship the state storage implementation. That is the team's choice:

- Database
- Flat file
- Code
- Blockchain
- Punched holes

Multiple state stores can coexist and compose. Status from a database, freshness computed
from git history, assignment from LDAP.

### Structural vs. Operational

| Change type | Where it lives | How it changes |
|---|---|---|
| Spec structure | Java source in `.jarista/` | Branch, commit, PR, compile |
| Piece structure | Java source in `.jarista/` | Branch, commit, PR, compile |
| Status, assignment, priority | Externalized state store | Runtime, via StateStore interface |
| `@ForFeature` links | Java source in `src/` | Normal code workflow |

## UI Layer

### Build Artifact as Data Source

The Jarista build plugin compiles `.jarista/`, walks the graph, and outputs a structured
graph artifact. The UI reads this artifact. A dev server with live reload provides real-time
feedback during development.

### Dual-Audience Views

- **Stakeholder view:** Spec-level only. Status aggregated from pieces. No Java, no code.
- **Developer view:** Full depth. Specs, pieces, `@ForFeature` links, freshness signals.

Same graph, same artifact, different filtered projections.

### Write-Back

Structural changes (spec/piece creation, restructuring) go through the normal code workflow.
Operational changes (status, priority, assignment) write to the externalized state store
via the same `StateStore` interface. The UI does not modify Java source files.

## Architecture Summary

Jarista is three things:

1. **Compiled types** — Specs, Pieces, levels, `@ForFeature`. The structure. Immutable. In git.
2. **Interfaces** — `StateStore` and friends. The contract for mutable state.
3. **Implementations** — Whatever you want. Not shipped by Jarista.

## Design Stances

- **No YAML, no JSON, no config files.** The graph is Java source code.
- **No Gantt charts, no velocity, no burndowns.** These are accountability theater.
- **No fixed hierarchy names.** L1-L18 are structural; teams name their own classes.
- **No runtime framework.** Jarista is a library + build plugin + annotation set.
- **Stakeholders interact with the top of the graph directly.** No PM intermediary.
- **AI computes status from the graph.** No manual reporting.
- **Stale tasks must be explicitly acknowledged.** Freshness protocol.

## Cross-Language Support

- **JVM languages (Kotlin, Scala):** Native — use the same interfaces and annotations.
- **Other languages:** Comment convention (`// @jarista:fulfills(oauth-flow)`) + CLI scanner.
  Less type-safe but still queryable. The graph doesn't care how the link was declared.

## Traceability Loop

```
1. Spec defines WHAT        →  class OAuthFlow implements L3_Spec<AuthEpic>
2. Spec declares DEPENDS ON  →  dependencies() returns other spec instances
3. Code links back to spec   →  @ForFeature(OAuthFlow.class)
4. Everything compiles        →  broken links are build failures
```

Four Java constructs. No framework. No config. No ceremony.
