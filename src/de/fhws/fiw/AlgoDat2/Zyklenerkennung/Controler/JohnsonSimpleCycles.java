package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	private List<Stack<Integer>> circuits;
	private Stack<Integer> processed;
	private HashSet<Integer> blockedSet;
	private Map<Integer, Integer> blockedMap;

	
	public JohnsonSimpleCycles()
	{
		circuits = new ArrayList<Stack<Integer>>();
		processed = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
	}
	
	public boolean hasSimpleCycles(DirectedWeightedGraph graph)
	{
		boolean hasSimpleCycles =  getSimpleCycles(graph);
		
		circuits = new ArrayList<>();
		
		return hasSimpleCycles;
	}
	
	public int getAmountSimpleCycles(DirectedWeightedGraph graph)
	{
		getSimpleCycles(graph);
		int amountCycles = circuits.size();
		
		circuits = new ArrayList<>();
		
		return amountCycles;
	}
	
	private boolean getSimpleCycles(DirectedWeightedGraph graph)
	{
		boolean hasCirciuts = false; 
		TarjanSCC tarjan = new TarjanSCC(graph);
		ArrayList<DirectedWeightedGraph> sccs =  tarjan.getSCCs();
		
		for(DirectedWeightedGraph g : sccs)
		{
			if(controlJohnsonAlgorithm(g))
				hasCirciuts = true;
			
			cleanup();
		}
		
		return hasCirciuts;
	}
	
	private boolean controlJohnsonAlgorithm(DirectedWeightedGraph graph)
	{
		boolean hasCirciuts = false; 
		
		Node n = graph.getFirstNode();
		hasCirciuts = processJohnsonAlgorithm(graph, n, n.getId());
		
		return hasCirciuts;
	}
	
	private boolean processJohnsonAlgorithm(DirectedWeightedGraph graph, Node n, int startIndex)
	{
		int amountCyclesInGraph = 0;
		int inputNodeID = n.getId();
		boolean isPartOfCycle = false;
		processed.push(inputNodeID);
		blockedSet.add(inputNodeID);
		
		ArrayList<Edge> successors = n.getEdges();
		int successorID = -1;
		Iterator<Edge> successorIT = successors.iterator();
		while(successorIT.hasNext())
		{
			Edge e = successorIT.next();
			successorID = e.getDestinationNode().getId();
			Node successor = graph.getNodeByID(successorID);
			
			if(!(successorID == startIndex))
			{
				if(!blockedSet.contains(successorID))
				{
					if(processJohnsonAlgorithm(graph, successor, startIndex))
						isPartOfCycle = true;
					
					if(!successorIT.hasNext() && blockedMap.containsValue(successorID)
							&& !isPartOfCycle)
						blockedMap.put(successorID, inputNodeID);
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
				System.out.println(cycle);
				circuits.add(cycle);
			}
		}
		
		
		processed.pop();
		if(isPartOfCycle)
		{
			unblockNode(inputNodeID);
		}
		
		return isPartOfCycle;
	}
	
	
	private void unblockNode(int inputNodeID)
	{
		Set<Entry<Integer, Integer>> entrySet = blockedMap.entrySet();
		Iterator<Entry<Integer, Integer>> entrySetIterator = entrySet.iterator();
		
		blockedSet.remove(inputNodeID);
		while(entrySetIterator.hasNext())
		{
			Entry<Integer, Integer> entry = entrySetIterator.next();
			int entryKey = entry.getKey();
			int entryValue = entry.getValue();
			
			if(entryValue == inputNodeID)
			{
				blockedMap.remove(entryKey);
				unblockNode(entryKey);
				entrySetIterator = entrySet.iterator();
			}
			else if(entryKey == inputNodeID)
			{
				blockedMap.remove(entryKey);
				unblockNode(entryValue);
				entrySetIterator = entrySet.iterator();
			}
		}
	}
	
	private void cleanup()
	{
		processed = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
	}
}

