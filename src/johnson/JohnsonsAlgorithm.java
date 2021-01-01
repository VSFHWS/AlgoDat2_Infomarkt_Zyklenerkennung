package johnson;

import graph.DG;
import tarjan.TarjansAlgorithm;

import java.util.*;

public class JohnsonsAlgorithm {
    private static List<DG> subGraphs;

    private static Deque<Integer> stack;
    private static Set<Integer> blockedSet;
    private static Map<Integer, Integer> blockedMap;

    private static List<List<Integer>> allCycles;


    public static List<List<Integer>> calculateCycles(DG graph) {
        setup(graph);
        long startIndex = 0;

        for (DG subGraph : subGraphs) {
            int originalSize = subGraph.size();

            while (startIndex <= originalSize) {
                calculateCyclesSub(subGraph);
                subGraph.removeVertex((int) startIndex);
                startIndex++;
            }
        }

        // return allCycles;
        return allCycles;
    }

    private static void calculateCyclesSub(DG subGraph) {

    }



    // ### SETUP ###

    private static void setup(DG graph) {
        // Initialize all stacks, lists, etc...
        subGraphs = sccToSubGraphs(graph);

        stack = new LinkedList<>();
        blockedSet = new HashSet<>();
        blockedMap = new HashMap<>();

        allCycles = new ArrayList<>();
    }

    private static List<DG> sccToSubGraphs(DG graph) {
        subGraphs = new ArrayList<>();

        // Calculate the simply connected components with Tarjan's Algo
        List<Set<Integer>> sccs = TarjansAlgorithm.calculateSCC(graph);

        // Turn the SCC result of a list of sets of ints into a list of subgraphs
        for (Set<Integer> scc : sccs) {
            // No circle if vertices.size() == 1
            if (scc.size() == 1) continue;

            DG subGraph;

            // Check if the startVertex from the SCC is 0 or not. If not we need labels
            if (scc.contains(0)) {
                subGraph = new DG(scc.size());
            } else {
                subGraph = new DG(new ArrayList<>(scc), scc.size());
            }

            // If the subGraph only has one vertex, there can't be any edges
            if (!(scc.size() == 1)) {
                // Setup edges
                for (int i : scc) {
                    for (DG.Edge edge : graph.getVertex(i).getEdges()) {
                        if (scc.contains(edge.getTo())) {
                            subGraph.addEdge(edge.getFrom(), edge.getTo());
                        }
                    }
                }
            }

            subGraphs.add(subGraph);
        }

        return subGraphs;
    }
}
