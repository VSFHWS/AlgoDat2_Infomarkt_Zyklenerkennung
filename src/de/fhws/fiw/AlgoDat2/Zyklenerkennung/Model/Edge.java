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
	
	/**
	 * Gets the source node
	 * @return source node
	 */
	public Node getSourceNode()
	{
		return this.sourceNode;
	}
	
	/**
	 * Gets the destination node
	 * @return destination node
	 */
	public Node getDestinationNode()
	{
		return this.destinationNode;
	}
	
	/**
	 * Gets the weight of the edge
	 * @return weight of the edge
	 */
	public double getWeight()
	{
		return this.weight;
	}
}