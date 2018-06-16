import java.util.Arrays;

class MaxFlow extends Graph {
  long totalFlow;
  // edge arrays
  long[] flow, capa;
  // node arrays
  int[] now, level;

  long resCapa(int e) {
    return capa[e] - flow[e];
  }

  MaxFlow(int nNodes, int nEdges) {
    super(nNodes, nEdges);
    flow = new long[2 * m]; capa = new long[2 * m];
    level = new int[n]; now = new int[n];
  }

  int addEdge(int u, int v, long capacity) {
    int e = addEdge(u, v);
    capa[e] = capacity;
    return e;
  }

  long dinic(int source, int sink) {
    while (bfs(source, sink)) {
      System.arraycopy(elast, 0, now, 0, n);
      while (dfs(source, Long.MAX_VALUE, sink) > 0);
    }
    return totalFlow;
  }

  boolean bfs(int source, int sink) {
    Arrays.fill(level, -1);
    int front = 0, back = 0;
    int[] queue = new int[n];

    level[source] = 0;
    queue[back++] = source;

    while (front < back && level[sink] == -1) {
      int u = queue[front++];
      for (int e = elast[u]; e != -1; e = eprev[e]) {
        int v = ehead[e];
        if (level[v] != -1 || resCapa(e) == 0) continue;
        level[v] = level[u] + 1;
        queue[back++] = v;
      }
    }

    return level[sink] != -1;
  }

  long dfs(int u, long curDelta, int sink) {
    if (u == sink) {
      totalFlow += curDelta;
      return curDelta;
    }

    for (int e = now[u]; e != -1; now[u] = e = eprev[e]) {
      int v = ehead[e];
      if (level[v] <= level[u] || resCapa(e) == 0) continue;
      long delta = dfs(v, Math.min(curDelta, resCapa(e)), sink);
      if (delta == 0) continue;
      flow[e] += delta;
      flow[einv(e)] -= delta;
      return delta;
    }
    return 0;
  }
}
