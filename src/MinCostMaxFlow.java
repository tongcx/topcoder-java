import java.util.*;

class MinCostMaxFlow {
    long[] flow, capa;
    double[] cost;
    int[] eadj, eprev, elast;
    int eidx, N, M;
    long totalFlow;
    double totalCost;

    MinCostMaxFlow(int nodes, int edges) {
        this.N = nodes;
        this.M = edges;

        flow = new long[2*M];
        capa = new long[2*M];
        cost = new double[2*M];
        eadj = new int[2*M];
        eprev = new int[2*M];
        elast = new int[N];
        Arrays.fill(elast, -1);
    }

    void add_edge(int a, int b, long c, double t) {
        eadj[eidx] = b; flow[eidx] = 0; capa[eidx] = c; cost[eidx] = t; eprev[eidx] = elast[a]; elast[a] = eidx++;
        eadj[eidx] = a; flow[eidx] = 0; capa[eidx] = 0; cost[eidx] = -t; eprev[eidx] = elast[b]; elast[b] = eidx++;
    }

    double ssp(int source, int sink) {
        while (spfa(source, sink));
        return totalCost;
    }

    boolean spfa(int source, int sink) {
        int[] ent = new int[N];
        double[] dist = new double[N];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);

        Queue<Integer> q = new ArrayDeque<>();
        q.add(source); dist[source] = 0; ent[source] = -1;
        while (!q.isEmpty()) {
            int u = q.remove();
            for (int e = elast[u]; e != -1; e = eprev[e])
                if (flow[e] < capa[e] && dist[eadj[e]] > dist[u] + cost[e]) {
                    q.add(eadj[e]); dist[eadj[e]] = dist[u] + cost[e]; ent[eadj[e]] = e;
                }
        }

        if (dist[sink] == Double.POSITIVE_INFINITY) return false;
        long curflow = Long.MAX_VALUE;
        for (int e = ent[sink]; e != -1; e = ent[eadj[e^1]])
            curflow = Math.min(curflow, capa[e] - flow[e]);
        totalFlow += curflow;

        for (int e = ent[sink]; e != -1; e = ent[eadj[e^1]]) {
            flow[e] += curflow;
            flow[e^1] -= curflow;
            totalCost += curflow * cost[e];
        }
        return true;
    }
}
