import java.util.*;

class MaxFlow {
    long[] flow, capa;
    int[] now, level, eadj, eprev, elast;
    int eidx, N, M;
    long totalFlow;

    MaxFlow(int nodes, int edges) {
        this.N = nodes;
        this.M = edges;

        flow = new long[2*M];
        capa = new long[2*M];
        eadj = new int[2*M];
        eprev = new int[2*M];
        elast = new int[N];
        level = new int[N];
        now = new int[N];
        Arrays.fill(elast, -1);
        eidx = 0;
    }

    void add_edge(int a, int b, long c) {
        eadj[eidx] = b; flow[eidx] = 0; capa[eidx] = c; eprev[eidx] = elast[a]; elast[a] = eidx++;
        eadj[eidx] = a; flow[eidx] = 0; capa[eidx] = 0; eprev[eidx] = elast[b]; elast[b] = eidx++;
    }

    long dinic(int source, int sink) {
        while (bfs(source, sink)) {
            System.arraycopy(elast, 0, now, 0, N);
            while (dfs(source, Long.MAX_VALUE, sink) > 0);
        }
        return totalFlow;
    }

    boolean bfs(int source, int sink) {
        Arrays.fill(level, -1);
        int front = 0, back = 0;
        int[] queue = new int[N];

        level[source] = 0;
        queue[back++] = source;

        while (front < back && level[sink] == -1) {
            int node = queue[front++];
            for (int e = elast[node]; e != -1; e = eprev[e]) {
                int to = eadj[e];
                if (level[to] == -1 && flow[e] < capa[e]) {
                    level[to] = level[node] + 1;
                    queue[back++] = to;
                }
            }
        }

        return level[sink] != -1;
    }

    long dfs(int cur, long curflow, int goal) {
        if (cur == goal) {
            totalFlow += curflow;
            return curflow;
        }

        for (int e = now[cur]; e != -1; now[cur] = e = eprev[e]) {
            if (level[eadj[e]] > level[cur] && flow[e] < capa[e]) {
                long res = dfs(eadj[e], Math.min(curflow, capa[e] - flow[e]), goal);
                if (res > 0) {
                    flow[e] += res;
                    flow[e ^ 1] -= res;
                    return res;
                }
            }
        }
        return 0;
    }
}
