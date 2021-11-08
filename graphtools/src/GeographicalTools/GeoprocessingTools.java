package GeographicalTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

public class GeoprocessingTools {
	
	private Graph g ;
	private double[] xExt  , yExt  ; 
	private Collection<Double>  
		coordX = new HashSet<Double> () ,
		coordY = new HashSet<Double> () ;

	public GeoprocessingTools ( Graph g , boolean computeInfoCoords  ) {
		this.g = g ;
		if ( computeInfoCoords)		computeInfoCoords();
	}
 
	public void setGraph ( Graph g) { this.g = g ;	}

	public void computeInfoCoords (  ) {
		for ( Node n : g.getEachNode() ) { 
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			coordX.add(coords[0]);
			coordY.add(coords[1]);	        
		}
		xExt = new double[] { Collections.min(coordX) , Collections.max(coordX) };
		yExt = new double[] { Collections.min(coordY) , Collections.max(coordY) };	
	}
	
	public void alignGraph ( double[] minLignXY) {
		if ( xExt == null  || yExt ==null) { System.out.print("ERROR: graph not still computed");return ; }	
		double[] deltaXY = new double[] { xExt[0] - minLignXY[0]   , yExt[0] - minLignXY[1]  } ;
		for ( Node n : g ) {
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			n.addAttribute("xyz", coords[0] - deltaXY[0] , coords[1] - deltaXY[1] , 0 );
		}
		 xExt[0] =minLignXY[0] ;
		 xExt[1] = xExt[1] - deltaXY[0] ;
		 yExt[0] = minLignXY[1] ;
		 yExt[1] = yExt[1] - deltaXY[1] ;
	}
	
	public void incremCoords ( double incremSize ) {
		double minX = 10000000 , minY = 100000000, maxX = -1 , maxY = -1 ; 
		for ( Node n : g ) {
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			n.addAttribute("xyz", coords[0] * incremSize , coords[1] * incremSize , 0 );
			if ( coords[0] * incremSize < minX ) minX = coords[0] * incremSize ;
			if ( coords[0] * incremSize > maxX ) maxX = coords[0] * incremSize ; 
			if ( coords[1] * incremSize < minY ) minY = coords[1] * incremSize ;
			if ( coords[1] * incremSize > maxY ) maxY = coords[1] * incremSize ;
		}	
		 xExt[0] = minX ;
		 xExt[1] = maxX ;
		 yExt[0] = minY ;
		 yExt[1] = maxY ;
	}
	
	public void scaleGraph ( double[] scaleXY ) {
		double minX = 10000000 , minY = 100000000, maxX = -1 , maxY = -1 ; 
		for ( Node n : g ) {
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			n.addAttribute("xyz", coords[0] * scaleXY[0] , coords[1] * scaleXY[1] , 0 );
		}	
		 xExt[0] = minX ;
		 xExt[1] = maxX ;
		 yExt[0] = minY ;
		 yExt[1] = maxY ;
	}
	
	public void moveGraph ( double [] valXY ) {
		coordX = new HashSet<Double> () ;
		coordY = new HashSet<Double> () ;
		for ( Node n : g ) {
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			n.addAttribute("xyz", coords[0] + valXY[0] , coords[1] + valXY[1] , 0 );	
			coordX.add(coords[0] + valXY[0]);
			coordY.add(coords[1] + valXY[1]);	    
		}
		xExt = new double[] { Collections.min(coordX) , Collections.max(coordX) };
		yExt = new double[] { Collections.min(coordY) , Collections.max(coordY) };	
	}
	
	public void cutGraphOutSquare ( double[] xExt , double[] yExt ) {
		ArrayList<Node> listNodeRemove = new ArrayList<Node>() ;
		for ( Node n : g ) {
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			if ( coords[0] < xExt[0] | coords[0] > xExt[1] |coords[1] < yExt[0] | coords[1] > yExt[1] ) 	 listNodeRemove.add(n) ; 
		}
		listNodeRemove.stream().forEach(n -> g.removeNode(n));
	}
	
	public double[] getXext( ) {	return xExt; }
	public double[] getYext ( ) {	return yExt; }
	

	public static void main(String[] args) { System.out.print(new Object(){}.getClass());	

	}

}
