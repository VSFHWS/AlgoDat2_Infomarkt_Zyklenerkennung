package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Edge;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Node;

public class JohnsonSimpleCycles
{
	private DirectedWeightedGraph graph;
	private ArrayList<Node> unvisited;
	private List<Stack<Integer>> circuits;
	private Stack<Integer> processed;
	private HashSet<Integer> blockedSet;
	private Map<Integer, Integer> blockedMap;

	
	public JohnsonSimpleCycles()
	{
		//this.unvisited = new ArrayList<>(graph.getNodes());
		circuits = new ArrayList<Stack<Integer>>();
		processed = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
	}
	
	public int getSimpleCycles(DirectedWeightedGraph graph)
	{
		this.graph = graph;
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
		int result = 0;
		
		Node n = graph.getFirstNode();
		result += processJohnsonAlgorithm(n, n.getId());
		
		return result;
	}
	
	private int processJohnsonAlgorithm(Node n, int startIndex)
	{
		int amountCyclesInGraph = 0;
		int inputNodeID = n.getId();
		boolean isPartOfCycle = false;
		processed.push(inputNodeID);
		blockedSet.add(inputNodeID);
		
		ArrayList<Edge> successors = n.getEdges();
		int successorID = -1;
		for(Edge e : successors)
		{
			Node successor = e.getDestinationNode();
			successorID = successor.getId();
			successor = graph.getNodeByID(successorID);
			
			if(!(successorID == startIndex))
			{
				if(!blockedSet.contains(successorID))
				{
					amountCyclesInGraph += processJohnsonAlgorithm(successor, startIndex);
				}
				else
				{
					blockedMap.put(successorID, inputNodeID);
				}
			}
			else
			{
				amountCyclesInGraph++;
				isPartOfCycle = true;
				Stack<Integer> cycle = (Stack<Integer>) processed.clone();
				cycle.push(successorID);
				System.out.println(reverseStack(cycle));
				
				blockedSet.remove(inputNodeID);
			}
		}
		
		
		processed.pop();
		if(!isPartOfCycle)
		{
			addToBlockedMapIfNeeded(n);
		}
		
		return amountCyclesInGraph;
	}
	
	private Stack<Integer> reverseStack(Stack<Integer> cycle)
	{
		Stack<Integer> reversedStack = new Stack<>();
		
		while(!cycle.isEmpty())
		{
			reversedStack.push(cycle.pop());
		}
		
		return reversedStack;
	}
	
	private void addToBlockedMapIfNeeded(Node inputNode)
	{
		int inputNodeID = inputNode.getId();
		ArrayList<Edge> successors = inputNode.getEdges();
		Set<Entry<Integer, Integer>> blocked = blockedMap.entrySet();
		
		for(Entry<Integer, Integer> entry : blocked)
		{
			int entryValue = entry.getValue();
			
			for(Edge successor : successors)
			{
				int successorID = successor.getDestinationNode().getId();
				
				if(successorID == entryValue)
					blockedMap.put(entryValue, inputNodeID);
			}
		}
	}
}

