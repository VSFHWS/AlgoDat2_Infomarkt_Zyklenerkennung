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
	
	/**
	 * Get Nodes (and weights if included) from csv and add them to the graph
	 * @param filePath	path of csv file including nodes/edges
	 * @param weightIncluded boolean indicating whether weight is included in file or not
	 * @param csvHasHeader	boolean indicating whether file has header
	 */
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
				
				//create new Node if id doesn't exists yet. But get the node if it exists
				if(!this.contains(sourceID))
					source = new Node();
				else
					source =  getNodeByID(sourceID);
				
				source.setId(nodesSource.get(i));
				
				//query to avoid nullpointer exceptions
				if(i < nodesDestination.size())
				{
					Node dest = new Node();
					dest.setId(nodesDestination.get(i));
					if(weightIncluded)
						source.addDirectedEdge(dest, weights.get(i));
					else
						source.addDirectedEdge(dest);
				}
				
				//add source node if it doesn't exist yet
				if(!this.contains(sourceID))
					nodes.add(source);
			}
			
			addDestinationsToNodesIfMissing(nodesDestination);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Add nodes as destinations which haven't been added yet
	 * @param destinations List of possible destinations
	 */
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
	
	/**
	 * Add node to graph
	 * @param n new node
	 */
	public void addNode(Node n)
	{
		nodes.add(n);
	}
	
	/**
	 * Takes a node and all available Nodes that could be destinations. This method is used
	 * to change/refactor a graph or to help creating a new graph from an existing one.
	 * @param inputNode node which is the source node of the edge
	 * @param availableNodes all nodes that are available in the (new) graph
	 */
	public void addEdgeRefactoredNode(Node inputNode, ArrayList<Node> availableNodes)
	{
		Node newNode = setNode(inputNode);
		ArrayList<Edge> edgeList = inputNode.getEdges();
		
		//If inputNode is the only Node in that (sub-)graph
//		if(edgeList.isEmpty())
//		{
//			nodes.add(newNode);
//		}
		
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
	
	/**
	 * Gets the reference to a node with the same id if it exists and 
	 * creates a new one if it doesn't
	 * @param inputNode node that should be copied
	 * @return the new added node
	 */
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
	
	/**
	 * Get all nodes from graph
	 * @return ArrayList containing all nodes of the graph
	 */
	public ArrayList<Node> getNodes()
	{
		return this.nodes;
	}
	
	/**
	 * Get the first node in the graph/ArrayList
	 * @return the first node in the graph
	 */
	public Node getFirstNode()
	{
		return this.nodes.get(0);
	}
	
	/**
	 * Searches the graph for a node with a specific id and returns it
	 * @param id id of the node that should be found
	 * @return the found node - null if it wasn't found
	 */
	public Node getNodeByID(int id)
	{
		for(Node n : nodes)
		{
			if(n.getId() == id)
				return n;
		}
		
		return null;
	}
	

	/**
	 * Searches the graph for a specific node 
	 * @param n node being searched for
	 * @return true if it was found and alfs if it wasn't
	 */
	public boolean contains(Node n)
	{
		for(Node internNode : nodes)
		{
			if(internNode.equals(n)) return true;
		}
		
		return false;
	}
	
	/**
	 * Searches the graph for a node with a specific id
	 * @param id id of the node that should be found
	 * @return true if it was found and false if it wasn't
	 */
	public boolean contains(int id)
	{
		for(Node n : nodes)
		{
			if(n.getId() == id) return true;
		}
		
		return false;
	}
	
	/**
	 * deletes a node from the graph
	 * @param n node to be deleted
	 */
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
	
	/**
	 * Searches for a node by id and deletes it
	 * @param id id of the node to be deleted
	 */
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
	
	/**
	 * Deletes a node and all its edges.
	 * @param n node to be deleted
	 */
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
	
	/**
	 * Determines if graph is empty
	 * @return true if it is and false if it isn't
	 */
	public boolean isEmpty()
	{
		return nodes.isEmpty();
	}
}