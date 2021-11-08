package graphTool;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphReplay;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

import generic_tools.Tools;


public class GraphTool {

//	public static Graph mergeGraphs (String idNewGraph ,  Graph g1, Graph g2  ) {
//		Graph g = getGraphReplay(idNewGraph, g1, true, true) ;
//		for ( Node n : g2.getEachNode() ) {
//			Node n2 = g.addNode("g1_"+ n.getId());
//			for ( String attr : n.getAttributeKeySet() )
//				n2.addAttribute(attr, n.getAttribute(attr));
//		}
//		return g ;
//	}

	public static Graph getReplayElementNewId (Graph origin , String id_new_graph , String id_new_node, String id_new_edge) {
		Graph g_new = new SingleGraph(id_new_graph) ;
		for (Node n : origin.getEachNode()) {
			String id_node = n.getId();
			double[] coords = GraphPosLengthUtils.nodePosition(n);
			Node new_node = g_new.addNode(id_new_node + id_node) ;
			new_node.setAttribute("xyz", coords[0], coords[1], coords[2]);
		}
		for ( Edge e : origin.getEachEdge() ) {
			String id_edge = e.getId(), id_node_0 = e.getNode0().getId() , id_node_1 = e.getNode1().getId();
			Edge new_edge = g_new.addEdge(id_new_edge+id_edge, id_new_node+id_node_0, id_new_node + id_node_1) ;
		}		
		return g_new ;
	}
	/**
	 * the length is computed for as the sum of the geometrical distance between two opposite vertices
	 * @param p
	 * @return
	 */
	public static double getLengthPath (Path p) {
		double len = 0 ; 
		if (p == null)
			return 0 ; 
		else {
			for (Edge e : p.getEachEdge())
				len += GraphTool.getDistGeom(e.getNode0(), e.getNode1()) ;
		}
		return len;
	}

	/**
	 * the length is computed for as the sum of the sum of attributes of edges
	 * @param p
	 * @return
	 */
	public static double getLengthPath (Path p, String attribute) {
		double len = 0 ; 
		if (p == null)
			return 0 ; 
		else {
			for (Edge e : p.getEachEdge()) {
				double val = e.getAttribute(attribute);
				len += val ;
			}
		}
		return len;
	}
	
	/**
	 * get pairs of nodes
	 * @param g
	 * @param loops
	 * @param toShuffle
	 * @return
	 */
	public static ArrayList<Node[]> getPairs (Graph g , boolean loops , boolean toShuffle ) {
		ArrayList<Node[]> pairs = new ArrayList<Node[]> ();
		ArrayList<Node>listNodes = new ArrayList<Node>(g.getNodeSet());
		
		if (! loops ) {
			
			for ( int x = 0 ; x < g.getNodeCount() ; x++) 
				for ( int y = 0 ; y < g.getNodeCount() ; y++) 
					if (x > y) 		
						pairs.add(new Node[] {listNodes.get(x), listNodes.get(y) } );		
		}
		else {
			for ( int x = 0 ; x < g.getNodeCount() ; x++) 
				for ( int y = 0 ; y < g.getNodeCount() ; y++) 
					if (x >= y) 		
						pairs.add(new Node[] {listNodes.get(x), listNodes.get(y) } );				
		}
	
		if (toShuffle)
			Collections.shuffle(pairs);		
		
		return pairs;
	}
	
	public static Graph getGiantGraph ( Graph source , boolean setCoords ) {
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(source);
		Graph gr = new MultiGraph("gr2");
		for (Node n : cc.getGiantComponent() ) {
			Node n2 = gr.addNode(n.getId()) ;
			if ( setCoords ) {				
				double[] coords = GraphPosLengthUtils.nodePosition(n);
				n2.setAttribute("xyz",coords[0], coords[1] , 0);			
			}
		}
		
		for ( Node n : cc.getGiantComponent()) 
			for ( Edge e : n.getEdgeSet() ) {
				String id = e.getId() ;
				Node n0 = e.getNode0(), n1 = e.getNode1() ; 
				try {
					gr.addEdge(id , n0.getId() , n1.getId());
				} catch (Exception ex) { 	} 
		}
		
		return gr ;
	}
	
