package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.View.GraphVisualizer;

public class CycleDetection
{

	public static void main(String[] args)
	{
		GraphVisualizer graphVis = new GraphVisualizer();
		
		generateCSVGraphs();
		
		DirectedWeightedGraph graph = createDirectedGraph("graph1.csv");
		graphVis.displayGraph(graph);
		
		countSimpleCycles(graph);
	}
	
	private static void generateCSVGraphs()
	{
		System.out.println("Generating test graph in .csv file ....");
		CSVReaderWriter csvrw = new CSVReaderWriter();
		
		String graph1 = 
			  "1,2" + "\n"
			+ "1,5" + "\n"
			+ "1,8" + "\n"
			+ "2,3" + "\n"
			+ "2,7" + "\n"
			+ "2,9" + "\n"
			+ "3,1" + "\n"
			+ "3,2" + "\n"
			+ "3,4" + "\n"
			+ "3,6" + "\n"
			+ "4,5" + "\n"
			+ "5,2" + "\n"
			+ "6,4" + "\n"
			+ "8,9" + "\n"
			+ "9,8";
		
		csvrw.writeToCSV("graph1.csv", graph1,"\n", true);
		System.out.println("CSV has successfully been created.");
	}
	
	private static DirectedWeightedGraph createDirectedGraph(String filePath)
	{
		System.out.println("Generating graph object from " + filePath + " ....");
		DirectedWeightedGraph graph = new DirectedWeightedGraph();
		graph.fromFile(filePath, false, false);
		
		System.out.println("Graph has successfully been generated.");
		
		return graph;
	}
	
	private static void countSimpleCycles(DirectedWeightedGraph graph)
	{
		JohnsonSimpleCycles jsc = new JohnsonSimpleCycles();
		//boolean hasSimpleCycles = jsc.hasSimpleCycles(graph);
		boolean hasSimpleCycles = true; /* TODO this constant ist only for testing - remove after finishing */
		System.out.println("----------------------------------------");
		
		if(hasSimpleCycles)
		{
			System.out.println("Graph has simple cycles!\n");
			System.out.println("Found cycles: ");
			
			int amountCycles = jsc.getAmountSimpleCycles(graph);
			
			System.out.println("----------------------------------------");
			System.out.println("Amount of cycles in Graph: " + amountCycles);
		}
		else
		{
			System.out.println("Graph doesn't have any simple cycles!\n");
		}
	}
}