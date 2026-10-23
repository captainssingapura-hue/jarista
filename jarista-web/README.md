# jarista-web

A web workspace for Jarista feature graphs, built on the **Homing** framework.
It scans the compiled `jarista.project` graph at boot, adapts it into a Homing
`ContentTree`, and serves it as an expand/collapse **Navigator** tree; clicking a
node renders its typed details (description, rationale, code, and the SVG/HTML
cartoons) inline as a document — the rendering the JavaFX and TUI viewers punt to
"the web UI."

## Run

```
mvn -pl jarista-web exec:java
```

Then open <http://localhost:8083> and choose **Jarista Feature Graph**.
(If you changed upstream modules, run `mvn -q -DskipTests install` first so the
server picks up fresh `jarista-project` classes.)

## Prerequisite — Homing is local-only

This module depends on the Homing framework (version set by `homing.core.version`
in the root `pom.xml`, currently **`0.8.3`**), which is **not published to Maven
Central**. Those jars must already be installed in your local `~/.m2` (they are on
the original dev machine, from building the Homing repo). On a machine without them,
`jarista-web` will not resolve its dependencies — build/install the Homing repo
first, or skip this module (`mvn -pl '!jarista-web' ...`). The other five Jarista
modules have no Homing dependency. When Homing is republished under a new version,
bump `homing.core.version` and recompile.

## How it fits together

| File | Role |
|------|------|
| `JaristaStudioServer` | `main` — bootstraps a single-studio umbrella on port 8083 |
| `JaristaFixtures` | delegates to `StudioStarterFixtures`; overrides `trees()` to surface the Jarista tree |
| `JaristaStudio` / `JaristaHome` | the studio bundle + home catalogue (intro, tree-app tile, nested Feature Graph catalogue) |
| `GraphNode` | the one reflective scan of `jarista.project` into a neutral node tree |
| `JaristaRigidTree` | maps `GraphNode` → a foldable `RigidDocV2` ("bring your own tree") — the browsable graph |
| `DetailToDoc` | maps a node's `List<RoledDetail>` → Homing segments (a `ComposedDoc`, or a rigid node body) |

In the rigid tree every node — branch or leaf — has its own body, so a spec's prose
renders on the spec itself; nodes are addressed by name-path
(`jarista/jaristagoal/…`), so links survive reordering.

## How the graph is browsed

The "Jarista Feature Graph" tile opens the `RigidDocV2`, rendered by `DocTreeViewer`
as a foldable tree with inline details. The same doc opens inside the Studio
Workspace (`?app=genericWorkspace&ws_kind=studio`) in its DocContent pane; in the
workspace's catalogue Navigator it appears as a single leaf, not an expandable
subtree.

Why not mirror the graph into nested sub-catalogues so the Navigator expands it?
Homing 0.8.x keys a catalogue's navigation identity by its **class** and forbids one
identity at more than one position (RFC 0053, Law 1). An 80-node graph would need
one class per node; a data-driven, many-instances-per-class mirror is rejected at
boot. The foldable RigidDoc is the framework's shape for bringing your own tree —
the same choice the reference demo makes with its `DemoContentTreeDoc`.

Homing 0.8.x replaced the earlier `ContentTree`/`TreeAppHost` app with this RigidTree
model (`homing-rigid-tree`); its `TreeLevel.L0–L18` mirrors Jarista's own L0–L18.