	public static ArrayList<Node> getNodesOfGiant ( String attributeGiant , Graph g , String idNode) {
		ArrayList<Node> nodes = new ArrayList<Node> () ;
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(g);
		cc.setCountAttribute(attributeGiant);
		cc.getConnectedComponentsCount(); 
//		System.out.print(g.getNode(idNode).getAttributeKeySet() );
		String t = g.getNode(idNode).getAttribute(attributeGiant).toString();
		for ( Node n : g.getEachNode() ) {
			String test = n.getAttribute(attributeGiant).toString() ;
			if ( test.equals(t)) nodes.add(n);
		}
		return nodes;
	}

		
	public static Set<Node> getNodesOfEdgeSet ( Collection<Edge> collEdges ) {
		Set<Node> set = new HashSet<Node> ();
		for ( Edge e : collEdges ) {
			set.add(e.getNode0()) ;
			set.add(e.getNode1()) ;
		}
		return set;
	}
	
	protected static Map getMapEdgeValue ( Graph gr , String attr ){
		Map map = new HashMap();
		for ( Edge e : gr.getEachEdge() ) {
			map.put(e, e.getAttribute(attr));
		}
		return map;
	}

//	public static ArrayList<Node> getOrdListNodesNear (Node source , Collection<Node> collNodes ) {
//		Map<Node,Double> mapNodeNearDist = new HashMap<>() ;	 
//		Iterator<Node> itNode = collNodes.iterator();
//		while ( itNode.hasNext() ) {
//			Node next = itNode.next();
//			if ( ! next.equals(source)) {
//				double dist = getDistGeom( next , source ) ;
//				mapNodeNearDist.put(next, dist);
//			}
//		}
//		return  new ArrayList <Node> ( Tools.getSortedMap(mapNodeNearDist).keySet() );  
//	}
	 
	public static Collection<Node> getNeigs (Node n) {
		Collection<Node> neigs = new HashSet<Node>();
		Iterator<Node> itNeig = n.getNeighborNodeIterator();
		while( itNeig.hasNext() ) 
			neigs.add( itNeig.next() );

		return neigs;
	}

	public static double getDistGeom ( Node n1 , Node n2 ) {		
		double [] 	coordN1 = GraphPosLengthUtils.nodePosition(n1) , 
					coordN2 = GraphPosLengthUtils.nodePosition(n2); 
		return  Math.pow(Math.pow( coordN1[0] - coordN2[0] , 2 ) + Math.pow( coordN1[1] - coordN2[1] , 2 ), 0.5 )  ;
	}
	
	public static double getDistGeom ( double [] 	coordN1 , double [] 	coordN2) {		
		return  Math.pow(Math.pow( coordN1[0] - coordN2[0] , 2 ) + Math.pow( coordN1[1] - coordN2[1] , 2 ), 0.5 )  ;
	}
	
	public static Graph getGraphReplay ( String idGraph, String idNodes, String idEdges , Graph source ) {
		Graph rep = new MultiGraph(idGraph) ; 
//		rep.setStrict(false);
		
			for ( Node nSource : source.getEachNode()  ) {		
				double [] nCoords = GraphPosLengthUtils.nodePosition(nSource);
				rep.addNode(idNodes +"_" + nSource.getId()).addAttribute("xyz", nCoords[0], nCoords[1] , 0 );
			}		
			for ( Edge e : source.getEachEdge() ) {
				String n0id = e.getNode0().getId() , n1id = e.getNode1().getId();
				Node n0rep = rep.getNode(idNodes +"_" + n0id) , n1rep = rep.getNode(idNodes +"_" + n1id) ;
				
				rep.addEdge(idEdges +"_" + e.getId() , n0rep , n1rep  ) ;
//				for (String at : e.getAttributeKeySet() ) {
//					double val = e.getAttribute(at) ;
//					rep.getEdge(e.getId()).setAttribute(at, val );
//				}
			}
			
	    return rep;
	}
	
	public static Graph getGraphReplay ( String id, Graph source , boolean setCoords , boolean setAttributesEdge ) {
		Graph rep = new MultiGraph(id) ; 
		rep.setStrict(false);
		GraphReplay replay = new GraphReplay("replay");
	    replay.addElementSink(rep);
	    replay.replay(source);
	    replay.removeElementSink(rep);
		
	    if ( setCoords ) 
	    	for ( Node nSource : source.getEachNode()  ) {
	    		double [] nCoords = GraphPosLengthUtils.nodePosition(nSource);
	    		rep.addNode(nSource.getId()).addAttribute("xyz", nCoords[0], nCoords[1] , 0 );
	    	}
	    
	    if (setAttributesEdge ) 
	    	for ( Edge e : source.getEachEdge() ) {
	    		for (String at : e.getAttributeKeySet() ) {
	    			double val = e.getAttribute(at) ;
	    			rep.getEdge(e.getId()).setAttribute(at, val );
	    		}
	    	}
	    
	    return rep;
	}

