package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.io.IOException;
import java.util.ArrayList;

import de.fhws.fiw.AlgoDat2.Zyklenerkennung.Model.DirectedWeightedGraph;
import de.fhws.fiw.AlgoDat2.Zyklenerkennung.View.GraphVisualizer;

public class CycleDetection
{

	public static void main(String[] args)
	{
		generateCSVGraphs();
		
		DirectedWeightedGraph graph = createDirectedGraph("graph1.csv");
		
		getSCCs(graph);
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
		GraphVisualizer graphVis = new GraphVisualizer();
		System.out.println("Generating graph object from " + filePath + " ....");
		DirectedWeightedGraph graph = new DirectedWeightedGraph();
		graph.fromFile(filePath, false, false);
		
		System.out.println("Graph has successfully been generated.");
		graphVis.displayGraph(graph);
		
		return graph;
	}
	
	public static void getSCCs(DirectedWeightedGraph graph)
	{
		TarjanSCC tarjanSCC = new TarjanSCC(graph);
		ArrayList<DirectedWeightedGraph> sccList = tarjanSCC.getSCCs();
		GraphVisualizer graphVis = new GraphVisualizer();
		
		for(DirectedWeightedGraph g : sccList)
		{
			graphVis.displayGraph(g);
		}
	}
}