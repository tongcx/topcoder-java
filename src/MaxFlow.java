import java.util.*;

class MaxFlow {
  int eidx, n, m;
  long f;
  // edge arrays
  long[] flow, capa;
  int[] ehead, eprev;
  // node arrays
  int[] now, level, elast;

  long resCapa(int e) {
    return capa[e] - flow[e];
  }

  int etail(int e) {
    return ehead[e^1];
  }

  MaxFlow(int nNodes, int nEdges) {
    n = nNodes;
    m = nEdges;
    eidx = 0;

    flow = new long[2 * m]; capa = new long[2 * m];
    ehead = new int[2 * m]; eprev = new int[2 * m]; elast = new int[n];
    level = new int[n]; now = new int[n];
    Arrays.fill(elast, -1);
  }

  int addEdge(int u, int v, long capacity) {
    ehead[eidx] = v; flow[eidx] = 0; capa[eidx] = capacity; eprev[eidx] = elast[u]; elast[u] = eidx++;
    ehead[eidx] = u; flow[eidx] = 0; capa[eidx] = 0; eprev[eidx] = elast[v]; elast[v] = eidx++;
    return elast[u];
  }

  long dinic(int source, int sink) {
    while (bfs(source, sink)) {
      System.arraycopy(elast, 0, now, 0, n);
      while (dfs(source, Long.MAX_VALUE, sink) > 0);
    }
    return f;
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
      f += curDelta;
      return curDelta;
    }

    for (int e = now[u]; e != -1; now[u] = e = eprev[e]) {
      int v = ehead[e];
      if (level[v] <= level[u] || resCapa(e) == 0) continue;
      long delta = dfs(v, Math.min(curDelta, resCapa(e)), sink);
      if (delta == 0) continue;
      flow[e] += delta;
      flow[e ^ 1] -= delta;
      return delta;
    }
    return 0;
  }
}
