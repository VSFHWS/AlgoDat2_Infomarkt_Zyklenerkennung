package de.fhws.fiw.AlgoDat2.Zyklenerkennung.View;

import java.util.ArrayList;
import java.util.List;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Edge;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.Node;

public class GraphVisualizer
{
	/**
	 * Console output of a single graph
	 * @param graph graph to be displayed
	 */
	public void displayGraph(DirectedWeightedGraph graph)
	{
		ArrayList<Node> nodes = graph.getNodes();
		String descrS = "";
		String descrD = "";
		System.out.println("Graph description (format = source -- weight --> destination):");
		
		for(Node n : nodes)
		{
			ArrayList<Edge> edges = n.getEdges();
			if(edges.isEmpty() && nodes.size() == 1)
			{
				if(n.getDescription() != null)
					descrS = "(" + n.getDescription() + ")";
				else 
					descrS = "";
				
				System.out.println("edge " + n.getId() + descrS + " --" 
						+	0d + "--> " +  n.getId() + descrD);
			}
			
			for(Edge e : edges)
			{
				Node dest = e.getDestinationNode();
				
				if(n.getDescription() != null)
					descrS = "(" + n.getDescription() + ")";
				else 
					descrS = "";
				
				if(dest.getDescription() != null)
					descrD = "(" + n.getDescription() + ")";
				else 
					descrD = "";
				
				System.out.println("edge " + n.getId() + descrS + " --" 
						+	e.getWeight() + "--> " + dest.getId() + descrD);
			}
		}
		System.out.println("----------------------------------------");
	}
	
	/**
	 * Console output of multiple graphs.
	 * @param sccList ArrayList containing multiple graphs
	 */
	public void displayMultipleGraphs(ArrayList<DirectedWeightedGraph> sccList)
	{
		for(DirectedWeightedGraph g : sccList)
		{
			this.displayGraph(g);
		}
	}
	
	/**
	 * Console output of Lists 
	 * @param <E> Generic type of the given list
	 * @param list list containing the value to be printed
	 */
	public <E> void displayList(List<E> list)
	{
		for(E out : list)
		{
			System.out.println(out);
		}
	}
}
