import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Report {
	private File statText;
	private FileOutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter writer;
	
	
	public Report(String path){
		try{
		statText = new File(path+"TC Report.txt");
		int number = 1;
		while(statText.exists()){
			statText = new File(path+"TC Report "+ number +".txt");
			++number;
		}
		
		os = new FileOutputStream(statText);
		osw = new OutputStreamWriter(os); 
		writer = new BufferedWriter(osw);
		}
		catch(IOException ex){
			System.err.println("Problem creating the report file.");
		}
	}
	
	
	public void write(String text){
		try{
			writer.write(text);
		}
		catch(IOException ex){
			System.err.println("Problem writing to report file.");
		}
	}
	
	public void close(){
		try{
		writer.close();
		}
		catch(IOException ex){
			System.err.println("Problem closing the report file.");
		}
	}
	
}
