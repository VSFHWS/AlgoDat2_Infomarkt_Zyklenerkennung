package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Edge;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Node;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.View.GraphVisualizer;

public class TarjanSCC
{
	private int index;
	private DirectedWeightedGraph graph;			//input graph
	private Stack<Node> nodeStack;					//contains the nodes in order they are traversed
	private Map<Node, Integer> indexMap;			
	private Map<Node, Integer> lowMap;				//contains the lowest index that the nodes have through their connections
	private DirectedWeightedGraph unvisited;		//Graph containing all unvisited nodes
	private ArrayList<ArrayList<Node>> result;
	
	public TarjanSCC(DirectedWeightedGraph graph)
	{
		this.index = 0;
		this.graph = graph;
		nodeStack = new Stack<>();
		indexMap = new HashMap<>();
		lowMap = new HashMap<>();
		result = new ArrayList<>();
	}
	
	/**
	 * Runs tarjans algorithm for every node in graph if it's not in the indexMap.
	 * @return an ArrayList containing all subgraphs/strongly connected components
	 */
	public ArrayList<DirectedWeightedGraph> getSCCs()
	{
		result = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> allNodes = graph.getNodes();
		unvisited = new DirectedWeightedGraph(graph);
		
		for(Node n : allNodes)
		{
			if(indexMap.get(n) == null)
				result.addAll(this.getSingleSCC(n));
		}
		
		//return result after converting the ArrayList<ArrayList<Node>> to ArrayList<DirectedWeightedGraph>
		return convertToGraph(result);
	}
	
	/**
	 * Determines one SCC for the given node recursively.
	 * @param n current node
	 * @return ArrayList containing the SCC
	 */
	private ArrayList<ArrayList<Node>> getSingleSCC(Node n)
	{
		int sucId = -1;
		indexMap.put(n, index);
		lowMap.put(n, index);
		index++;
		nodeStack.push(n);
		unvisited.deleteNode(n);
		ArrayList<ArrayList<Node>> res = new ArrayList<>();
		
		//For every edge of given node check if it has visited before and according to that set 
		//the lowMap of that node
		ArrayList<Edge> successors = n.getEdges();
		for(Edge e : successors)
		{
			Node suc = e.getDestinationNode();
			sucId = suc.getId();
			suc = graph.getNodeByID(sucId);
			
			if(unvisited.contains(suc))
			{
				res.addAll(getSingleSCC(suc));
				lowMap.put(n, Math.min(lowMap.get(n), lowMap.get(suc)));
			}
			else if(nodeStack.contains(suc))		//If contained -> back edge; if not -> cross edge (= has to be ignored) 
			{
				lowMap.put(n, Math.min(lowMap.get(n), indexMap.get(suc)));
			}
		}
		
		if(lowMap.get(n).equals(indexMap.get(n)))
		{
			ArrayList<Node> scc = new ArrayList<>();
			
			//pop every node from stack and save it until the next node on stack is the current node
			while(nodeStack.peek() != n)
			{
				Node temp = nodeStack.pop();
				scc.add(temp);
			}
			
			scc.add(nodeStack.peek());
			
			if(!scc.isEmpty())
			{
				//reverse Stack so that the SCC will be in the same order the graph was traversed through
				Collections.reverse(scc);
				res.add(scc);
			}
			
			nodeStack.pop();
		}
		
		return res;
	}
	
	/**
	 * converts the result of tarjans algorithm back into DirectedWeightedGraphs and saves them in an ArrayList
	 * @param sccNodeLists result of tarjans algorithm
	 * @return ArrayList containing all found SCCs as DirectedWeightedGraphs
	 */
	private ArrayList<DirectedWeightedGraph> convertToGraph(ArrayList<ArrayList<Node>> sccNodeLists)
	{
		ArrayList<DirectedWeightedGraph> sccList = new ArrayList<>();
		for(ArrayList<Node> arrLNodes : sccNodeLists)
		{
			DirectedWeightedGraph graph = new DirectedWeightedGraph();
			
			for(Node n : arrLNodes)
			{
				graph.addEdgeRefactoredNode(n, arrLNodes);
			}
			
			sccList.add(graph);
		}
		
		return sccList;
	}
	
	/**
	 * Display all found SCCs
	 */
	public void displaySCCS()
	{
		if(result.isEmpty())
		{
			throw new NullPointerException("No Graph has been analyzed yet.");
		}
		else
		{
			GraphVisualizer graphVis = new GraphVisualizer();
			ArrayList<DirectedWeightedGraph> sccList = this.convertToGraph(result);
			
			System.out.println("\nStrongly Connected Components:\n");
			graphVis.displayMultipleGraphs(sccList);
		}
	}
}