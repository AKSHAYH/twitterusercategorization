import java.io.IOException;


public class PythonExecuter {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Process p = Runtime.getRuntime().exec("python sc2.py");
		
		int exitCode = p.waitFor();
	
	}
	public void execute() throws IOException, InterruptedException{
		Process p = Runtime.getRuntime().exec("python sc2.py");
		
		int exitCode = p.waitFor();
	
	}
}
