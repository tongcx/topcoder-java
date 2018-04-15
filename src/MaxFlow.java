import java.util.Arrays;

class MaxFlow {
  Graph g;
  long f;
  // edge arrays
  long[] flow, capa;
  // node arrays
  int[] now, level;

  long resCapa(int e) {
    return capa[e] - flow[e];
  }

  MaxFlow(Graph g) {
    this.g = g;
    flow = new long[2 * g.m]; capa = new long[2 * g.m];
    level = new int[g.n]; now = new int[g.n];
  }

  int addEdge(int u, int v, long capacity) {
    int e = g.addEdge(u, v);
    flow[e]  = 0; capa[e] = capacity;
    flow[g.einv(e)] = 0; capa[g.einv(e)] = 0;
    return e;
  }

  long dinic(int source, int sink) {
    while (bfs(source, sink)) {
      System.arraycopy(g.elast, 0, now, 0, g.n);
      while (dfs(source, Long.MAX_VALUE, sink) > 0);
    }
    return f;
  }

  boolean bfs(int source, int sink) {
    Arrays.fill(level, -1);
    int front = 0, back = 0;
    int[] queue = new int[g.n];

    level[source] = 0;
    queue[back++] = source;

    while (front < back && level[sink] == -1) {
      int u = queue[front++];
      for (int e = g.elast[u]; e != -1; e = g.eprev[e]) {
        int v = g.ehead[e];
        if (level[v] != -1 || resCapa(e) == 0) continue;
        level[v] = level[u] + 1;
        queue[back++] = v;
      }
    }

    return level[sink] != -1;
  }

  long dfs(int u, long curDelta, int sink) {
    if (u == sink) {
      f += curDelta;
      return curDelta;
    }

    for (int e = now[u]; e != -1; now[u] = e = g.eprev[e]) {
      int v = g.ehead[e];
      if (level[v] <= level[u] || resCapa(e) == 0) continue;
      long delta = dfs(v, Math.min(curDelta, resCapa(e)), sink);
      if (delta == 0) continue;
      flow[e] += delta;
      flow[g.einv(e)] -= delta;
      return delta;
    }
    return 0;
  }
}
