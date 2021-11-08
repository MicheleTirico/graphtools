package graphTool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

public class ReadDGS {

	private Graph g ; 
	private double stepMax ;
	private FileSource fs ;
	private String pathStart, pathStep ;
	
 
	
	public ReadDGS (String id ,String pathStart, String pathStep) throws IOException {
		
		FileSource fs = new FileSourceDGS();
		g = new MultiGraph(id);
		g.setStrict(false);
		fs.addSink(g);
		
		fs.readAll(pathStart);
		fs.readAll(pathStep);
		
		stepMax = g.getStep();
	}

	public ReadDGS ( Graph grStart, String pathStep, int stepToStop ) throws IOException {	
		FileSource fs = new FileSourceDGS();
		 
		g = grStart ;
		g.setStrict(false);
		fs.addSink(g);

		fs.begin(new GZIPInputStream(new FileInputStream(pathStep)));
		double step = 0 ;
		while ( fs.nextStep() && step < stepToStop  ) 
			step = g.getStep();
//			System.out.println(step);
		
		stepMax = step;
	}

	public ReadDGS ( String pathStart, String pathStep, int stepToStop ) throws IOException    {	
		fs = new FileSourceDGS();
		g = new MultiGraph("g");
		g.setStrict(false);
		fs.addSink(g);

		fs.readAll(pathStart);
		fs.begin(new GZIPInputStream(new FileInputStream(pathStep)));
		double step = 0 ;
		while ( fs.nextStep() && step < stepToStop  ) {
			step = g.getStep();
		} 
		stepMax = step;
//		System.out.println("sim stop at -> " + step );
	}

	public ReadDGS (Graph grStart, String pathStep ) throws IOException {
		FileSource fs = new FileSourceDGS();
		 
		g = grStart ;
		g.setStrict(false);
		fs.addSink(g);

		fs.begin(new GZIPInputStream(new FileInputStream(pathStep)));
	} 
	
	public ReadDGS (String path ) throws IOException {
//		this.pathStart= pathStart ;  
		fs = new FileSourceDGS();
		g = new MultiGraph("id");
		g.setStrict(true); 
		fs.addSink(g);
		fs.readAll(path);
		

	}
	
	public ReadDGS ( String pathStart , String pathStep ) throws IOException {
		this.pathStart= pathStart ;  this.pathStep = pathStep ;
		fs = new FileSourceDGS();
		g = new MultiGraph("id");
		g.setStrict(false); 
		fs.addSink(g);
		
		fs.readAll(pathStart);

	//	fs.begin(new GZIPInputStream(new FileInputStream(pathStep)));
	}
	
	public void vizGraph (int stepToStop , int stepToPrint , int thread  ) throws IOException, InterruptedException {
		double step = 0 ;
		System.out.println(pathStep);
		System.out.println(pathStart);
		System.out.println(g);
		System.out.println(fs);
		g.display(false);
		fs.begin(new GZIPInputStream(new FileInputStream(pathStep)));
		while ( fs.nextStep() & step < stepToStop ) {
			if ( step / stepToPrint - (int)(step / stepToPrint ) < 0.0001 ) 
			System.out.println("step -> " + step + " / seeds -> "+ g.getAttribute("numberOfSeeds"));
			step = g.getStep();
			Thread.sleep( thread );
		}
		System.out.println("sim stop at -> " + step );
	}
	
	public double getStepMax ( ) {
		return stepMax ;
	}
				
	public Graph getGraph() {
		return g ;
	}

	public Graph getGraph (int stepToStop ) {
		System.out.print(" Sorry, not implemented ! " ); 
		return null;
	}
	
	public void storeGraph (String path ) {
		g.addAttribute("ui.screenshot", path + "/" + g.getId() + ".png");
	}
}
