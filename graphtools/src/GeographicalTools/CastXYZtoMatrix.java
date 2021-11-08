package GeographicalTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import handleFile.expCsv;
import handleFile.handleFolder;

public class CastXYZtoMatrix {

		
	private   String path  , pathData   ,    pathStoreXYZ ,   pathStoreOut ;
	private boolean storeXYZ , storeOut , isNorm   ;
	double[][] matrix , matrixOut ; 
	double[][] vals ;
	double[] extX , extY , extZ , sizeXYZ  	 	; 
	int[] sizeMatrix ;
	int pixelPerCell ; 
	Cell[][] cells ;
	ArrayList<Double> 
			valsX = new ArrayList<Double> (),
			valsY = new ArrayList<Double> (),
			valsZ = new ArrayList<Double> () ;
	
	
	public CastXYZtoMatrix ( String pathData , int [] sizeMatrix , boolean isNorm ) throws IOException {
		this.pathData = pathData;
		this.sizeMatrix = sizeMatrix ;
		this.isNorm = isNorm ;
		updateExt();
	}
	
	public void setStore ( String pathStoreXYZ , String pathStoreOut , boolean storeXYZ , boolean storeOut ) {
		this.pathStoreXYZ = pathStoreXYZ ;
		this.pathStoreOut = pathStoreOut ;
		this.storeXYZ = storeXYZ ;
		this.storeOut = storeOut ;
	}

	public void setPixelPerCell ( int pixelPerCell ) {
		this.pixelPerCell = pixelPerCell ; 
	}
	
	public void compute () throws IOException {
//		System.out.println("ext x -> " + extX[0] + " " + extX[1]);
//		System.out.println("ext y -> " + extY[0] + " " + extY[1]);
//		System.out.println("ext z -> " + extZ[0] + " " + extZ[1]);
//		System.out.println("sizeGrid -> " + sizeXYZ[0] + " " + sizeXYZ[1] + " " + sizeXYZ[2]);
//		System.out.println("pixel per cell -> " + pixelPerCell );
		cells = new Cell[sizeMatrix[0]][sizeMatrix[1]];
		
		for ( int x = 0 ; x < sizeMatrix[0] ; x++ )
			for ( int y = 0 ; y < sizeMatrix[1] ; y++ ) 
				cells[x][y] = new Cell(new int [] {x,y});
	 
		for ( int x = 0 ; x < valsX.size(); x++ ) {
			int posX = (int) Math.floor( ( valsX.get(x) - extX[0] )  / pixelPerCell  ) ; 
			double z = valsZ.get(x) ; 
			int posY = (int) Math.floor( ( valsY.get(x) - extY[0] )  / pixelPerCell ) ;
			if ( posX <sizeMatrix[0] && posY < sizeMatrix[1] ) {
				if ( z > 0 )
					cells[posX][posY].putVal(z) ;
				else {
					cells[posX][posY].putVal(0) ;
					cells[posX][posY].setHasOneNegative();
				}			
			}
		}
		
		// put in matrix 
		matrix = new double[sizeMatrix[0]] [sizeMatrix[1]] ;
		matrixOut  = new double[sizeMatrix[0]] [sizeMatrix[1]] ;
		
		handleFolder.removeFileIfExist(new String[] {pathStoreXYZ, pathStoreOut});
		FileWriter fwXYZ = null , fwOut= null ;
		
		if ( storeXYZ) {
			fwXYZ = new FileWriter(pathStoreXYZ  , true) ; 
			expCsv.addCsv_header( fwXYZ ,  "x;y;z;" ) ;
		}
		if ( storeOut) {
 			fwOut = new FileWriter(pathStoreOut  , true) ; 
			expCsv.addCsv_header( fwOut ,  "x;y;z;" ) ;
		}
		
		// put value
		for ( int x = 0 ; x < sizeMatrix[0] ; x++ ) {
			for ( int y = 0 ; y < sizeMatrix[1] ; y++ ) { 
				double val = 0 , valOut = 0.5 ;
				try {
					
				if ( isNorm)  	val = cells[x][y].getAverage() / extZ[1] ;
				else 			val = cells[x][y].getAverage() ;
				matrix[x][y] = val; 
				if ( cells[x][y].hasOneNegative ) {
					matrixOut[x][y] = valOut ; 
				}
				if ( storeXYZ) expCsv.writeLine(fwXYZ, new String [] {Integer.toString(x) ,
						Integer.toString(y) ,
						Double.toString(val) } , ';' ) ;
				if ( storeOut) expCsv.writeLine(fwOut, new String [] {Integer.toString(x) ,
							Integer.toString(y) ,
							Double.toString(valOut) } , ';' ) ;	 
				} catch (NoSuchElementException e) {
					// TODO: handle exception
				}
			}
		}	
		if ( storeXYZ ) fwXYZ.close();
		if ( storeOut ) fwOut.close();
	}

// PRIVATE METHODS
// ------------------------------------------------------------------------------------------------	
	private void updateExt ( ) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(pathData));
	    String line;
	    while ((line = input.readLine()) != null)  {
	    	String[] split = line.split(" ");
	    	valsX.add(Double.parseDouble(split[0]));
	    	valsY.add(Double.parseDouble(split[1]));
	     	valsZ.add( Double.parseDouble(split[2]) ); 	    	
	    }
	    input.close();
	    // update all values 
	    extX = new double[] {Collections.min(valsX),Collections.max(valsX) };
	    extY = new double[] {Collections.min(valsY),Collections.max(valsY) };
	    extZ = new double[] {Collections.min(valsZ),Collections.max(valsZ) };
	    sizeXYZ = new double[] { extX[1] - extX[0] ,extY[1] - extY[0] , extZ[1] - extZ[0] } ;
	//    pixelPerCell = new double[] { sizeXYZ[0] /sizeMatrix[0], sizeXYZ[1] / sizeMatrix[1] } ;
	}
	

// GET
// ------------------------------------------------------------------------------------------------
	public double[][] getMatrix ( ) { return matrix ; } 
	public double[][] getMatrixOut ( ) { return matrixOut ; } 
	public int   getPixelPerCell () { return pixelPerCell; }
	
	public double[]   getExtX() { return extX; }
	public double[]   getExtY() { return extY; }
	public double[]   getExtZ() { return extZ; }
	
// RUN
// ------------------------------------------------------------------------------------------------
	public static void main(String[] args) throws IOException {
		String path = "/home/mtirico/GIS/alti/" ,
				nameFile = "leHavreAlti.xyz"  , // "test_02.xyz" , 	// "test.csv" , 
				nameFileStore = "expCsv.csv" ,
				pathData = path + "/"+nameFile ;
		new CastXYZtoMatrix(pathData, new int [] { 512 , 512 }, false );
	}
	
// CLASS CELL
// ------------------------------------------------------------------------------------------------
private class Cell  {
	private int [] coords ;
	private boolean hasOneNegative = false ;
	private List<Double> vals = new  ArrayList<Double> () ;
	public Cell ( int[] coords ) {
		this.coords = coords ;
	}
	public void setHasOneNegative ( ) {this.hasOneNegative = true ; }  ; 
	public boolean hasOneNegative ( ) {return hasOneNegative; }  ; 
	public void putVal (double val) { vals.add(val); }
	
	public double getMax ( ) { return Collections.max(vals); }
	public double getMin ( ) { return Collections.min(vals); }
	public double getAverage ( ) { return vals.stream().mapToDouble(a->a).average().getAsDouble() ; }
}
	


}
