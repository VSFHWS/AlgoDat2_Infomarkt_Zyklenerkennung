package johnson;

import graph.DG;
import tarjan.TarjansAlgorithm;

import java.util.*;


public class JohnsonsAlgorithm {
    private static List<DG> subGraphs;
    private static List<List<Integer>> sccs;

    private static Deque<Integer> stack;
    private static Set<Integer> blockedSet;
    private static Map<Integer, Set<Integer>> blockedMap;

    private static List<List<Integer>> allCycles;


    public static List<List<Integer>> calculateCycles(DG graph) {
        setup(graph);

        int startVertex = 0;

        for (int i = 0; i < subGraphs.size(); i++) {
            while (subGraphs.get(i).size() > 1) {
                blockedSet.clear();
                blockedMap.clear();
                calculateCyclesSub(subGraphs.get(i), startVertex, startVertex);

                // Now remove the startVertex from the current subGraph by creating a new one from a changed scc
                sccs.get(i).remove(0);
                subGraphs.set(i, subGraphFromSCCBlueprint(sccs.get(i), graph));
            }
        }

        // return allCycles;
        return allCycles;
    }

    private static boolean calculateCyclesSub(DG subGraph, int startVertex, int currentVertex) {
        boolean foundCycle = false;
        stack.push(currentVertex);
        blockedSet.add(currentVertex);

        for (DG.Edge e : subGraph.getVertex(currentVertex).getEdges()) {
            int neighbour = e.getTo();

            // if neighbour is the same as start vertex -> cycle found
            if (neighbour == startVertex) {
                stack.push(startVertex);
                List<Integer> cycle = new ArrayList<>(stack);
                Collections.reverse(cycle);
                stack.pop();

                // Before adding the cycle to the final list of all cycles, the vertex id's converted to original
                if (subGraph.hasLabel()) {
                    for (int i = 0; i < cycle.size(); i++) {
                        cycle.set(i, subGraph.getLabel(cycle.get(i)));
                    }
                }
                allCycles.add(cycle);
                foundCycle = true;
            } else if (!blockedSet.contains(neighbour)) {
                boolean gotCycle = calculateCyclesSub(subGraph, startVertex, neighbour);
                foundCycle = foundCycle || gotCycle;
            }
        }

        if (foundCycle) {
            // Remove from blockedSet, then remove all other vertices dependent on this vertex from blockedSet
            unblock(currentVertex);
        } else {
            for (DG.Edge e : subGraph.getVertex(currentVertex).getEdges()) {
                int w = e.getTo();
                Set<Integer> bSet = getBSet(w);
                bSet.add(currentVertex);
            }
        }
        // remove vertex from the stack
        stack.pop();
        return foundCycle;
    }

    private static void unblock(int currentVertex) {
        blockedSet.remove(currentVertex);
        if (blockedMap.get(currentVertex) != null) {
            blockedMap.get(currentVertex).forEach(v -> {
                if (blockedSet.contains(v)) unblock(v);
            });
            blockedMap.remove(currentVertex);
        }
    }

    private static Set<Integer> getBSet(int w) {
        return blockedMap.computeIfAbsent(w, (key) -> new HashSet<>());
    }



    // ### SETUP ###

    private static void setup(DG graph) {
        // Calculate SCC with Tarjan
        sccs = TarjansAlgorithm.calculateSCC(graph);
        // If SCC size is 1 then no subGraph will be built because you can't build a cycle from one vertex
        sccs.removeIf(scc -> scc.size() <= 1);

        subGraphs = new ArrayList<>();

        // Initialize all stacks, lists, etc...
        for (List<Integer> scc : sccs) {
            DG subGraph = subGraphFromSCCBlueprint(scc, graph);
            subGraphs.add(subGraph);
        }

        stack = new LinkedList<>();
        blockedSet = new HashSet<>();
        blockedMap = new HashMap<>();

        allCycles = new ArrayList<>();
    }

    private static DG subGraphFromSCCBlueprint(List<Integer> scc, DG graph) {
        DG subGraph;

        // Check if the startVertex from the SCC is 0 or not. If not we need labels
        if (scc.contains(0)) {
            subGraph = new DG(scc.size());
        } else {
            subGraph = new DG(new ArrayList<>(scc), scc.size());
        }

        // If the subGraph only has one vertex, there can't be any edges
        for (int i : scc) {
            for (DG.Edge edge : graph.getVertex(i).getEdges()) {
                if (scc.contains(edge.getTo())) {
                    subGraph.addEdge(edge.getFrom(), edge.getTo());
                }
            }
        }

        return subGraph;
    }
}
