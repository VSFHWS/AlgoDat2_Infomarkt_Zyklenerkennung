import graph.DG;
import johnson.JohnsonsAlgorithm;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DG graph = DG.createGraphFromFile("graphWithCycles.txt");

        List<List<Integer>> johnsonResult = JohnsonsAlgorithm.calculateCycles(graph);

        // print the result of the Johnson algorithm
        johnsonResult.forEach(System.out::println);
    }
}
