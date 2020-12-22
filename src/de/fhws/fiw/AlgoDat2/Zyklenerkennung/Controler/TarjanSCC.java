package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Edge;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Node;

public class TarjanSCC
{
	private int index;
	private DirectedWeightedGraph graph;
	private Stack<Node> nodeStack;
	private Map<Node, Integer> indexMap;
	private Map<Node, Integer> lowMap;
	DirectedWeightedGraph newSCCGraph;
	DirectedWeightedGraph unvisited;
	
	public TarjanSCC(DirectedWeightedGraph graph)
	{
		this.index = 0;
		this.graph = graph;
		nodeStack = new Stack<>();
		indexMap = new HashMap<>();
		lowMap = new HashMap<>();
	}
	
	public ArrayList<DirectedWeightedGraph> getSCCs()
	{
		ArrayList<ArrayList<Node>> result = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> allNodes = graph.getNodes();
		unvisited = new DirectedWeightedGraph(graph);
		
		for(Node n : allNodes)
		{
			newSCCGraph = new DirectedWeightedGraph();
			if(indexMap.get(n) == null)
				result.addAll(this.getSingleSCC(n));
		}
		
		return convertToGraph(result);
	}
	
	private ArrayList<ArrayList<Node>> getSingleSCC(Node n)
	{
		int sucId = -1;
		indexMap.put(n, index);
		lowMap.put(n, index);
		index++;
		nodeStack.push(n);
		unvisited.deleteNode(n);
		ArrayList<ArrayList<Node>> res = new ArrayList<>();
		
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
			else if(nodeStack.contains(n))
			{
				lowMap.put(n, Math.min(lowMap.get(n), lowMap.get(suc)));
			}
		}
		
		if(lowMap.get(n).equals(indexMap.get(n)))
		{
			ArrayList<Node> scc = new ArrayList<>();
			Node temp =  nodeStack.peek();
			
			while(temp.equals(n))
			{
				temp = nodeStack.pop();
				scc.add(temp);
				temp = nodeStack.peek();
			}
			
			if(!scc.isEmpty())
				res.add(scc);
		}
		
		return res;
	}
	
	private ArrayList<DirectedWeightedGraph> convertToGraph(ArrayList<ArrayList<Node>> sccNodeLists)
	{
		ArrayList<DirectedWeightedGraph> sccList = new ArrayList<>();
		for(ArrayList<Node> arrLNodes : sccNodeLists)
		{
			DirectedWeightedGraph graph = new DirectedWeightedGraph();
			
			for(Node n : arrLNodes)
			{
				graph.addNode(n);
			}
			
			sccList.add(graph);
		}
		
		return sccList;
	}
}
