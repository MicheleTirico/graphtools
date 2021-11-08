package graphTool;

import java.util.Collection;
import java.util.HashSet;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

public class BucketSet {
	
	private Graph g ;
	private Bucket[][] buckets ;
	private int []numBuckets;
	private int[] sizeGrid ;
	public BucketSet (Graph g , int[] numBuckets, int [] sizeGrid) {
		this.g = g ;
		this.numBuckets = numBuckets ;
		this.sizeGrid = sizeGrid ;
		initBuckets();
		for ( Node n : g.getEachNode()) {
			double[]coords = GraphPosLengthUtils.nodePosition(n);
			System.out.println(coords[0] + " "+ coords[1]);
			Bucket b = getBucket(coords);
			
			b.putNode(n);
		}
	}
	
	private void initBuckets () {
		System.out.println(numBuckets[0]);
		buckets = new Bucket[10] [10];
		for (int x =0 ; x < numBuckets[0] ; x++ )	{
			for (int y =0 ; y< numBuckets[1] ; y++ ) {
				double a =x * ( sizeGrid[0] / numBuckets[0] ) , b =  y * ( sizeGrid[1] / numBuckets[1] )  ;
				System.out.println(x + " " + y + " " + a + " " + b  );
				buckets[x][y] = new Bucket(new double[] { x * ( sizeGrid[0] / numBuckets[0] ) , y * ( sizeGrid[1] / numBuckets[1] ) });
			}
	}	
		}
	
	public Bucket getBucket(double[] coords ) {
		int x = (int) coords[0] /  numBuckets[0], y  = (int) coords[1] /  numBuckets[1] ; 
		return buckets[x][y];
	}
	
	public class Bucket {		
		double[]pos ;
		Collection<Node> nodes = new HashSet<Node>();
		
		public Bucket ( double [] pos) {
			this.pos = pos ;
		}
		
		public void putNode(Node n) { nodes.add(n);}
		public Collection<Node> getNodes () { return nodes; }
	}
}