class MinCostMaxFlow extends Graph {
  long[] flow, capa, cost, pot;
  long totalFlow, totalCost;

  MinCostMaxFlow(int nNodes, int nEdges) {
    super(nNodes, nEdges);

    flow = new long[2 * m];
    capa = new long[2 * m];
    cost = new long[2 * m];
    pot = new long[2 * m];
  }

  long resCapa(int e) {
    return capa[e] - flow[e];
  }

  long resCost(int e) {
    return pot[etail(e)] + cost[e] - pot[ehead[e]];
  }

  int addEdge(int u, int v, long ca, long co) {
    int e = super.addEdge(u, v);
    capa[e] = ca;
    cost[e] = co;
    cost[einv(e)] = -co;
    return e;
  }

  long ssp(int source, int sink) {
    while (true) {
      ShortestPath sp = new ShortestPath(this, e -> {
        if (resCapa(e) == 0) return Long.MAX_VALUE;
        return resCost(e);
      });
      sp.dijkstra(source);
      if (sp.dist[sink] == Long.MAX_VALUE) break;

      long delta = Long.MAX_VALUE;
      for (int e = sp.ent[sink]; e != -1; e = sp.ent[etail(e)]) {
        delta = Math.min(delta, resCapa(e));
      }
      for (int e = sp.ent[sink]; e != -1; e = sp.ent[etail(e)]) {
        flow[e] += delta;
        flow[einv(e)] -= delta;
        totalCost += cost[e] * delta;
      }
      totalFlow += delta;

      for (int u = 0; u < n; u++)
        if (sp.dist[u] != Long.MAX_VALUE)
          pot[u] += sp.dist[u];
    }
    return totalCost;
  }
}
