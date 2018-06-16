class MinCostMatching {
  int nl, nr;
  MinCostMaxFlow mcmf;

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

  MinCostMatching(int nLNodes, int nRNodes, int nEdges) {
    mcmf = new MinCostMaxFlow(nLNodes + nRNodes + 2, nLNodes + nRNodes + nEdges);
    nl = nLNodes;
    nr = nRNodes;

    for (int u = 0; u < nl; u++) mcmf.addEdge(source(), lnode(u), 1, 0);
    for (int v = 0; v < nr; v++) mcmf.addEdge(rnode(v), sink(), 1, 0);
  }

  int addEdge(int u, int v, long c) {
    return mcmf.addEdge(lnode(u), rnode(v), 1, c);
  }

  long hungarian() {
    return mcmf.ssp(source(), sink());
  }
}
