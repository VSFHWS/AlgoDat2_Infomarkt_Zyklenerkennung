import graph.DG;
import johnson.JohnsonsAlgorithm;
import tarjan.TarjansAlgorithm;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        DG [] graphs = new DG [] {
                DG.createGraphFromFile("src/graphExamples/graphWithoutCycles1.txt"),
                DG.createGraphFromFile("src/graphExamples/graphWithoutCycles2.txt"),
                DG.createGraphFromFile("src/graphExamples/graphWithOneCycle1.txt"),
                DG.createGraphFromFile("src/graphExamples/graphWithOneCycle2.txt"),
                DG.createGraphFromFile("src/graphExamples/graphWithCycles1.txt"),
                DG.createGraphFromFile("src/graphExamples/graphWithCycles2.txt")
        };

        int graphNum = 0;

        // calculate Tarjan's algorithm for visualisation purposes
        List<List<Integer>> tarjanResult = TarjansAlgorithm.calculateSCC(graphs[graphNum]);

        // calculate Johnson's Algorithm (which calls Tarjan's algorithm in the methods)
        List<List<Integer>> johnsonResult = JohnsonsAlgorithm.calculateCycles(graphs[graphNum]);

        // print the result of Tarjan's algorithm
        System.out.println("Tarjan's algorithm result:");
        if (tarjanResult.size() == 0) System.out.println("No strongly connected components detected.");
        else tarjanResult.forEach(System.out::println);

        // print the result of the Johnson algorithm
        System.out.println("\nJohnson's algorithm result:");
        if (johnsonResult.size() == 0) System.out.println("No cycles detected.");
        else johnsonResult.forEach(System.out::println);
    }
}
