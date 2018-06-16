import java.util.Arrays;

class Graph {
  int eidx, n, m;
  // edge arrays
  int[] ehead, eprev;
  // node arrays
  int[] elast;

  boolean isRev(int e) {
    return e%2 == 1;
  }

  int einv(int e) {
    return e^1;
  }

  int etail(int e) {
    return ehead[einv(e)];
  }

  Graph(int nNodes, int nEdges) {
    n = nNodes;
    m = nEdges;

    ehead = new int[2*m]; eprev = new int[2*m];
    elast = new int[n];
    Arrays.fill(elast, -1);
  }

  int addEdge(int u, int v) {
    ehead[eidx] = v; eprev[eidx] = elast[u]; elast[u] = eidx++;
    ehead[eidx] = u; eprev[eidx] = elast[v]; elast[v] = eidx++;
    return elast[u];
  }
}