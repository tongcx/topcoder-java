import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// codejam 2017 round 2
public class Bilingual {
  int nextIdx = 2;
  Map<String, Integer> wordIdx = new HashMap<>();

  int wordIdx(String word) {
    if (!wordIdx.containsKey(word)) wordIdx.put(word, nextIdx++);
    return wordIdx.get(word);
  }

  int run(Scanner s) {
    int N = s.nextInt(); s.nextLine();
    int wordIdx = 2;
    MaxFlow maxFlow = new MaxFlow(2+4000*2, 2000 + 4000 + 10*9*200);
    for (String word : s.nextLine().split(" ")) {
      maxFlow.addEdge(0, wordIdx(word)*2, Long.MAX_VALUE);
    }
    for (String word : s.nextLine().split(" ")) {
      maxFlow.addEdge(wordIdx(word)*2+1, 1, Long.MAX_VALUE);
    }
    for (int i = 2; i < N; i++) {
      String[] words = s.nextLine().split(" ");
      for (int j = 0; j < words.length; j++)
        for (int k = 0; k < words.length; k++) {
          if (j != k) {
            maxFlow.addEdge(wordIdx(words[j]) * 2 + 1, wordIdx(words[k]) * 2, Long.MAX_VALUE);
          }
        }
    }

    for (int i = 2; i < nextIdx; i++) {
      maxFlow.addEdge(i*2, i*2+1, 1);
    }
    return (int)maxFlow.dinic(0, 1);
  }

  public static void main(String[] args) throws Exception {
    Scanner s = new Scanner(new FileInputStream("C.in"));
    int T = s.nextInt();
    for (int t = 1; t <= T; t++) {
      System.out.format("Case #%d: %d\n", t, new Bilingual().run(s));
    }
  }
}
