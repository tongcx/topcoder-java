import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// TCO 18 2B
public class SquareFreeSet {
  int n;
  int[] x;
  MinCostMatching mcm;
  Map<Integer, Integer> fidx = new HashMap<>();

  int f(int a) {
    int res = 1;
    for (int i = 2; i*i <= a; i++) {
      while (a % i == 0) {
        a /= i;
        if (res % i == 0) res /= i;
        else res *= i;
      }
    }
    return res * a;
  }

  void produce(int i) {
    Set<Integer> ff = new HashSet<>();
    for (int d = 0; ff.size() < n; d++) {
      int dd = d%2==1 ? d/2+1 : -d/2;
      if (x[i] + dd <= 0) continue;
      int fff = f(x[i] + dd);
      if (ff.contains(fff)) continue;
      ff.add(fff);
      if (!fidx.containsKey(fff)) {
        fidx.put(fff, fidx.size());
      }

      mcm.addEdge(i, fidx.get(fff), Math.abs(dd));
    }
  }

	public int findCost(int[] x) {
	  this.x = x;
	  n = x.length;
	  mcm = new MinCostMatching(n, n*n, n*n);

	  for (int i = 0; i < n; i++) produce(i);
	  return (int)mcm.hungarian();
	}
}
