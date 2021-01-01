package graph;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/***
 * Implementation of a directed graph
 */

public class DG {
    private final ArrayList<Vertex> vertices;
    // Graph vertices start at 0 but some subGraphs might have vertices that start at another int.
    // Carry a map of original labels -> Example: startVertex = 7 is converted to 0 in the class.
    private boolean hasLabel = false;
    private Map<Integer, Integer> originalToLabel;
    private Map<Integer, Integer> labelToOriginal;


    public DG (int vertexCount) {
        if (vertexCount < 0) throw new IllegalArgumentException("Vertex count can't be smaller than 0.");
        vertices = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            vertices.add(new Vertex(i));
        }
    }


    public DG (List<Integer> vertexLabels, int vertexCount) {
        this(vertexCount);
        hasLabel = true;
        this.originalToLabel = new HashMap<>();
        this.labelToOriginal = new HashMap<>();

        for (int i = 0; i < vertexLabels.size(); i++) {
            this.originalToLabel.put(i, vertexLabels.get(i));
            this.labelToOriginal.put(vertexLabels.get(i), i);
        }
    }


    public void addEdge(int from, int to) {
        if (hasLabel) {
            from = labelToOriginal.get(from);
            to = labelToOriginal.get(to);
        }
        vertices.get(from).addEdge(from, to);
    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    public void removeVertex(int id) {
        if (hasLabel) {
            id = labelToOriginal.remove(id);
            originalToLabel.remove(id);
        }

        if (!vertices.contains(id)) throw new RuntimeException("Id not in vertices list.");
        vertices.remove(id);

        for (Vertex v : vertices) {
            int finalId = id;
            v.edges.removeIf(e -> e.to == finalId);
        }
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }


    public int size() {
        return vertices.size();
    }

    // Create a graph from a .txt file
    public static DG createGraphFromFile(String filename) {
        Path file = Paths.get(filename);

        try {
            List<int[]> parseResult =
                    Files.readAllLines(Paths.get(filename))
                            .stream()
                            .map(x -> x.split(" "))
                            .flatMap(Arrays::stream)
                            .filter(x -> !x.isEmpty())
                            .map(x -> x.replaceAll("\\{", "").replaceAll("}", "").replaceAll(" ", ""))
                            .map(x -> x.split(","))
                            .map(x -> new int[]{ Integer.parseInt(x[0]), Integer.parseInt(x[1]) })
                            .collect(Collectors.toList());

            int maxIndex = parseResult.stream().flatMapToInt(Arrays::stream).max().getAsInt();
            DG graph = new DG(maxIndex + 1);

            for(int[] arr : parseResult) {
                graph.addEdge(arr[0], arr[1]);
            }

            return graph;

        } catch (Exception e) {
            System.out.println("An error has occurred while loading from path: " + file.toAbsolutePath().toString());
            throw new RuntimeException(e);
        }
    }

    // Creates a graph as copy of G
    public DG cloneGraph() {
        DG graph = new DG(vertices.size());

        for(Vertex v : vertices) {
            for(DG.Edge e : v.getEdges()) {
                graph.addEdge(e.from, e.to);
            }

        }
        return graph;
    }

    public void display() {
        // Check if there vertex labels available
        if (hasLabel) {
            for (Vertex v : vertices) {
                for (Edge e : v.edges) {
                    System.out.println((originalToLabel.get(e.from)+1) + " -> " + (originalToLabel.get(e.to)+1));
                }
            }

        } else {
            for (Vertex v : vertices) {
                for (Edge e : v.edges) {
                    System.out.println(e.toString());
                }
            }
        }
    }


    // ### Vertex ###
    public class Vertex {
        private int id;
        private ArrayList<Edge> edges;

        public Vertex (int id) {
            this.id = id;
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to) {
            edges.add(new Edge(from, to));
        }

        public ArrayList<Vertex> getAdjacentVertices() {
            ArrayList<Vertex> vertices = new ArrayList<>();
            for (Edge e : getEdges()) {
                vertices.add(getVertex(e.to));
            }
            return vertices;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ArrayList<Edge> getEdges() {
            return edges;
        }

        public void setEdges(ArrayList<Edge> edges) {
            this.edges = edges;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return id == vertex.getId();
        }

    }


    // ### Edge ###
    public class Edge {
        private int from;
        private int to;

        public Edge (int from, int to) {
            this.from = from;
            this.to = to;
        }

        public Vertex getFromVertex() {
            return getVertex(from);
        }

        public Vertex getToVertex() {
            return getVertex(to);
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public void setTo(int to) {
            this.to = to;
        }

        @Override
        public String toString() {
            return (from+1) + " -> " + (to+1);
        }
    }
}
