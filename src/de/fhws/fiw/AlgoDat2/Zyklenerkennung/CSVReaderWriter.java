package de.fhws.fiw.AlgoDat2.Zyklenerkennung;

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
		//Read column of csv as ArrayList<Float>
		public static ArrayList<Integer> getCSVInteger(String filePath, int columnNo, String delimiter)
				throws FileNotFoundException, IOException
		{
			ArrayList<Integer> csvColumn = new ArrayList<>();
			String[] splitLine;
			File file = new File(filePath);
			String line = "";
			int value;
			
			//Generating BufferedReader for file
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				//overread the first line (Headers)
				br.readLine();
				
				while((line = br.readLine()) != null)
				{
					splitLine = line.split(delimiter);
					
					try
					{
						value = Integer.valueOf(splitLine[columnNo]);
					}
					catch(NumberFormatException e)
					{
						throw new NumberFormatException("Not a float type value");
					}
					
					csvColumn.add(value);
				}
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
			}
			
			return csvColumn;
		}
			
		//Read column of csv as ArrayList<Float>
		public static ArrayList<Float> getCSVFloat(String filePath, int columnNo, String delimiter)
				throws FileNotFoundException, IOException
		{
			ArrayList<Float> csvColumn = new ArrayList<>();
			String[] splitLine;
			File file = new File(filePath);
			String line = "";
			float value;
			
			//Generating BufferedReader for file
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				//overread the first line (Headers)
				br.readLine();
				
				while((line = br.readLine()) != null)
				{
					splitLine = line.split(delimiter);
					
					try
					{
						value = Float.valueOf(splitLine[columnNo]);
					}
					catch(NumberFormatException e)
					{
						throw new NumberFormatException("Not a float type value");
					}
					
					csvColumn.add(value);
				}
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
			}
			
			return csvColumn;
		}
		
		//Read column of csv as ArrayList<String>
		public static ArrayList<String> getCSVColumnString(String filePath, int columnNo, String delimiter)
				throws FileNotFoundException, IOException
		{
			ArrayList<String> csvColumn = new ArrayList<>();
			String[] splitLine;
			File file = new File(filePath);
			String line = "";
			String value;
			
			//Generating BufferedReader for file
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				//overread the first line (Headers)
				br.readLine();
				
				//Reading file line by line
				while((line = br.readLine()) != null)
				{
					//split row into seperated (by delimiter) columns
					splitLine = line.split(delimiter);
					
					//Get wanted column
					value = splitLine[columnNo];
					
					csvColumn.add(value);
				}
			}
			catch (RuntimeException e)
			{
				e.printStackTrace();
			}
			
			return csvColumn;
		}
		
		//Read column of csv as ArrayList<String>
		public static String getCSVLineAsString(String filePath)
				throws FileNotFoundException, IOException
		{
			File file = new File(filePath);
			String line = "";
			
			if(file.exists())
			{
				try(BufferedReader br = new BufferedReader(new FileReader(filePath));)
				{
					line += br.readLine() + System.lineSeparator();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			return line;
		}
		
		public void writeToCSV(String filePath, String input, boolean overWriteEnabled)
		{
			writeToCSV(filePath, input, ",", overWriteEnabled);
		}
		
		public void writeToCSV(String filePath, String input, String delimiter, boolean overwriteEnabled)
		{
			String[] inputLines = input.split("\n");
			
			for(String line : inputLines)
			{
				writeCSVLine(filePath, line, overwriteEnabled);
				overwriteEnabled = false;
			}
			
		}
		
		public void writeCSVLine(String filePath, String inputLine, boolean overwriteEnabled)
		{
			String line = "";
			
//			if(overwriteEnabled)
//			{
//				try
//				{
//					line = getCSVLineAsString(filePath);
//				}
//				catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
			
			line += inputLine;
			
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));)
			{
				if(overwriteEnabled)
					bw.write(line);
				else
					bw.append(line);
				
				bw.flush();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
}
