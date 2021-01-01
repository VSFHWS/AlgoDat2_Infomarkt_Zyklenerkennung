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
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.View.GraphVisualizer;

public class JohnsonSimpleCycles
{
	private List<Stack<Integer>> circuits;							//Every cycle that has been found in the given graph
	private Stack<Integer> processed;								//Stack that keeps track of which nodes already have been visited 
	private HashSet<Integer> blockedSet;							//Set of nodes that are blocked for further use
	private Map<Integer, Integer> blockedMap;						//Map auf nodes which block each other

	
	public JohnsonSimpleCycles()
	{
		circuits = new ArrayList<Stack<Integer>>();
		processed = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
	}
	
	/**
	 * Runs the johnson algorithm and determines if the given graph has an simple cycle
	 * @param graph graph to be analyzed
	 * @return true if the graph has at least one simple cycle and false if it doesn't
	 */
	public boolean hasSimpleCycles(DirectedWeightedGraph graph)
	{
		boolean hasSimpleCycles =  getSimpleCycles(graph);
		
		return hasSimpleCycles;
	}
	
	/**
	 * checks if the johnson algorithm has been issued before and if it was returns the 
	 * amount of cycles that where determined if not it runs the algorithm to determine that amount.
	 * @param graph graph to be analyzed
	 * @return amount of simple cycles in graph
	 */
	public int getAmountSimpleCycles(DirectedWeightedGraph graph)
	{
		if(circuits.isEmpty())
			getSimpleCycles(graph);
		
		int amountCycles = circuits.size();
		
		circuits = new ArrayList<>();
		
		return amountCycles;
	}
	
	/**
	 * Gets the strongly connected components and checks each for simple cycles.
	 * @param graph graph to be analyzed
	 * @return true if the graph has at least one simple cycle and false if it doesn't
	 */
	private boolean getSimpleCycles(DirectedWeightedGraph graph)
	{
		boolean hasCirciuts = false; 
		TarjanSCC tarjan = new TarjanSCC(graph);
		//Get Strongly connected components via tarjan algorithm
		ArrayList<DirectedWeightedGraph> sccs =  tarjan.getSCCs();
		
		tarjan.displaySCCS();
		
		//Issue johnson algorithm for every strongly connected component
		for(DirectedWeightedGraph g : sccs)
		{
			if(controlJohnsonAlgorithm(g))
				hasCirciuts = true;
			
			cleanup();
		}
		
		return hasCirciuts;
	}
	
	/**
	 * Use GraphVisualizer class to display found simple cycles in console
	 */
	public void displaySimpleCycles()
	{
		GraphVisualizer graphVis = new GraphVisualizer();
		graphVis.displayList(circuits);
	}
	
	/**
	 * Control/starting method where the algorithm is issued for every node 
	 * as starting node in SCC.
	 * @param graph a SCC as graph
	 * @return true if the graph has at least one simple cycle and false if it doesn't
	 */
	private boolean controlJohnsonAlgorithm(DirectedWeightedGraph graph)
	{
		boolean hasCirciuts = false; 
		
		while(!graph.isEmpty())
		{
			Node n = graph.getFirstNode();
			//process Johnson algorithm and set "hasCircuts" true if the algorithm finds a cycle
			if(processJohnsonAlgorithm(graph, n, n.getId()))
				hasCirciuts = true;
			
			graph.purgeNode(n);
			/* TODO Johnsons algorithm works now with the given SCCs. Next it should remove the
			 * already used nodes in the SCC and treat the remaining as new SCC */
		}
		
		
		return hasCirciuts;
	}
	
	/**
	 * Main part of the johnson algorithm.
	 * @param graph SCC as graph
	 * @param n	starting node
	 * @param startIndex index of start node to check if the next node in recursion is the starting node (= cycle)
	 * @return true if the graph has at least one simple cycle and false if it doesn't
	 */
	private boolean processJohnsonAlgorithm(DirectedWeightedGraph graph, Node n, int startIndex)
	{
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
			
			//if current node != starting node
			if(!(successorID == startIndex))
			{
				//if node is not blocked
				if(!blockedSet.contains(successorID))
				{
					//set boolean, if current node is part of a cycle
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
				isPartOfCycle = true;
				
				//Clone cylce and push it on stack for saving the cycles
				@SuppressWarnings("unchecked")
				Stack<Integer> cycle = (Stack<Integer>) processed.clone();
				cycle.push(successorID);
				circuits.add(cycle);
			}
		}
		
		//pop this node from stack
		processed.pop();
		//use unblockNode method if this node is part of a found cycle
		if(isPartOfCycle)
		{
			unblockNode(inputNodeID);
		}
		
		return isPartOfCycle;
	}
	
	/**
	 * Removes the given node from blockedMap and every node that was blocked/waiting to be unblocked
	 * by the given node.
	 * @param inputNodeID node that is going to be unblocked
	 */
	private void unblockNode(int inputNodeID)
	{
		//Get a set of entries of the blockedMap to iterate through every entry
		Set<Entry<Integer, Integer>> entrySet = blockedMap.entrySet();
		Iterator<Entry<Integer, Integer>> entrySetIterator = entrySet.iterator();
		
		//Remove given node from blockedMap
		blockedSet.remove(inputNodeID);
		//Check if there was a node blocked by the given node and recursively remove all the following nodes 
		//which are not going to be blocked anymore.
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
	
	/**
	 * Generate new objects so there is a clean new state
	 */
	private void cleanup()
	{
		processed = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
	}
}

