# Jarista — Open Design Gaps

## Status: Active

Each gap is categorized by severity and current resolution state.

---

## 1. Externalized State Storage — Concrete Implementation

**Severity:** High — this is the keystone for daily usage
**Status:** Interface designed, no reference implementation

The `StateStore` interface is defined. But teams adopting Jarista need at least one
working implementation out of the box. Without it, adoption requires building your own
state backend before you can use Jarista at all.

**What needs to happen:**
- Ship at least one reference implementation (likely file-based, git-friendly)
- Design the state file format — must handle concurrent updates without constant merge conflicts
- Consider a git-compatible approach: one state file per piece (like one class per piece)
  to minimize conflicts

**The tension:**
Frequent state changes (status updates, assignment) don't want to live in git.
But the design principle says everything travels with the code.
The reference implementation needs to resolve this tension or explicitly choose a side.

---

## 2. Proxy Node Sync Mechanism

**Severity:** High — core idea #3 has no implementation path yet
**Status:** Concept only

A proxy node references a spec in another team's graph (another git repo). The design
says it "caches remote state and syncs on demand." The mechanics are undefined.

**Open questions:**
- How does a proxy discover the remote graph? Git clone? Published artifact? API?
- What exactly is cached? The full spec class? A compiled summary? Just status?
- What happens when the remote spec changes shape (renamed, restructured, deleted)?
- How is the cache updated? Git hook? Manual CLI command? Build plugin?
- What happens when the remote repo is unreachable? Stale cache policy?
- Can a proxy reference a proxy (transitive cross-team dependency)?

**Possible approaches:**
- Git submodule pointing to remote `.jarista/` — heavy, version-locked
- Published graph artifact (Maven/Gradle dependency) — versioned, decoupled
- Lightweight manifest exchange — each team publishes a summary of their public specs

---

## 3. Piece Creation Friction

**Severity:** Medium — daily usability concern
**Status:** Acknowledged, mitigation identified

Creating a piece means creating a Java source file, writing 3 lines, compiling, committing.
This is more friction than typing a title in Jira. For the most frequent PM operation,
this matters.

**Mitigation:**
- CLI generator: `jarista piece "Write callback handler" --parent ImplTokenExchange`
- IDE plugin with a quick-action / template
- The class is genuinely minimal (3 lines) so the friction is bounded

**Residual risk:**
Developers may resist adopting a workflow that requires a compile cycle to log a task.
Need to validate with real usage whether this friction is tolerable or a dealbreaker.

---

## 4. Stakeholder Write-Back

**Severity:** Medium — affects the "direct access" promise
**Status:** Partially resolved

Stakeholders can read the graph via the UI (filtered spec-level view). But writing
back — reprioritizing, flagging, commenting — needs a path from UI action to persistent
state change.

**Resolved for operational state:**
Priority, flags, comments are externalized. The UI writes to the `StateStore`. No Java
source changes needed. This is clean.

**Unresolved for structural changes:**
If a stakeholder wants to add a new spec, restructure the hierarchy, or remove a spec,
that requires Java source changes. Options:
- The UI generates a PR on behalf of the stakeholder (feasible, adds git/GitHub dependency)
- Structural changes are always developer-mediated (simpler, but reintroduces a translation layer)
- Accept that structural changes are infrequent enough that developer mediation is fine

---

## 5. Merge Conflicts at Scale

**Severity:** Medium — team-size dependent
**Status:** Acknowledged

Two developers on different branches adding pieces under the same parent spec will not
conflict on file creation (different files). But any aggregation point — a parent piece's
file, a graph index, a state file — could conflict.

**Mitigations:**
- One file per piece / one state file per piece — minimizes overlap
- No central registry file — graph is discovered by scanning, not indexed
- Piece classes have no references to their children (children reference parents) —
  adding a sibling piece never touches the parent file

**Residual risk:**
Teams of 20+ working on the same module may still hit conflicts in the state layer.
The `StateStore` implementation design must account for this.

---

## 6. Circular Dependency Detection

**Severity:** Low — important but straightforward
**Status:** Designed, not implemented

Spec-level dependencies declared via `dependencies()` could form cycles. This must be
detected at build time.

**Approach:**
- Graph walk during build plugin execution
- Standard cycle detection algorithm (DFS with coloring)
- Compile error or build failure on cycle detection
- Report the full cycle path for debugging

---

## 7. Build-Time INSTANCE Enforcement

**Severity:** Low — important but straightforward
**Status:** Designed, not implemented

Every class implementing `Stateless` must have a `public static final INSTANCE` field
of its own type.

**Approach:**
- Annotation processor or build plugin scans all `Stateless` implementors
- Validates field existence, type, and modifiers
- Build failure with clear error message on violation

---

## 8. Freshness Protocol Details

**Severity:** Low — concept is clear, details are not
**Status:** Concept only

Stale pieces must be explicitly acknowledged. Open questions:
- What defines "stale"? Time since last status change? Time since last git activity on
  related code? Configurable threshold?
- Who gets nagged? The assignee (from externalized state)? The piece creator?
  The spec owner? What if assignment is empty?
- Where does the acknowledgment live? In the `StateStore`?
- What does acknowledgment look like? A status change? A dedicated field?
- Does freshness cascade? If a parent piece is stale, are all sub-pieces stale?

---

## 9. Cross-Language @ForFeature Equivalent

**Severity:** Low — Java-first, others later
**Status:** Concept only

Non-JVM languages use a comment convention: `// @jarista:fulfills(oauth-flow)`.
This needs:
- A CLI scanner that parses comments across language syntaxes
- String-based references (no compile-time safety)
- A reconciliation step between string refs and the compiled Java graph
- Error reporting for broken string references

This is lower priority — Jarista is for Java developers first. But the design should
not preclude this path.
