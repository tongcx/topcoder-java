class TwoSat {
  int nVar;
  Graph g;

  int node(int i, boolean pos) {
    return i * 2 + (pos ? 1 : 0);
  }

  TwoSat(int nVar, int nClause) {
    this.nVar = nVar;
    g = new Graph(nVar * 2, nClause * 2);
  }

  void addClause(int i, boolean ipos, int j, boolean jpos) {
    g.addEdge(node(i, !ipos), node(j, jpos));
    g.addEdge(node(j, !jpos), node(i, ipos));
  }

  boolean run() {
    SCC scc = new SCC(g);
    scc.run();
    for (int i = 0; i < nVar; i++) {
      if (scc.comp[node(i, true)] == scc.comp[node(i, false)])
        return false;
    }
    return true;
  }
}
