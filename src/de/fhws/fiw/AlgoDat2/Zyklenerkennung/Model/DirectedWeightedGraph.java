package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler.CSVReaderWriter;

public class DirectedWeightedGraph
{
	private ArrayList<Node> nodes;
	
	public DirectedWeightedGraph()
	{
		nodes = new ArrayList<>();
	}
	
	public DirectedWeightedGraph(DirectedWeightedGraph graph)
	{
		this.nodes = new ArrayList<>(graph.nodes);
	}
	
	public void fromFile(String filePath, boolean weightIncluded, boolean csvHasHeader)
	{
		CSVReaderWriter csvrw = new CSVReaderWriter();
		String delimiter = ",";
		ArrayList<Integer> nodesSource = new ArrayList<>();
		ArrayList<Integer> nodesDestination = new ArrayList<>();
		ArrayList<Double> weights = new ArrayList<>();
		
		try
		{
			nodesSource = csvrw.getCSVColumnAsInteger(filePath, 1, delimiter, csvHasHeader);
			nodesDestination = csvrw.getCSVColumnAsInteger(filePath, 2, delimiter, csvHasHeader);
			if(weightIncluded)
			{
				weights = csvrw.getCSVColumnAsDouble(filePath, 3, delimiter, csvHasHeader);
			}
			
			for(int i = 0; i < nodesSource.size(); i++)
			{
				Node source;
				int sourceID = nodesSource.get(i);
				
				if(!this.arrayListContains(sourceID))
					source = new Node();
				else
					source =  getNodeByID(sourceID);
					
				
				source.setId(nodesSource.get(i));
				
				if(i < nodesDestination.size())
				{
					Node dest = new Node();
					dest.setId(nodesDestination.get(i));
					if(weightIncluded)
						source.addDirectedEdge(dest, weights.get(i));
					else
						source.addDirectedEdge(dest);
				}
				
				if(!this.arrayListContains(sourceID))
					nodes.add(source);
			}
			
			addDestinationsToNodesIfMissing(nodesDestination);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void addDestinationsToNodesIfMissing(ArrayList<Integer> destinations)
	{
		boolean exists;
		for(int d : destinations)
		{
			exists = false;
			
			for(Node n : nodes)
			{
				if(n.getId() == d)
					exists = true;
			}
			
			if(!exists)
			{
				Node newNode = new Node();
				newNode.setId(d);
				nodes.add(newNode);
			}
				
		}
	}
	
	public void addNode(Node n)
	{
		nodes.add(n);
	}
	
	public void addEdgeRefactoredNode(Node inputNode, ArrayList<Node> availableNodes)
	{
		Node newNode = setNode(inputNode);
		ArrayList<Edge> edgeList = inputNode.getEdges();
		
		//If inputNode is the only Node in that (sub-)graph
		if(edgeList.isEmpty())
		{
			nodes.add(newNode);
		}
		
		
		//Go through every edge of this node and check if Node is
		//going to be used in new graph
		for(Edge e : edgeList)
		{
			Node dest = e.getDestinationNode();
			
			for(Node avail : availableNodes)
			{
				int availID = avail.getId();
				int destID = dest.getId();
				if(availID == destID)
				{
					if(!this.contains(dest.getId()))
					{
						nodes.add(dest);
					}
					
					newNode.addDirectedEdge(dest);
					break;
				}
			}
		}
	}
	
	private Node setNode(Node inputNode)
	{
		Node newNode; 
		int inNodeID = inputNode.getId();
		String inNodeDescription = inputNode.getDescription();
		
		if(!this.contains(inNodeID))
		{
			newNode = new Node();
			newNode.setId(inNodeID);
			newNode.setDescription(inNodeDescription);
			nodes.add(newNode);
		}
		else
		{
			newNode = this.getNodeByID(inNodeID);
		}
		
		return newNode;
	}
	
	public ArrayList<Node> getNodes()
	{
		return this.nodes;
	}
	
	public Node getFirstNode()
	{
		return this.nodes.get(0);
	}
	
	public Node getNodeByID(int id)
	{
		for(Node n : nodes)
		{
			if(n.getId() == id)
				return n;
		}
		
		return null;
	}
	
	private boolean arrayListContains(int id)
	{
		for(Node n : nodes)
		{
			if(n.getId() == id) return true;
		}
		
		return false;
	}
	
	public boolean contains(Node n)
	{
		for(Node internNode : nodes)
		{
			if(internNode.equals(n)) return true;
		}
		
		return false;
	}
	
	public boolean contains(int id)
	{
		for(Node n : nodes)
		{
			if(n.getId() == id) return true;
		}
		
		return false;
	}
	
	public void deleteNode(Node n)
	{
		Node intern = null;
		for(Iterator<Node> it = nodes.iterator(); it.hasNext();)
		{
			intern = it.next();
			if(intern.equals(n)) 
				it.remove();
		}
	}
	
	public void deleteNode(int id)
	{
		for(Node n : nodes)
		{
			if(n.getId() == id)
			{
				nodes.remove(n);
				break;
			}
		}
	}
	
	public void purgeNode(Node n)
	{
		Node intern = null;
		for(Iterator<Node> it = nodes.iterator(); it.hasNext();)
		{
			intern = it.next();
			if(intern.equals(n))
			{
				it.remove();
			}
			else 
			{
				if(intern.hasEdge(n))
					intern.removeEdge(n);
			}
		}
	}
	
	public boolean isEmpty()
	{
		return nodes.isEmpty();
	}
}