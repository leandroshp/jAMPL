package ampl;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Ampl {
	
	static String last_prompt = "";
	private static String AMPL_CMD = "ampl";
	
	
	static OutputStream out;
	static PrintStream pout;
	static DataInputStream dis;
	static DataInputStream dos;
	
	public Ampl() throws IOException{
		this(AMPL_CMD);
	}

	public Ampl(String cmd) // local process version.
			throws IOException {
		
		String[] amplcmd = new String[2];
		amplcmd[0] = cmd;
		amplcmd[1] = "-b"; // turns on block mode, off cr

		Runtime rt = Runtime.getRuntime();
		// System.err.println("starting " + amplcmd[0]);
		Process p = rt.exec(amplcmd);
		// System.err.println("started " + amplcmd[0]);
		out = p.getOutputStream();
		pout = new PrintStream(out, true);
		dis = new DataInputStream(System.in);
		dos = new DataInputStream(p.getInputStream());
		// System.err.println("files opened");

		rcv(); // absorb initial prompt from ampl
	}

	public String getPrompt() {
		return last_prompt;
	}

	public void send(String s) {
		// System.err.println("send " + s.length() + " [" + s + "]");
		pout.print(s.length() + " " + s);
	}

	public String rcv() {
		byte buf[] = new byte[20000];
		int n, i;
		byte b;
		String s = new String("");

		try {
			while (true) {
				n = 0;
				for (i = 0; (b = dos.readByte()) >= '0' && b <= '9'; i++)
					n = 10 * n + b - '0';
				// System.err.println("n = " + n + ", b = [" + b + "]");
				// read n more characters; delete carriage returns.
				for (i = 0; i < n;) {
					b = dos.readByte();
					// System.err.println("b = " + b);
					if (b != '\r')
						buf[i++] = b;
				}
				String s1 = new String(buf, 0, 0, i);
				// System.err.println("rcv " + s1.length() + " [" + s1 + "]");
				if (s1.regionMatches(0, "prompt", 0, 6)) {
					last_prompt = s1.substring(s1.indexOf('\n') + 1);
					break;
				}
				s = s + s1;
			}
		} catch (IOException e) {
			System.err.println(e + "rcv error");
			return e + "rcv error";
		}
		return s.substring(s.indexOf('\n') + 1);
	}

	public void close() {
		try {
			if (out != null)
				out.close();
			if (pout != null)
				pout.close();
			if (dis != null)
				dis.close();
			if (dos != null)
				dos.close();
			out = null;
			pout = null;
			dis = null;
			dos = null;
		} catch (IOException e) {
			;
		}
	}

	public static boolean isInvalidModel(String modelSolution) {
		return modelSolution.contains("file -") || modelSolution.contains("error_error");
	}

	public static boolean isInfeasibleModel(String modelSolution) {
		return modelSolution.contains("infeasible problem");
	}

}
