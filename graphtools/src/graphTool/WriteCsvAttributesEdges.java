package graphTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;

import handleFile.expCsv;

public class WriteCsvAttributesEdges {

	private Graph g ;
	private String[] listAttr ;
	private String pathStore ; 
	
	public WriteCsvAttributesEdges ( Graph g , String[] listAttr , String pathStore ) {
		this.g = g ;
		this.listAttr = listAttr;
		this.pathStore = pathStore ; 
	}
	
	public void compute ( ) throws IOException {
		if (new File(pathStore).exists())			new File(pathStore).delete();

		FileWriter fw = new FileWriter(pathStore , true);
 		String header = "id;" ;
 		for ( String a : listAttr ) header += a + ";";
		expCsv.addCsv_header( fw , header ) ;
 		for ( Edge e : g.getEachEdge() ) {
			Object test = e.getAttribute("scale");
 			String[] line = new String [1 + listAttr.length] ;
			int pos = 0 ;
			line[pos++] = e.getId().toString() ;
			if (test == null ) {
				for ( String a : listAttr )   {
					double val = e.getAttribute(a) ;
					line[pos++] = String.format("%.3f",val) ;
				}
				expCsv.writeLine(fw, Arrays.asList(  line ) ,';' ) ;				
				}
		}
		fw.close();
		
	}
	
	public static void main(String[] args) throws IOException {
//		int numNodes = 100 , seed = 1;
//		int[] sizeGrid = new int[] {10,10};
//		String pathStore = "/home/mtirico/test/ciao.csv";
//		
//		//gabriel
//		GabrielGenerator gg = new GabrielGenerator("gabriel", seed, numNodes, sizeGrid ) ;
//		gg.compute();
//		
//		Graph g = gg.getGraph();
//		
//		for ( Edge e : g.getEachEdge() ) {
//			Node n1 = e.getNode0() , n2 = e.getOpposite(n1);
//			double len = GraphTool.getDistGeom(n1, n2);
//			e.addAttribute("length", len);
//		}
//		new WriteCsvAttributesEdges( g , new String[] {"length"} ,pathStore).compute();;
//		System.out.println("finish");
		
	}

}
