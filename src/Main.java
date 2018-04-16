import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static String IMPOSSIBLE = "IMPOSSIBLE";
  static String POSSIBLE = "POSSIBLE";
  static int[] dr = {-1, 1, 0, 0};
  static int[] dc = {0, 0, -1, 1};

  int R, C;
  String[] board;
  int[][] hshooter;
  int[][] vshooter;

  Main(Scanner s) {
    R = s.nextInt();
    C = s.nextInt();
    board = new String[R];
    for (int r = 0; r < R; r++) {
      board[r] = s.next();
    }

    hshooter = new int[R][C];
    vshooter = new int[R][C];
    for (int r = 0; r < R; r++) {
      Arrays.fill(hshooter[r], -1);
      Arrays.fill(vshooter[r], -1);
    }
  }

  boolean isShooter(int r, int c) {
    return board[r].charAt(c) == '-' || board[r].charAt(c) == '|';
  }

  boolean isWall(int r, int c) {
    return board[r].charAt(c) == '#';
  }

  boolean isEmpty(int r, int c) {
    return board[r].charAt(c) == '.';
  }

  boolean beam(int r, int c, int dir) {
    while (true) {
      if (r < 0 || r >= R || c < 0 || c >= C) return true;
      if (isShooter(r, c)) return false;
      if (isWall(r, c)) return true;
      if (board[r].charAt(c) == '/') dir ^= 3;
      if (board[r].charAt(c) == '\\') dir ^= 2;
      r += dr[dir];
      c += dc[dir];
    }
  }

  void assign(int r, int c, int dir, int k) {
    while(true) {
      if (r < 0 || r >= R || c < 0 || c >= C) return;
      if (isWall(r, c)) return;
      if (isEmpty(r, c)) {
        if ((dir & 2) == 0) {
          vshooter[r][c] = k;
        } else {
          hshooter[r][c] = k;
        }
      }
      if (board[r].charAt(c) == '/') dir ^= 3;
      if (board[r].charAt(c) == '\\') dir ^= 2;
      r += dr[dir];
      c += dc[dir];
    }
  }

  void run() {
    int nShooter = 0, nEmpty = 0;
    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (isEmpty(r, c)) nEmpty++;
        if (isShooter(r, c)) nShooter++;
      }
    }

    TwoSat twoSat = new TwoSat(nShooter, nEmpty + nShooter);
    int shooterIdx = 0;
    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (!isShooter(r, c)) continue;
        int v = -1, h = -1;
        if (beam(r - 1, c, 0) && beam(r + 1, c, 1)) {
          v = shooterIdx*2;
          assign(r - 1, c, 0, v);
          assign(r + 1, c, 1, v);
        }
        if (beam(r, c-1, 2) && beam(r, c+1, 3)) {
          h = shooterIdx*2+1;
          assign(r, c-1, 2, h);
          assign(r, c+1, 3, h);
        }
        v = v == -1 ? h : v;
        h = h == -1 ? v : h;
        if (v == -1 && h == -1) {
          System.out.println(IMPOSSIBLE);
          return;
        }
        twoSat.addClause(h, v);
        shooterIdx++;
      }
    }

    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (!isEmpty(r, c)) continue;

        int h = hshooter[r][c] == -1 ? vshooter[r][c] : hshooter[r][c];
        int v = vshooter[r][c] == -1 ? hshooter[r][c] : vshooter[r][c];

        if (h == -1 && v == -1) {
          System.out.println(IMPOSSIBLE);
          return;
        }
        twoSat.addClause(h, v);
      }
    }
    if (!twoSat.run()) {
      System.out.println(IMPOSSIBLE);
      return;
    }

    System.out.println(POSSIBLE);
    shooterIdx = 0;
    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (!isShooter(r, c)) {
          System.out.print(board[r].charAt(c));
          continue;
        }
        System.out.print(twoSat.value[shooterIdx] == 0 ? '|' : '-');
        shooterIdx++;
      }
      System.out.println();
    }
  }

  public static void main(String[] args) throws Exception {
    Scanner s = new Scanner(new FileInputStream("C.in"));
    int T = s.nextInt();
    for (int t = 1; t <= T; t++) {
      System.out.format("Case #%d: ", t);
      new Main(s).run();
    }
  }
}
