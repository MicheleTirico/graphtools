package graphViz;

import java.awt.Color;
import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GraphVizFillColor {
	private Graph g ;
	public GraphVizFillColor  ( Graph g ) {
		this.g = g ;
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		g.addAttribute("ui.quality");
		g.addAttribute("ui.antialias");
	}

	// old
	public void setColor ( String attribute ) {
		System.out.println("tip: change method to setColorNode");
		for ( Node n : g.getEachNode()) 	try {
			n.addAttribute("ui.color", Math.min(1.0, (double) n.getAttribute(attribute)));
		} catch (NullPointerException exc) {   }
	}

	public void setColorNode ( String attribute ) {
 		for ( Node n : g.getEachNode()) 	try {
			n.addAttribute("ui.color", Math.min(1.0, (double) n.getAttribute(attribute)));
		} catch (NullPointerException exc) {   }
	}

	public void setColorEdge ( String attribute ) {
			for ( Edge e : g.getEachEdge()) 	try {
			e.addAttribute("ui.color", Math.min(1.0, (double) e.getAttribute(attribute)));
		} catch (NullPointerException exc) {   }
	}

	public static ArrayList<float[]> getListHsb ( float[] extH , float s , float b , int numColors ) {
		ArrayList<float[]>  list = new ArrayList<float[]>  () ;
		float increm = (extH[1] - extH[0]) / numColors ;
		for ( float h = extH[0] ; h < extH[1] ; h = h + increm )			list.add(new float[] { h, s , b} ) ;
		return list;
	}

	public static ArrayList<float[]> getListHsb ( float h , float[] extS , float b , int numColors ) {
		ArrayList<float[]>  list = new ArrayList<float[]>  () ;
		float increm = (extS[1] - extS[0]) / numColors ;
		for ( float s = extS[0] ; s < extS[1] ; s = s + increm )	 		list.add(new float[] { h, s , b} ) ;
		return list;
	}

	public static ArrayList<float[]> getListHsb ( float h , float s, float[] ExtB , int numColors ) {
		ArrayList<float[]>  list = new ArrayList<float[]>  () ;
		float increm = (ExtB[1] - ExtB[0]) / numColors ;
		for ( float b = ExtB[0] ; b < ExtB[1] ; b = b + increm )			list.add(new float[] { h, s , b} ) ;
		return list;
	}

	public void setStyle ( int sizeNode , double sizeEdge , String fillColorEdge  , String fillColorNode  ) {
		g.addAttribute("ui.stylesheet", getStylesheet(sizeNode, sizeEdge, fillColorEdge, fillColorNode));
	}

	public void setColorVal ( double valMax , String attribute , int [] rgb ) {
		for ( Node n : g.getEachNode() ) {
			double test = n.getAttribute(attribute) ;
			if ( test < valMax)   n.setAttribute("ui.style", "fill-color: rgb(" + rgb[0]+ ","+ rgb[1] +","+ rgb[2] +");");
		}
	}

	public void setSpecificVizNode ( String attribute , int sizeNode, int [] rgb ) {
		for (Node n :g.getEachNode()) {
			try {
				boolean test = n.getAttribute(attribute) ;
				if (test == true) {
					System.out.println(test + " "+ n.getId());
					n.setAttribute("ui.style", "fill-color: rgb(" + rgb[0]+ ","+ rgb[1] +","+ rgb[2] +");");
					n.addAttribute("ui.size", sizeNode);
				};
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
		}
	}

	public void setSpecificVizEdge ( String attribute , int size, int [] rgb ) {
		for (Node n :g.getEachNode()) {
			try {
				boolean test = n.getAttribute(attribute) ;
				if (test == true) {
					System.out.println(test + " "+ n.getId());
					n.setAttribute("ui.style", "fill-color: rgb(" + rgb[0]+ ","+ rgb[1] +","+ rgb[2] +");");
					n.addAttribute("ui.size", size);
				};
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
		}
	}


	private String getStylesheet (int sizeNode , double sizeEdge , String fillColorEdge  , String fillColorNode ) {
		return "node {"+
				"	size: " + sizeNode + "px;"+
				 " fill-color:"+fillColorNode + " ;  "+
				"	fill-mode: dyn-plain;"+
				"}"+
				"node#setScale1 {	size: 0px; }" +
				"node#setScale2 {	size: 0px; }" +
				"edge {"+
				"	size: " + sizeEdge + "px;"+
				"	fill-color:"+fillColorEdge+" ;"+
				"	fill-mode: dyn-plain;"+
				"}" ;
	}

	public static  ArrayList<float[]>  castListHsbToRgb ( ArrayList<float[]> listHsb ) {
		ArrayList<float[]>  list = new ArrayList<float[]>  () ;

		for ( float[] hsb : listHsb)  {
		 	int cast = Color.HSBtoRGB( hsb[0], hsb[1] , hsb[2])  ;
			Color c = new Color(cast);
			list.add( new float[] { c.getRed() , c.getGreen() , c.getBlue()}  ) ; //castHsbToRgb(hsb) ) ;
		}
		return list;
	}

	// cast array of floats in hsb to corresponding rgb float
	private static float[] castHsbToRgb ( float[] hsb ) {
		Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		return new float[] {  c.getRed() ,    c.getGreen() ,    c.getBlue() };
	}

	// input arrayList of rgb colors, output fill-color rgb for graphstream
	public static String getStringRgb ( ArrayList<float[]> colors  ) {
		String colStr ="";
		int pos= 1;
		for ( float[] rgb : colors ) {
			colStr = colStr + "rgb(" + (int) rgb[0] +","+(int) rgb[1] +","+(int) rgb[2] +")";
			if ( pos < colors.size())  			colStr = colStr + ",";
			pos++;
		}
//		colStr = colStr + ";";
		return colStr ;
	}










	public Graph getGraph ( ) { return g ; }

	public void vizTh(int[] rgb0 , int[] rgb1 , String ind, double th) {
	for ( Node n : g.getEachNode()) {
		double val = n.getAttribute(ind);
		if (val > th ) {
			n.setAttribute("ui.style", "fill-color: rgb(" + rgb0[0]+ ","+ rgb0[1] +","+ rgb0[2] +");size-mode: dyn-size;");
			n.addAttribute("ui.size", "5");
		}

		else {
			n.setAttribute("ui.style", "fill-color: rgb(" + rgb1[0]+ ","+ rgb1[1] +","+ rgb1[2] +");size-mode: dyn-size;");
			n.addAttribute("ui.size", 1);
		}

	}

	}
}
