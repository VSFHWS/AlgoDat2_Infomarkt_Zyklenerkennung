package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model;

import java.util.ArrayList;
import java.util.Iterator;

public class Node
{
	private int id;
	private String description;
	private ArrayList<Edge> edges;
		
	public Node()
	{
		edges = new ArrayList<>();
	}
	
	public Node(Node n)
	{
		this.id = n.getId();
		this.description = n.getDescription();
		edges = new ArrayList<>();
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public ArrayList<Edge> getEdges()
	{
		return edges;
	}
	
	/**
	 * Adds an directed edge to the node
	 * @param destionationNode node to which an edge should be created
	 */
	public void addDirectedEdge(Node destionationNode)
	{
		for(Edge e : edges)
		{
			Node source = e.getSourceNode();
			Node dest = e.getDestinationNode();
			
			if((source.getId() == this.getId()) && (dest.getId() == destionationNode.getId()))
				return;
		}
		
		Edge newEdge = new Edge(this, destionationNode);
		edges.add(newEdge);
	}
	
	/**
	 * Adds an directed weighted edge to the node
	 * @param destionationNode node to which an edge should be created
	 * @param weight weight of the edge
	 */
	public void addDirectedEdge(Node destionationNode, double weight)
	{
		for(Edge e : edges)
		{
			Node source = e.getSourceNode();
			Node dest = e.getDestinationNode();
			
			if((source.getId() == this.getId()) && (dest.getId() == destionationNode.getId()))
				return;
		}
		
		Edge newEdge = new Edge(this, destionationNode, weight);
		edges.add(newEdge);
	}
	
	/**
	 * Checks if this node has an edge to the input node
	 * @param inputNode node to be searched for in the edges of this node
	 * @return true if the edge was found false if it wasn't
	 */
	public boolean hasEdge(Node inputNode)
	{
		for(Iterator<Edge> edgesIter = this.getEdges().iterator(); edgesIter.hasNext();)
		{
			Edge edge = edgesIter.next();
			Node dest = edge.getDestinationNode();
			if(dest.getId() == inputNode.getId())
				return true;
		}
		
		return false;
	}
	
	/**
	 * Removes an edge between this node and the input node
	 * @param inputNode node to be searched for in the edges of this node
	 */
	public void removeEdge(Node inputNode)
	{
		//used an iterator (also iterator.remove()) to avoid ConcurrentModificationException
		for(Iterator<Edge> edgesIter = this.getEdges().iterator(); edgesIter.hasNext();)
		{
			Edge edge = edgesIter.next();
			Node dest = edge.getDestinationNode();
			if(dest.getId() == inputNode.getId())
				edgesIter.remove();
		}
	}
}
