package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList();
        int start = 1000;
        Ns.addLast(start);
        for (int i = 0; i < 7; i++) {
            Ns.addLast(start * 2);
            start *= 2;
        }

        AList<Double> times = new AList();

        int M = 10000;
        AList<Integer> opCounts = new AList();
        for (int i = 0; i < Ns.size(); i++) {
            opCounts.addLast(M);
        }

        for (int i = 0; i < Ns.size(); i++) {
            SLList<Integer> sl = new SLList();
            for (int j = 0; j < Ns.get(i); j++) {
                sl.addLast(M);
            }

            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < M; j++) {
                sl.getLast();
            }

            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }


        printTimingTable(Ns, times, opCounts);
    }

}
