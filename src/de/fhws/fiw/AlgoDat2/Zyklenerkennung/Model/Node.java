package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model;

import java.util.ArrayList;

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
		this.id = n.id;
		this.description = n.description;
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
		Edge newEdge = new Edge(this, destionationNode);
		edges.add(newEdge);
	}
	
	public void addDirectedEdge(Node destionationNode, double weight)
	{
		Edge newEdge = new Edge(this, destionationNode, weight);
		edges.add(newEdge);
	}
}
