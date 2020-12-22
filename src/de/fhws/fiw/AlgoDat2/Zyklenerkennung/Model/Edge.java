package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model;

public class Edge
{
	private Node sourceNode;
	private Node destinationNode;
	double weight;
	
	public Edge(Node sourceNode, Node destinationNode, double weight)
	{
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.weight = weight;
	}
	
	public Edge(Node sourceNode, Node destinationNode)
	{
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.weight = 0d;
	}
	
	public Node getSourceNode()
	{
		return this.sourceNode;
	}
	
	public Node getDestinationNode()
	{
		return this.destinationNode;
	}
	
	public double getWeight()
	{
		return this.weight;
	}
}
