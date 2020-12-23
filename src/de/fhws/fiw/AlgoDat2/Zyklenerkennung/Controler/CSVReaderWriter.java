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
	
	public ArrayList<String> getCSVColumnAsString(String filePath, int columnNo, String delimiter, boolean csvHasHeader)
			throws FileNotFoundException, IOException
	{
		return getCSVContent(filePath, columnNo, delimiter, csvHasHeader);
	}
	
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
		
		//Read column of csv as ArrayList<Float>
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
			
				
		//Read column of csv as ArrayList<String>
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
		
		public void writeToCSV(String filePath, String input, String delimiter, boolean overwriteEnabled)
		{
			String[] inputLines = input.split(delimiter);
			
			for(String line : inputLines)
			{
				writeCSVLine(filePath, line, overwriteEnabled);
				overwriteEnabled = false;
			}
			
		}
		
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
