import java.util.Arrays;

class SCC {
  Graph g;
  // node arrays
  int[] comp; // comp[u] = root
  boolean[] mark;
  // list of finished nodes
  int[] fin;
  int fidx;
  // list of nodes in topological order
  int[] topo;
  int tidx;

  SCC(Graph g) {
    this.g = g;
    comp = new int[g.n];
    mark = new boolean[g.n];
    fin = new int[g.n]; fidx = 0;
    topo = new int[g.n]; tidx = 0;
    Arrays.fill(comp, -1);
  }

  void dfs(int u) {
    if (mark[u]) return;
    mark[u] = true;
    for (int e = g.elast[u]; e != -1; e = g.eprev[e]) {
      if (g.isRev(e)) continue;
      int v = g.ehead[e];
      dfs(v);
    }
    fin[fidx++] = u;
  }

  void assign(int u, int root) {
    if (comp[u] != -1) return;
    comp[u] = root;
    topo[tidx++] = u;
    for (int e = g.elast[u]; e != -1; e = g.eprev[e]) {
      if (!g.isRev(e)) continue;
      int v = g.ehead[e];
      assign(v, root);
    }
  }

  void kosaraju() {
    for (int u = 0; u < g.n; u++) {
      dfs(u);
    }
    for (int i = g.n - 1; i >= 0; i--) {
      int u = fin[i];
      assign(u, u);
    }
  }
}
