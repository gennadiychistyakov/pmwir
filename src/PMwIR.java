import java.io.*;
import java.math.BigInteger;
import java.util.*;

import org.apache.commons.cli.*;

public class PMwIR {
	
	private Scanner in = new Scanner(System.in);
	private PrintWriter out = new PrintWriter(System.out);
	private int nextID;
	private int delay;
	private boolean promtMode;
	private boolean debugMode;
	private Registers registers;
	private Vector<Vector<Command>> programs;
	
	synchronized void write(String str) {
		out.print(str);
		out.flush();
	}
	
	synchronized String read() {
		return in.nextLine();
	}
	
	boolean isPromt() {
		return promtMode;
	}
	
	boolean isDebug() {
		return debugMode;
	}
	
	int getDelay() {
		return delay;
	}
	
	void startProcess(int program, BigInteger number) {
		new Process(this, nextID++, (Command[])programs.elementAt(program).toArray(new Command[programs.elementAt(program).size()]), registers, number);
	}
	
	private Command parse(String str) throws Exception {
		ArrayList<String> s = new ArrayList<String>(Arrays.asList(str.split("[\\(\\), ]")));
		Vector<String> rm = new Vector<String>();
		rm.add("");
		s.removeAll(rm);
		String[] tokens = s.toArray(new String[s.size()]);
		
		if(tokens[0].equals("Z"))
			return new Command(Command._Z, new BigInteger(tokens[1]), 0, null, 0, 0);
		if(tokens[0].equals("S"))
			return new Command(Command._S, new BigInteger(tokens[1]), 0, null, 0, 0);
		if(tokens[0].equals("T"))
			return new Command(Command._T, new BigInteger(tokens[1]), 0, new BigInteger(tokens[2]), 0, 0);
		if(tokens[0].equals("J"))
			return new Command(Command._J, new BigInteger(tokens[1]), 0, new BigInteger(tokens[2]), 0, Integer.valueOf(tokens[3]));
		if(tokens[0].equals("I"))
			return new Command(Command._I, new BigInteger(tokens[1]), 0, null, 0, 0);
		if(tokens[0].equals("O"))
			return new Command(Command._O, new BigInteger(tokens[1]), 0, null, 0, 0);
		if(tokens[0].equals("G"))
			return new Command(Command._G, new BigInteger(tokens[1]), 0, null, 0, 0);
		if(tokens[0].equals("P"))
			return new Command(Command._P, new BigInteger(tokens[1]), 0, null, 0, 0);
		if(tokens[0].equals("s"))
			return new Command(Command._s, new BigInteger(tokens[2]), 0, null, 0, Integer.valueOf(tokens[1]));
		return null;
	}
	
	private void init(String[] files, int delay, boolean promtMode, boolean debugMode) {
		this.nextID = 0;
		this.delay = delay;
		this.promtMode = promtMode;
		this.debugMode = debugMode;
		this.registers = new Registers();
		this.programs = new Vector<Vector<Command>>();
		
		for(int i = 0; i < files.length; i++) {
			programs.add(new Vector<Command>());
			int line = 0;
			try {
				Scanner fileReader = new Scanner(new FileReader(files[i]));
				while(fileReader.hasNext()) {
					line++;
					String str = fileReader.nextLine().trim();
					if(str.equals(""))
						continue;
					Command cmd = parse(str);
					programs.elementAt(i).add(cmd);
				}
				fileReader.close();
			} catch (FileNotFoundException e) {
				write("File " + files[i] + " not found\n");
			} catch (Exception e) {
				write("Some strange in file " + files[i] + " at line " + line + "\n");
			}
		}
		
		startProcess(0, BigInteger.valueOf(0));
	}
	
	public static void main(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("files").hasArgs().withDescription("List of files with programs for pMwIR").create("p"));
		options.addOption(OptionBuilder.withDescription("Output descriptions of command line options").create("h"));
		options.addOption(OptionBuilder.withDescription("Indicates an invitation when the input and output").create("i"));
		options.addOption(OptionBuilder.withDescription("Show debug output").create("d"));
		options.addOption(OptionBuilder.withArgName("time").hasArg().withDescription("Changes the delay between commands (in ms, default: 100)").create("r"));
		
		CommandLineParser parser = new PosixParser();
		CommandLine line = parser.parse(options, args);
		
		if(line.hasOption("h")) {
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("pMWiR", options);
			System.out.flush();
		}
		
		if(line.hasOption("p")) {
			String[] files = line.getOptionValues("p");
			PMwIR pMwIR = new PMwIR();
			int delay = 100;
			if(line.hasOption("r")) 
				try {
					delay = Math.max(10, Integer.valueOf(line.getOptionValue("r").trim()));
				} catch (NumberFormatException e) {
					System.out.println("Incorrectly delay time");
					return;
				}
			pMwIR.init(files, delay, line.hasOption("i"), line.hasOption("d"));
		}
		
	}

}