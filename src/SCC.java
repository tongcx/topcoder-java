import java.util.Arrays;

class SCC extends Graph {
  // node arrays
  int[] comp; // comp[u] = root
  boolean[] mark;
  // list of finished nodes
  int[] fin;
  int fidx;
  // list of nodes in topological order
  int[] topo;
  int tidx;

  SCC(int nNodes, int nEdges) {
    super(nNodes, nEdges);
    comp = new int[n];
    mark = new boolean[n];
    fin = new int[n];
    topo = new int[n];
    Arrays.fill(comp, -1);
  }

  void dfs(int u) {
    if (mark[u]) return;
    mark[u] = true;
    for (int e = elast[u]; e != -1; e = eprev[e]) {
      if (isRev(e)) continue;
      int v = ehead[e];
      dfs(v);
    }
    fin[fidx++] = u;
  }

  void assign(int u, int root) {
    if (comp[u] != -1) return;
    comp[u] = root;
    topo[tidx++] = u;
    for (int e = elast[u]; e != -1; e = eprev[e]) {
      if (!isRev(e)) continue;
      int v = ehead[e];
      assign(v, root);
    }
  }

  void kosaraju() {
    for (int u = 0; u < n; u++) {
      dfs(u);
    }
    for (int i = n - 1; i >= 0; i--) {
      int u = fin[i];
      assign(u, u);
    }
  }
}
