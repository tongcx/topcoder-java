import java.util.*;

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
    while (dijkstra(source, sink));
    return totalCost;
  }

  boolean dijkstra(int source, int sink) {
    class Entry {
      int e, u;
      long d;

      Entry(int e, int u, long d) {
        this.e = e;
        this.u = u;
        this.d = d;
      }
    }

    long[] dist = new long[n];
    int[] parent = new int[n];
    Arrays.fill(dist, Long.MAX_VALUE);
    PriorityQueue<Entry> pq = new PriorityQueue<>(Comparator.comparingLong(entry -> entry.d));
    pq.add(new Entry(-1, source, 0));

    while(!pq.isEmpty()) {
      Entry entry = pq.remove();
      int u = entry.u;
      if (dist[u] != Long.MAX_VALUE) continue;
      dist[u] = entry.d;
      parent[u] = entry.e;
      for (int e = elast[u]; e != -1; e = eprev[e]) {
        if (resCapa(e) == 0) continue;
        pq.add(new Entry(e, ehead[e], dist[u] + resCost(e)));
      }
    }

    if (dist[sink] == Long.MAX_VALUE) return false;

    long delta = Long.MAX_VALUE;
    for (int e = parent[sink]; e != -1; e = parent[etail(e)]) {
      delta = Math.min(delta, resCapa(e));
    }
    for (int e = parent[sink]; e != -1; e = parent[etail(e)]) {
      flow[e] += delta;
      flow[einv(e)] -= delta;
      totalCost += cost[e] * delta;
    }
    totalFlow += delta;

    for (int u = 0; u < n; u++)
      if (dist[u] != Long.MAX_VALUE)
        pot[u] += dist[u];

    return true;
  }
}
