class Matching {
  int nl, nr;
  MaxFlow mf;

  int lnode(int u) {
    return u;
  }

  int rnode(int v) {
    return nl + v;
  }

  int source() {
    return nl + nr;
  }

  int sink() {
    return source() + 1;
  }

  Matching(int nLNodes, int nRNodes, int nEdges) {
    mf = new MaxFlow(nLNodes + nRNodes + 2, nLNodes + nRNodes + nEdges);
    nl = nLNodes;
    nr = nRNodes;

    for (int u = 0; u < nl; u++) mf.addEdge(source(), lnode(u), 1);
    for (int v = 0; v < nr; v++) mf.addEdge(rnode(v), sink(), 1);
  }

  int addEdge(int u, int v) {
    return mf.addEdge(lnode(u), rnode(v), 1);
  }

  long hopcroftKarp() {
    return mf.dinic(source(), sink());
  }
}