	public static Graph getGraphReplay ( boolean isSingleGraph,  String id, Graph source , boolean setCoords , boolean setAttributesEdge ) {
		Graph rep ;
		if ( isSingleGraph)
			rep = new SingleGraph(id) ; 
		else
			rep = new MultiGraph(id) ; 
				
		rep.setStrict(false);
		GraphReplay replay = new GraphReplay("replay");
	    replay.addElementSink(rep);
	    replay.replay(source);
	    replay.removeElementSink(rep);
		
	    if ( setCoords ) 
	    	for ( Node nSource : source.getEachNode()  ) {
	    		double [] nCoords = GraphPosLengthUtils.nodePosition(nSource);
	    		rep.addNode(nSource.getId()).addAttribute("xyz", nCoords[0], nCoords[1] , 0 );
	    	}
	    
	    if (setAttributesEdge ) 
	    	for ( Edge e : source.getEachEdge() ) {
	    		for (String at : e.getAttributeKeySet() ) {
	    			double val = e.getAttribute(at) ;
	    			rep.getEdge(e.getId()).setAttribute(at, val );
	    		}
	    	}
	    
	    return rep;
	}
	
	public static double getLengthEdge ( Edge e) {
		return getDistGeom(e.getNode0(), e.getNode1() );
	}

	public static void divideEdges ( Graph source , double lenMax ) {
		int idNewNodeInt = source.getNodeCount() , idNewEdgeInt = source.getEdgeCount() ;
		Collection<Edge> setEdge  = source.getEdgeSet()  ;
		for ( Edge e : setEdge ) {
			double len =  getLengthEdge(e) ; 
			if ( len > lenMax ) {
				
				Node n0 = e.getNode0() , n1 = e.getOpposite(n0) , oldNode = n0 ;
				double [ ] 	coords0 = GraphPosLengthUtils.nodePosition(n0) ,
							coords1 = GraphPosLengthUtils.nodePosition(n1) ;

				double 	lenX = -(coords0[0] - coords1[0]) ,
						lenY = -(coords0[1] - coords1[1]) ;		
				
				int numSeg = (int) Math.floor(len / lenMax) ;

				double  segX = lenX / numSeg ,
						segY = lenY / numSeg ;

				int p = 1 ;
				while ( p < numSeg ) {
					Node newNode = source.addNode("n"+Integer.toString(idNewNodeInt++));
					newNode.addAttribute("xyz", coords0[0] + ( p * segX ) , coords0[1] + ( p * segY) ,0);
					source.addEdge("n"+Integer.toString(idNewEdgeInt++), oldNode, newNode) ;
					oldNode = newNode;
					p++ ;
				} 
					try {
				source.addEdge("n"+Integer.toString(idNewEdgeInt++), oldNode, n1);
				} catch (Exception ex) {
					// TODO: handle exception
				}
				source.removeEdge(e);
	 
			}
		}
	}

	public static ArrayList<Node> getListRandomNodes ( Graph graph , int randomSeed , int numMax  ) {
		
		ArrayList<Node> list = new ArrayList<Node> ();
		ArrayList<Node> listNodes = new ArrayList<Node> () ;
		listNodes.addAll( graph.getNodeSet()) ;				
		int nodeCount = graph.getNodeCount();
		Random rd = new Random(randomSeed);
		int numNodes = 0 ;
	
		while ( numNodes < numMax) {	 
			double v = rd.nextDouble();
			int pos = (int) ( nodeCount * v );
			Node n = listNodes.get(pos);
			list.add(n);		
			numNodes++;
		}
		return list;		
	}	

