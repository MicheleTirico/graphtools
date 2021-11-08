package graphTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

import handleFile.expCsv;


public class WriteCsvAttributesNodes {

	private Graph g ;
	private String[] listAttr ;
	private String pathStore ; 
	
	public WriteCsvAttributesNodes ( Graph g , String[] listAttr , String pathStore ) {
		this.g = g ;
		this.listAttr = listAttr;
		this.pathStore = pathStore ; 
	}
	
	public void compute ( ) throws IOException {
		if (new File(pathStore).exists())			new File(pathStore).delete();

		FileWriter fw = new FileWriter(pathStore , true);
 		String header = "id;x;y;" ;
 		for ( String a : listAttr ) header += a + ";";
		expCsv.addCsv_header( fw , header ) ;
		
		for ( Node n: g.getEachNode() ) {
			Object test = n.getAttribute("scale");
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			String[] line = new String [3 + listAttr.length] ;
			int pos = 0 ;
			line[pos++] = n.getId().toString() ;
			line[pos++] = String.format("%.3f",coords[0]) ;
			line[pos++] = String.format("%.3f",coords[1]) ;
			if (test == null ) {
				
				for ( String a : listAttr )   {
					double val = n.getAttribute(a) ;
					line[pos++] = String.format("%.3f",val) ;
				}
				expCsv.writeLine(fw, Arrays.asList(  line ) ,';' ) ;				
				}
		}
//		System.out.println("finish for " + g.getNodeCount() +" nodes/ exp -> " + expTest[0] + " " + expTest[1] + " " + expTest[2]);
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
//		BcAnalysis  bc = new BcAnalysis (g);
//		bc.compute("length");		
//		bc.computeNorm("CbNorm", "CbNorm01");
//		
//		new WriteCsvAttributesNodes( g , new String[] {"Cb", "CbNorm", "CbNorm01"} ,pathStore).compute();;
//		System.out.println("finish");
		
	}

}
