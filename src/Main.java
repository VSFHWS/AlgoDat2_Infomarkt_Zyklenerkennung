import graph.DG;
import johnson.JohnsonsAlgorithm;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        DG graph = DG.createGraphFromFile("graphExample.txt");

        List<List<Integer>> johnsonResult = JohnsonsAlgorithm.calculateCycles(graph);

        johnsonResult.forEach(System.out::println);
    }
}
