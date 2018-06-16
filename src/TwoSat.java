import java.util.Arrays;

class TwoSat {
  int nVar;
  int[] value;
  SCC scc;

  TwoSat(int nVar, int nClause) {
    this.nVar = nVar;
    value = new int[nVar];
    scc = new SCC(nVar * 2, nClause * 2);
    Arrays.fill(value, -1);
  }

  void addClause(int u, int v) {
    scc.addEdge(u^1, v);
    scc.addEdge(v^1, u);
  }

  boolean aspvall() {
    scc.kosaraju();
    for (int i = 0; i < nVar; i++) {
      if (scc.comp[i*2] == scc.comp[i*2+1])
        return false;
    }

    for (int i = scc.n-1; i >= 0; i--) {
      int u = scc.topo[i];
      if (value[u/2] != -1) continue;
      value[u/2] = u%2;
    }
    return true;
  }
}
