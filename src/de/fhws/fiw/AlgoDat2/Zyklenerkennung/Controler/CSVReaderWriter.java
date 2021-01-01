package de.fhws.fiw.AlgoDat2.Zyklenerkennung.Controler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReaderWriter
{
	
	/**
	 * Returns the values of a csv file as String values
	 * @param filePath	path of the csv file
	 * @param columnNo	requested column number of the file
	 * @param delimiter	delimiter which seperates the values from one another
	 * @param csvHasHeader	determines if the file has headers
	 * @return	the column of a csv file as ArrayList<String>
	 * @throws FileNotFoundException if file doesn't exist in path
	 * @throws IOException	if some IO operation went wrong
	 */
	public ArrayList<String> getCSVColumnAsString(String filePath, int columnNo, String delimiter, boolean csvHasHeader)
			throws FileNotFoundException, IOException
	{
		return getCSVContent(filePath, columnNo, delimiter, csvHasHeader);
	}
	
	/**
	 * Returns the values of a csv file as Double values
	 * @param filePath	path of the csv file
	 * @param columnNo	requested column number of the file
	 * @param delimiter	delimiter which seperates the values from one another
	 * @param csvHasHeader	determines if the file has headers
	 * @return	the column of a csv file as ArrayList<Double>
	 * @throws FileNotFoundException if file doesn't exist in path
	 * @throws IOException	if some IO operation went wrong
	 */
	public ArrayList<Double> getCSVColumnAsDouble(String filePath, int columnNo, String delimiter, boolean csvHasHeader)
			throws FileNotFoundException, IOException
	{
		ArrayList<String> csvContent = getCSVContent(filePath, columnNo, delimiter, csvHasHeader);
		ArrayList<Double> csvColumn = new ArrayList<>();
		double value;
		
		try
		{
			for(String row : csvContent)
			{
				value = Double.valueOf(row);
				csvColumn.add(value);
			}
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException("Not a double type value");
		}
		
		return csvColumn;
	}
	
	/**
	 * Returns the values of a csv file as Integer values
	 * @param filePath	path of the csv file
	 * @param columnNo	requested column number of the file
	 * @param delimiter	delimiter which seperates the values from one another
	 * @param csvHasHeader	determines if the file has headers
	 * @return	the column of a csv file as ArrayList<Integer>
	 * @throws FileNotFoundException if file doesn't exist in path
	 * @throws IOException	if some IO operation went wrong
	 */
	public ArrayList<Integer> getCSVColumnAsInteger(String filePath, int columnNo, String delimiter, boolean csvHasHeader)
			throws FileNotFoundException, IOException
	{
		ArrayList<String> csvContent = getCSVContent(filePath, columnNo, delimiter, csvHasHeader);
		ArrayList<Integer> csvColumn = new ArrayList<>();
		int value;
		
		try
		{
			for(String row : csvContent)
			{
				value = Integer.valueOf(row);
				csvColumn.add(value);
			}
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException("Not a float Integer value");
		}
		
		return csvColumn;
	}
	
	/**
	 * Returns the values of a csv file as Float values
	 * @param filePath	path of the csv file
	 * @param columnNo	requested column number of the file
	 * @param delimiter	delimiter which seperates the values from one another
	 * @param csvHasHeader	determines if the file has headers
	 * @return	the column of a csv file as ArrayList<Float>
	 * @throws FileNotFoundException if file doesn't exist in path
	 * @throws IOException	if some IO operation went wrong
	 */
	public ArrayList<Float> getCSVColumnAsFloat(String filePath, int columnNo, String delimiter, boolean csvHasHeader)
			throws FileNotFoundException, IOException
	{
		ArrayList<String> csvContent = getCSVContent(filePath, columnNo, delimiter, csvHasHeader);
		ArrayList<Float> csvColumn = new ArrayList<>();
		float value;
		
		try
		{
			for(String row : csvContent)
			{
				value = Float.valueOf(row);
				csvColumn.add(value);
			}
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException("Not a float type value");
		}
		
		return csvColumn;
	}
		
	/**
	 * Reads a csv file line by line and add it's value of the requested column to an ArrayList<String>
	 * @param filePath	path of the csv file
	 * @param columnNo	requested column number of the file
	 * @param delimiter	delimiter which seperates the values from one another
	 * @param csvHasHeader	determines if the file has headers
	 * @return	the column of a csv file as ArrayList<String>
	 * @throws FileNotFoundException if file doesn't exist in path
	 * @throws IOException	if some IO operation went wrong
	 */
	public ArrayList<String> getCSVContent(String filePath, int columnNo, String delimiter, boolean hasHeader)
			throws FileNotFoundException, IOException
	{
		ArrayList<String> csvColumn = new ArrayList<>();
		String[] splitLine;
		File file = new File(filePath);
		String line = "";
		
		//Generating BufferedReader for file
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			//overread the first line (Headers)
			if(hasHeader)
				br.readLine();
			
			while((line = br.readLine()) != null)
			{
				splitLine = line.split(delimiter);
				csvColumn.add(splitLine[columnNo - 1]);
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
		
		return csvColumn;
	}
			
				
	/**
	 * Reads a file and returns the whole line
	 * @param filePath	path of the csv file
	 * @return	line of the csv file as a String
	 * @throws FileNotFoundException if file doesn't exist in path
	 * @throws IOException	if some IO operation went wrong
	 */
	public String getCSVLineAsString(String filePath)
			throws FileNotFoundException, IOException
	{
		File file = new File(filePath);
		String line = "";
		
		if(file.exists())
		{
			try(BufferedReader br = new BufferedReader(new FileReader(file));)
			{
				line += br.readLine() + "\n";// + System.lineSeparator();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return line;
	}
	
	/**
	 * 
	 * @param filePath	path of the csv file
	 * @param input		line that is supposed to be added to a file
	 * @param delimiter	delimiter which separates each line from one another
	 * @param overwriteEnabled	determines whether the file should be updated or overwritten
	 */
	public void writeToCSV(String filePath, String input, String delimiter, boolean overwriteEnabled)
	{
		String[] inputLines = input.split(delimiter);
		
		for(String line : inputLines)
		{
			writeCSVLine(filePath, line, overwriteEnabled);
			overwriteEnabled = false;
		}
		
	}
	
	/**
	 * Adds a line to a file.  
	 * @param filePath path of the file
	 * @param inputLine	line which is supposed to be added to the file
	 * @param overwriteEnabled determines whether the file should be updated or overwritten
	 */
	public void writeCSVLine(String filePath, String inputLine, boolean overwriteEnabled)
	{
		String line = "";
		
		line += inputLine + System.lineSeparator();
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, !overwriteEnabled));)
		{
			bw.write(line);
			bw.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
