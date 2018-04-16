import java.util.Arrays;

class TwoSat {
  int nVar;
  int[] value;
  Graph g;

  TwoSat(int nVar, int nClause) {
    this.nVar = nVar;
    value = new int[nVar];
    g = new Graph(nVar * 2, nClause * 2);
    Arrays.fill(value, -1);
  }

  void addClause(int u, int v) {
    g.addEdge(u^1, v);
    g.addEdge(v^1, u);
  }

  boolean run() {
    SCC scc = new SCC(g);
    scc.run();
    for (int i = 0; i < nVar; i++) {
      if (scc.comp[i*2] == scc.comp[i*2+1])
        return false;
    }

    for (int i = g.n-1; i >= 0; i--) {
      int u = scc.topo[i];
      if (value[u/2] != -1) continue;
      value[u/2] = u%2;
    }
    return true;
  }
}
