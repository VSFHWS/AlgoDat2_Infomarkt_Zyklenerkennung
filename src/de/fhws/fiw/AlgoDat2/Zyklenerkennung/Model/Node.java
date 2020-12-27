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
	
	public void removeEdge(Node inputNode)
	{
		for(Iterator<Edge> edgesIter = this.getEdges().iterator(); edgesIter.hasNext();)
		{
			Edge edge = edgesIter.next();
			Node dest = edge.getDestinationNode();
			if(dest.getId() == inputNode.getId())
				edgesIter.remove();
		}
	}
}