	public static ArrayList<Node> getListNodes ( Graph graph ) {
		ArrayList<Node> listNodes = new ArrayList<Node> () ;
		listNodes.addAll( graph.getNodeSet()) ;		
		return listNodes ;
	}
	 
// VIZ ----------------------------------------------------------------------------------------------------------------------------------------------
	public static void setColor ( Graph g , int colMax ) {
		Color[] cols = new Color[30];
		for(int i=0;i< 30 ;i++) 
					cols[i]= Color.getHSBColor( (float) (Math.random()), 0.9f, 1f); 	
	
		 for(Node n : g ){ 
			 int col =   n.getDegree() ;// n.getNumber("color"); 
			 if ( col < colMax )
				 n.addAttribute("ui.style", 
						 " fill-color:rgba("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+",200);"
						 + "size:20px;"); 
		 } 
	}

// CECK X EDGE --------------------------------------------------------------------------------------------------------------------------------------
	public static Collection<Edge> getEdgeSetIntersectWithsegment ( double[] point0 , double[] point1 , Collection<Edge> edgeSet ){ 
		Set<Edge> set = new HashSet<Edge>();	 
		Iterator<Edge> itEdge = edgeSet.iterator();
		while( itEdge.hasNext()  ) {
			Edge e = itEdge.next();
			Node n0 = e.getNode0() ,n1 =e.getNode1();
			double[] 	coordsN0 = GraphPosLengthUtils.nodePosition(n0) ,
						coordsN1 = GraphPosLengthUtils.nodePosition(n1) ;
		
			if (isSegmentIntersect(point0, point1, coordsN0, coordsN1)) 
				set.add(e) ; 
		}
		return set ;
	}
	
	public static boolean isSegmentIntersect ( double[] point1 , double[] point2 , double[] point3  ,double[] point4  ) {
		double[] inters = getCoordIntersectionLine(point1 , point2 , point3  , point4 );
		if ( inters[0] == -10 ) 
			return false ;
			
		if (Math.max(point1[0], point2[0])  > inters[0] && Math.min(point1[0], point2[0]) < inters[0]		&&
				Math.max(point1[1], point2[1])  > inters[1] && Math.min(point1[1], point2[1]) < inters[1]		&&
				
				Math.max(point3[0], point4[0])  > inters[0] && Math.min(point3[0], point4[0]) < inters[0]		&&
					Math.max(point3[1], point4[1])  > inters[1] && Math.min(point3[1], point4[1]) < inters[1]	)
			return true ;
		else 
			return false ;
	}
	
	public static double[] getCoordIntersectionLine ( double[] point1 , double[] point2 , double[] point3  ,double[] point4 ) {
		double 	a1 = point2[1]- point1[1],
				b1 = point1[0] - point2[0],
				c1 = a1 * point1[0] + b1 * point1[1],
				a2 = point4[1]- point3[1],
				b2 = point3[0] - point4[0],
				c2 = a2 * point3[0] + b2 * point3[1],
				det = a1 * b2 - a2 * b1 ;
		
		if ( det == 0 ) {// 	System.out.println("lines are parallel");
			return new double[] { -10 , -10 }  ;
		}
		else 
			return new double[] { ( b2*c1 - b1*c2 )/det ,( a1*c2  -a2*c1 )/det } ; 
	}
	
	public static boolean isSegmentIntersecInEdgeSet ( double[] point0 , double[] point1 , Collection<Edge> edgeSet ){ 		 
		Iterator<Edge> itEdge = edgeSet.iterator();
		while( itEdge.hasNext()  ) {
			Edge e = itEdge.next();
			Node n0 = e.getNode0() , n1 =e.getNode1();
			
			double[] 	coordsN0 = GraphPosLengthUtils.nodePosition(n0) ,
						coordsN1 = GraphPosLengthUtils.nodePosition(n1) ;
 
			if (isSegmentIntersect( point0, point1, coordsN0, coordsN1) ) 
				return true ; 
		}
		return false ;
	}
	
	public static Edge getEgeIntersecInEdgeSet ( double[] point0 , double[] point1 , Collection<Edge> edgeSet ){ 		 
		Iterator<Edge> itEdge = edgeSet.iterator();
		while( itEdge.hasNext()  ) {
			Edge e = itEdge.next();
			Node n0 = e.getNode0() , n1 =e.getNode1();
			
			double[] 	coordsN0 = GraphPosLengthUtils.nodePosition(n0) ,
						coordsN1 = GraphPosLengthUtils.nodePosition(n1) ;
 
			if (isSegmentIntersect( point0, point1, coordsN0, coordsN1) ) 
				return e; 
		}
		return null ;
	}
        
    public static void main(String[] args){
        System.err.println("a");
    }
}
