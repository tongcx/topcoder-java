import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;

class ShortestPath {
  Graph g;
  Function<Integer, Long> cost;
  long[] dist;
  int[] ent;

  ShortestPath(Graph g, Function<Integer, Long> cost) {
    this.g = g;
    this.cost = cost;
    dist = new long[g.n];
    ent = new int[g.n];
  }

  void dijkstra(int source) {
    Arrays.fill(dist, Long.MAX_VALUE);

    PriorityQueue<Entry> pq = new PriorityQueue<>(Comparator.comparingLong(entry -> entry.d));
    pq.add(new Entry(-1, source, 0));

    while(!pq.isEmpty()) {
      Entry entry = pq.remove();
      int u = entry.u;
      if (dist[u] != Long.MAX_VALUE) continue;
      dist[u] = entry.d;
      ent[u] = entry.e;
      for (int e = g.elast[u]; e != -1; e = g.eprev[e]) {
        if (cost.apply(e) == Long.MAX_VALUE) continue;
        pq.add(new Entry(e, g.ehead[e], dist[u] + cost.apply(e)));
      }
    }
  }

  static class Entry {
    int e, u;
    long d;

    Entry(int e, int u, long d) {
      this.e = e;
      this.u = u;
      this.d = d;
    }
  }
}
