package GeographicalTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CreateLayerCellFromCsv {

	double[] [] matrix ;
	public CreateLayerCellFromCsv ( String pathData , int[] sizeGrid ) throws IOException {
		matrix= new double[ sizeGrid[0]][sizeGrid[1]];
		BufferedReader input = new BufferedReader(new FileReader(pathData));
	    String line;
	    input.readLine() ;
	    while ((line = input.readLine()) != null)  {
	    	String[] split = line.split(";");
	    	int x = Integer.parseInt(split[0]) ,  y = Integer.parseInt(split[1]) ; 
	    	double z = Double.parseDouble(split[2]);
	    	matrix[x][y] = z ;
 	    }
	}
	
	public double[][] getMatrix ( ) {
		return matrix;
		
	}
	
	public static void main(String[] args) { System.out.print("hello");	}
}
