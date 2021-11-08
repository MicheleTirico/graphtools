package graphTool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkDGS;

import handleFile.handleFolder;

 
public class StoreNetwork {

	private boolean run , storeStart  , storeStepZip , isStartNameCommon;
	private String 	nameFolder, nameFile , pathFolder ,
					path , 
					pathStart, pathStep , pathStepZip 
					;
	
	FileSinkDGS fsd;
	private Graph graph ;
	
 
	
	public StoreNetwork (boolean run ,  boolean storeStart , boolean isStartNameCommon ,  boolean storeStepZip, 
			String path , String nameFolder, String nameFile ,
			Graph graph) {
		
		this.run = run;
		this.storeStart = storeStart ;
		this.isStartNameCommon = isStartNameCommon ;
		this.storeStepZip = storeStepZip ;
		this.path = path;
		this.nameFolder = nameFolder ; 
		this.nameFile = nameFile ;
		this.graph = graph ;

		if ( run ) {
			handleFolder hF = new handleFolder(path) ;
			pathFolder = hF.createNewGenericFolder(nameFolder);
		
			if ( isStartNameCommon )
				pathStart = pathFolder + "/" + "start.dgs" ;			
			else 
				pathStart = pathFolder + "/" + nameFile + "start.dgs" ;
				
			pathStep = pathFolder + "/" + nameFile + "step.dgs" ;
			pathStepZip = pathStep+".zip" ;
			
//			System.out.println(pathStart +  "\n"+  pathStep +  	"\n"+ pathStepZip 	);
			fsd = new FileSinkDGS() ;
			graph.addSink(fsd);
		}
	
	}
	
	public static void storeGraph ( String path  , String nameFile , Graph graph ) throws IOException { 
		path = path + File.separator + nameFile + ".dgs" ;	
		FileSinkDGS fsd = new FileSinkDGS() ;
		graph.addSink(fsd);
		graph.write(fsd, path);	
	}
	
	public static void storeGraph ( String path  , Graph graph ) throws IOException { 	
		FileSinkDGS fsd = new FileSinkDGS() ;
		graph.addSink(fsd);
		graph.write(fsd, path);
		
	}

	public void init() throws IOException {
		if ( run ) {
			if ( storeStart)
				graph.write(fsd, pathStart);
			
			if (storeStepZip )	
				fsd.begin(new GZIPOutputStream(new FileOutputStream(pathStepZip)));
			else
				fsd.begin(pathStep);
		}
	}
	
	public void close ( ) throws IOException {
		if (run )
			fsd.end();
	}
	
	public  String getPathStepZip () {
		return pathStepZip ;
	}

}
