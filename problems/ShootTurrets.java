import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

// codejam 2017 round 2
public class ShootTurrets {
  static int[] dr = {-1, 1, 0, 0};
  static int[] dc = {0, 0, -1, 1};

  int R, C, M;
  String[] board;
  BiMatch bm;
  int sidx, tidx;
  int[][] sid;
  int[][] tid;

  boolean isSolider(int r, int c) {
    return board[r].charAt(c) == 'S';
  }

  boolean isTurret(int r, int c) {
    return board[r].charAt(c) == 'T';
  }

  boolean isWall(int r, int c) {
    return board[r].charAt(c) == '#';
  }

  void check(int r, int c, int dir, boolean[] canReach) {
    while (true) {
      if (r < 0 || r >= R || c < 0 || c >= C) return;
      if (isWall(r, c)) return;
      if (isTurret(r, c)) canReach[tid[r][c]] = true;
      r += dr[dir];
      c += dc[dir];
    }
  }

  void bfs(int r, int c) {
    int s = sid[r][c];
    int front = 0, back = 0;
    int[] qr = new int[R * C], qc = new int[R * C], qd = new int[R * C];
    boolean[][] mark = new boolean[R][C];
    boolean[] canReach = new boolean[tidx];
    qr[0] = r; qc[0] = c; qd[0] = 0; mark[r][c] = true; back++;
    while (front < back) {
      r = qr[front]; c = qc[front]; int d = qd[front]; front++;
      for (int dir = 0; dir < 4; dir++) {
        check(r, c, dir, canReach);
      }
      if (d >= M) continue;
      for (int dir = 0; dir < 4; dir++) {
        int rr = r + dr[dir]; int cc = c + dc[dir];
        if (rr < 0 || rr >= R || cc < 0 || cc >= C) continue;
        if (isWall(rr, cc)) continue;
        if (!mark[rr][cc]) {
          mark[rr][cc] = true;
          qr[back] = rr; qc[back] = cc; qd[back] = d + 1; back++;
        }
      }
    }
    for (int t = 0; t < tidx; t++)
      if (canReach[t])
        bm.addEdge(s, t);
  }

  ShootTurrets(Scanner s) {
    C = s.nextInt();
    R = s.nextInt();
    M = s.nextInt();
    board = new String[R];
    for (int r = 0; r < R; r++) {
      board[r] = s.next();
    }

    sidx = 0;
    tidx = 0;
    sid = new int[R][C];
    tid = new int[R][C];
    for (int r = 0; r < R; r++) {
      Arrays.fill(sid[r], -1);
      Arrays.fill(tid[r], -1);
    }
    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (isSolider(r, c)) sid[r][c] = sidx++;
        if (isTurret(r, c)) tid[r][c] = tidx++;
      }
    }
  }

  void run() {
    bm = new BiMatch(sidx, tidx, sidx * tidx);
    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (isSolider(r, c)) {
          bfs(r, c);
        }
      }
    }
    System.out.println(bm.hopcroftKarp());
  }

  public static void main(String[] args) throws Exception {
    Scanner s = new Scanner(new FileInputStream("D.in"));
    int T = s.nextInt();
    for (int t = 1; t <= T; t++) {
      System.out.format("Case #%d: ", t);
      new ShootTurrets(s).run();
    }
  }
}
