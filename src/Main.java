import graph.DG;
import johnson.JohnsonsAlgorithm;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // TODO: DG.display() still + 1
        DG graph = DG.createGraphFromFile("graphExample.txt");

        List<List<Integer>> johnsonResult = JohnsonsAlgorithm.calculateCycles(graph);

        System.out.println(johnsonResult);
    }
}
