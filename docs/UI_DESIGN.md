# Jarista — UI Design

## Purpose

The UI is a visualization and interaction layer over the Jarista graph.
It does not own data. It reads compiled types and queries the `StateStore`.

## Data Source

### Build Artifact

The Jarista build plugin compiles `.jarista/`, walks the graph via `INSTANCE` references
and `dependencies()` calls, and outputs a structured graph artifact. This artifact contains:

- All spec nodes with their level, parent, and dependency edges
- All piece nodes with their level, parent spec, and sub-piece structure
- All `@ForFeature` links discovered by annotation scanning
- Orphan reports (specs with no pieces, specs with no `@ForFeature`)

The artifact is the source of truth for the UI's structural view.

### State Store

Operational state (status, assignment, priority, freshness) is read from the `StateStore`
interface at runtime. The UI calls the same interface that any other tooling uses.

### Dev Server (Optional)

For development, a file watcher recompiles `.jarista/` on change and regenerates the
artifact. The UI hot-reloads. Same experience as a frontend dev server.

## Audiences

### Stakeholder View

**What they see:**
- Spec graph only — no pieces, no code references
- Specs displayed at their hierarchy level with dependency edges
- Aggregated status per spec — computed from child pieces' states
  (e.g., "3 of 5 pieces done", or a simple progress indicator)
- Dependency health — are upstream specs fulfilled?
- Freshness signals — which specs have stalled work?

**What they can do:**
- Reprioritize specs (writes to externalized state, not Java source)
- Flag specs for attention (writes to externalized state)
- Add comments / context (writes to externalized state)
- Ask AI questions ("What's blocked?", "What changed this week?")

**What they cannot do:**
- Create or restructure specs (structural — requires code workflow)
- Modify piece breakdowns (structural — requires code workflow)

### Developer View

**What they see:**
- Full spec graph with dependency edges
- Piece breakdown per spec, with sub-piece hierarchy
- `@ForFeature` links — click through from spec to annotated code
- Externalized state — status, assignment, freshness per piece
- Orphan detection — specs with no pieces, specs with no implementing code
- Impact analysis — select a spec, see all pieces and annotated code affected

**What they can do:**
- Everything a stakeholder can do, plus:
- Update piece status (writes to externalized state)
- Acknowledge stale pieces (writes to externalized state)
- Navigate from spec → piece → code (read-only, opens in IDE or file viewer)

**What they cannot do from the UI:**
- Create or restructure specs/pieces (use IDE + git workflow)
- Modify `@ForFeature` annotations (those live in source code)

## Views

### 1. Graph Overview

The primary view. Full spec graph rendered as an interactive node-edge diagram.

- Nodes colored by type (spec level, proxy)
- Edges for parent-child and dependency relationships
- Node size or badge indicating aggregated piece status
- Zoom into a spec to expand its piece breakdown
- Filter by level, status, staleness, assignee
- Search by name

### 2. Spec Detail

Drill-down view for a single spec.

- Spec metadata (level, parent, dependencies)
- Piece breakdown as a tree
- Each piece shows externalized state (status, assignee, freshness)
- `@ForFeature` links — list of annotated classes and methods
- Dependency status — are upstream specs fulfilled?
- AI summary — natural language description of current state

### 3. Status Board

Operational view for daily work. Not a Kanban board — no columns to drag cards between.

- List of all pieces grouped by status (open, active, done, blocked, other)
- Sorted by freshness — stalest first
- Quick status update — click to change state
- Filter by spec, assignee, level

### 4. Health Dashboard

Aggregate view for the whole project.

- Spec fulfillment coverage — percentage of specs with linked code
- Piece completion — aggregate progress across specs
- Stale pieces — pieces past freshness threshold without acknowledgment
- Orphan specs — specs with no pieces or no `@ForFeature`
- Cross-team proxy health — are cached remote states current?
- AI-powered summary — "Here's what needs attention"

### 5. AI Query Interface

Natural language interface over the graph.

- "What is the status of the auth epic?"
- "What specs are blocked by cross-team dependencies?"
- "What has gone stale in the last two weeks?"
- "What code would be affected if we change the OAuth flow spec?"
- "Show me unfulfilled specs with no active pieces"

The AI reads the graph artifact + state store. It does not have special access —
it queries the same data the UI views display.

## Tech Stack Considerations

### Non-Prescriptive

Jarista does not mandate a frontend framework. The graph artifact is structured data.
Any frontend can consume it. However, the reference UI should demonstrate best practices.

### Likely Components

- **Graph rendering:** A capable graph visualization library (D3.js, Cytoscape.js,
  React Flow, or similar) for the interactive spec graph
- **State management:** The UI is largely read-heavy with occasional state writes.
  No complex client state needed.
- **Real-time updates:** WebSocket or SSE from the dev server for live reload.
  Polling is acceptable for the deployed version reading from a state store.

## Design Principles

1. **Read-heavy, write-light.** The UI is primarily a viewer. State changes are infrequent
   and go through `StateStore`.

2. **No ceremony.** Status updates should be one click. No modals, no forms, no
   confirmation dialogs for operational changes.

3. **Information density over whitespace.** Developers want to see the whole picture.
   Stakeholders want a clean summary. Both want density appropriate to their needs,
   not gratuitous padding.

4. **The graph is the navigation.** Don't hide the graph behind menus. The graph IS
   the interface. Click a node, see its details. Follow an edge, see the dependency.

5. **No notification spam.** Freshness signals are visible in the UI. They are not
   push notifications, emails, or Slack messages unless the team explicitly configures
   an integration.

## Write-Back Architecture

```
┌─────────────┐     reads      ┌──────────────────┐
│             │ ◄────────────── │                  │
│  Graph      │                 │      UI          │
│  Artifact   │                 │                  │
│  (compiled) │                 │                  │
│             │     writes      │                  │
└─────────────┘     ──────────► └──────────────────┘
                                        │
                                        │ writes
                                        ▼
                                ┌──────────────────┐
                                │                  │
                                │   StateStore     │
                                │   (externalized) │
                                │                  │
                                └──────────────────┘
```

The UI reads structure from the graph artifact (immutable, from the build).
The UI reads and writes operational state via the `StateStore` interface.
The UI never modifies Java source files.
