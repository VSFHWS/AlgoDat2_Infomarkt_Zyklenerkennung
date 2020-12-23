package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Edge;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Node;

public class JohnsonSimpleCycles
{
	private int index;
	private DirectedWeightedGraph graph;
	private ArrayList<Node> unvisited;
	List<Stack<Integer>> circuits;

	
	public JohnsonSimpleCycles(DirectedWeightedGraph graph)
	{
		this.index = 0;
		this.graph = graph;
		this.unvisited = new ArrayList<>(graph.getNodes());
		circuits = new ArrayList<Stack<Integer>>();
	}
	
	public int getSimpleCycles(DirectedWeightedGraph graph)
	{
		int amountCyclesInGraph = 0;
		TarjanSCC tarjan = new TarjanSCC(graph);
		
		ArrayList<DirectedWeightedGraph> sccs =  tarjan.getSCCs();
		
		for(DirectedWeightedGraph g : sccs)
		{
			amountCyclesInGraph += controlJohnsonAlgorithm(g);
		}
		
		return amountCyclesInGraph;
	}
	
	
	private int controlJohnsonAlgorithm(DirectedWeightedGraph graph)
	{
		ArrayList<Node> nodes = graph.getNodes();
		int result = 0;
		
		for(Node n : nodes)
		{
			result += processJohnsonAlgorithm(n);
		}
		
		return result;
	}
	
	private int processJohnsonAlgorithm(Node n)
	{
		int amountCyclesInGraph = 0;
		Stack<Integer> processed = new Stack<>();
		Stack<Integer> blockedSet = new Stack<>();
		Map<Node, Integer> blockedMap = new HashMap<>();
		
		processed.push(index);
		blockedSet.push(index);
		
		ArrayList<Edge> successors = n.getEdges();
		int sucID = -1;
		for(Edge e : successors)
		{
			Node suc = e.getDestinationNode();
			sucID = suc.getId();
			suc = graph.getNodeByID(sucID);
			
			if(unvisited.contains(suc))
			{
				amountCyclesInGraph += processJohnsonAlgorithm(suc);
				
			}
		}
		
		return amountCyclesInGraph;
	}
}
