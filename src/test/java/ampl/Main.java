package ampl;

import java.awt.*;
import java.io.*;

// class Main ////////////////////////////////////////////////////////

public class Main extends Frame {

	static String amplcmd = "ampl"; // executable

	MenuBar mb = new MenuBar();
	Menu file, edit, view, send, help;

	TextArea trans = new TextArea(30, 60); // transcript
	TextField acmd = new TextField(); // command line
	TextArea model = new TextArea(15, 60);
	TextArea data = new TextArea(15, 60);
	TextArea last_text = null; // who had the focus last
	Label prompt = new Label("ampl:");
	String last_sel = ""; // last selected text

	TextField find = new TextField(20);
	int last_pat = 0; // last pattern location for find

	String lastdir = "."; // directory for file opens

	Panel pl, pr, pb;
	CheckboxMenuItem cbtool;
	GridBagLayout gb;
	GridBagConstraints gbc = new GridBagConstraints();

	Ampl amplConsole = null;
	static int nsend = 0;

	static public void main(String arg[]) {
		if (arg.length > 0)
			amplcmd = arg[0];
		Main m = new Main(amplcmd);
	}

	Main(String amplcmd) {
		super("AMPL/Java 0.04 19990819");
		this.amplcmd = amplcmd;
		startGui();
		amplConsole = startAmpl(amplcmd);
	}

	public Ampl startAmpl(String cmd) {
		if (amplConsole != null)
			amplConsole.close();
		amplConsole = null;
		acmd.setBackground(Color.white);
		acmd.setText("");
		try {
			amplConsole = new Ampl(cmd);
		} catch (Exception e) {
			acmd.setBackground(Color.yellow);
			acmd.setText(e + " starting " + cmd);
			System.err.println(e + " starting " + cmd);
		}
		if (amplConsole != null) { // test for vital signs
			amplConsole
					.send("option show_stats 1; option display_eps .000001; print $version;\n");
			trans.appendText(amplConsole.rcv());
		}
		return amplConsole;
	}

	public void startGui() {
		// menubar...
		setMenuBar(mb);
		file = new Menu("File");
		file.add(new MenuItem("New"));
		file.add(new MenuItem("Open model..."));
		file.add(new MenuItem("Open data..."));
		file.add(new MenuItem("-"));
		file.add(new MenuItem("Save..."));
		file.add(new MenuItem("-"));
		file.add(new MenuItem("Exit"));
		mb.add(file);

		edit = new Menu("Edit");
		edit.add(new MenuItem("Cut"));
		edit.add(new MenuItem("Copy"));
		edit.add(new MenuItem("Paste"));
		edit.add(new MenuItem("Select All"));
		mb.add(edit);

		view = new Menu("View");
		cbtool = new CheckboxMenuItem("Toolbar");
		cbtool.setState(true);
		view.add(cbtool);
		mb.add(view);

		send = new Menu("Send");
		send.add(new MenuItem("Selection"));
		send.add(new MenuItem("-"));
		send.add(new MenuItem("Model"));
		send.add(new MenuItem("Data"));
		send.add(new MenuItem("Solve"));
		send.add(new MenuItem("-"));
		send.add(new MenuItem("Reset"));
		send.add(new MenuItem("Reset Data"));
		mb.add(send);

		help = new Menu("Help");
		help.add(new MenuItem("README file"));
		help.add(new MenuItem("-"));
		help.add(new MenuItem("About AMPL/Java"));
		mb.add(help);

		// initfont("Courier", 12);

		// left side: prompt, command input acmd and output trans:

		Panel p0 = new Panel();
		GridBagLayout g0 = new GridBagLayout();
		GridBagConstraints g0c = new GridBagConstraints();
		p0.setLayout(g0);
		g0c.fill = GridBagConstraints.HORIZONTAL;
		g0c.anchor = GridBagConstraints.WEST;
		g0c.weightx = 0;
		g0c.weighty = 0;
		g0c.gridx = 0;
		g0c.gridy = 0;
		g0.setConstraints(prompt, g0c);
		p0.add(prompt);

		g0c.weightx = 1;
		g0c.weighty = 0;
		g0c.gridx = 1;
		g0c.gridy = 0;
		g0.setConstraints(acmd, g0c);
		p0.add(acmd);

		pl = new Panel();
		gb = new GridBagLayout();
		pl.setLayout(gb);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gb.setConstraints(p0, gbc);
		pl.add(p0);

		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gb.setConstraints(trans, gbc);
		pl.add(trans);

		// right side: model and data:
		pr = new Panel();
		pr.setLayout(gb);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = .5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gb.setConstraints(model, gbc);
		pr.add(model);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gb.setConstraints(data, gbc);
		pr.add(data);

		// top: toolbar:
		pb = new Panel();
		pb.setLayout(new FlowLayout(FlowLayout.LEFT));
		pb.add(new Button("New"));
		pb.add(new Button("Model"));
		pb.add(new Button("Data"));
		pb.add(new Button("Solve"));
		pb.add(new Button("Reset"));
		pb.add(new Button("Display"));
		pb.add(new Button("Clear"));
		pb.add(find); // "Find"

		// overall:
		setLayout(gb);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // spans horiz
		gb.setConstraints(pb, gbc);
		add(pb);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = .5;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1; // revert
		gb.setConstraints(pl, gbc);
		add(pl);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = .5;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1; // revert
		gb.setConstraints(pr, gbc);
		add(pr);

		pack();
		show();
		model.setText("# Type model here or open a model file...\n");
		data.setText("# Type data here or open a data file...\n");
		acmd.requestFocus();
	}

	public void sendcmd(String s) {
		trans.appendText("== " + ++nsend + " == " + s + "\n");
		amplConsole.send(s + "\n");
		acmd.selectAll();
		trans.appendText(amplConsole.rcv());
		prompt.setText(amplConsole.getPrompt());
	}

	public boolean mouseDown(Event e, int x, int y) {
		// does not work for textareas; just unused parts of frames and panels
		// System.err.println("mouse down " + e.target);
		if (e.target instanceof TextArea) {
			last_text = (TextArea) e.target;
			return true;
		}
		return false;
	}

	public boolean gotFocus(Event e, Object what) {
		// so we know where to apply Edit menu items
		if (e.target instanceof TextArea) {
			last_text = (TextArea) e.target;
			return true;
		}
		return false;
	}

	public boolean action(Event e, Object what) {
		if (e.target == acmd) { // Return in command textfield
			sendcmd(acmd.getText());
			return true;
		} else if (e.target == find) {
			find_cmd();
			return true;
		} else if (e.target == cbtool) {
			if (cbtool.getState() == false) // seems backwards
				pb.hide();
			else
				pb.show();
			pack();
			return true;
		} else if (e.target instanceof Button) {
			String b = (String) what;
			if (b.equals("New")) {
				file_new();
			} else if (b.equals("Model")) {
				readmod(model);
			} else if (b.equals("Data")) {
				readmod(data);
			} else if (b.equals("Solve")) {
				solve_button();
			} else if (b.equals("Reset")) {
				sendcmd("reset;");
			} else if (b.equals("Display")) {
				display_button();
			} else if (b.equals("Clear")) {
				last_text.setText("");
			}
			return true;
		} else if (e.target instanceof MenuItem) {
			String arg = (String) e.arg;
			// File:
			if (arg.equals("New")) {
				file_new();
			} else if (arg.equals("Open model...")) {
				readmod(model);
			} else if (arg.equals("Open data...")) {
				readmod(data);
			} else if (arg.equals("Save...")) {
				savemod(last_text);
			} else if (arg.equals("Exit")) {
				if (amplConsole != null)
					amplConsole.send("exit;"); // don't ask for reply
				hide();
				dispose();
				System.exit(0); // needed standalone

				// Edit:
			} else if (arg.equals("Cut")) {
				last_sel = last_text.getSelectedText();
				int n1 = last_text.getSelectionStart();
				int n2 = last_text.getSelectionEnd();
				last_text.replaceText("", n1, n2);
			} else if (arg.equals("Copy")) {
				last_sel = last_text.getSelectedText();
			} else if (arg.equals("Paste")) {
				int n1 = last_text.getSelectionStart();
				int n2 = last_text.getSelectionEnd();
				last_sel = "" + last_sel + "";
				last_text.replaceText(last_sel, n1, n2);
			} else if (arg.equals("Select All")) {
				last_text.selectAll();

				// View:

				// Send:
			} else if (arg.equals("Selection")) {
				sendcmd(trans.getSelectedText());
			} else if (arg.equals("Model")) {
				sendcmd("reset; model;\n" + model.getText());
			} else if (arg.equals("Data")) {
				sendcmd("reset data; data;\n" + data.getText());
			} else if (arg.equals("Solve")) {
				solve_button();
			} else if (arg.equals("Reset")) {
				sendcmd("reset;");
			} else if (arg.equals("Reset Data")) {
				sendcmd("reset data;");

				// Options:
			} else if (arg.equals("Network connection")) {

				// Help:
			} else if (arg.equals("README file")) {
				Notepad d = new Notepad(this, "readme.txt");
			} else if (arg.equals("About AMPL/Java")) {
				Notepad d = new Notepad(this, "about.txt");
			} // last one
			return true;
		}
		return false;
	}

	public void file_new() {
		// should save and close previous files first
		trans.setText("");
		model.setText("# Type model here or open a model file...\n");
		data.setText("# Type data here or open a data file...\n");
		nsend = 0;
	}

	public void solve_button() {
		sendcmd("reset;\n" + model.getText() + "\n" + "data;\n"
				+ data.getText() + "solve;\n");
	}

	public void initfont(String ft, int size) {
		Font f = new Font(ft, Font.PLAIN, size);
		trans.setFont(f);
		acmd.setFont(f);
		model.setFont(f);
		data.setFont(f);
	}

	public void find_cmd() {
		String pat = find.getText();
		if (last_text == null)
			last_text = trans; // kludge
		String targ = last_text.getText();
		int n = 0;
		if (last_pat + pat.length() >= targ.length()) {
			last_pat = 0;
		}
		n = targ.indexOf(pat, last_pat); // look for it
		if (n == -1) {
			last_pat = 0;
			n = targ.indexOf(pat, 0); // look again
		}
		if (n >= 0) {
			last_text.select(n, n + pat.length());
			last_pat = n + 1;
			// there's a bug here: indexOf appears to count \r's while select
			// does not.
			System.err.println("n = " + n);
		}
	}

	public void display_button() {
		sendcmd("display " + last_text.getSelectedText() + ";");
	}

	public void readmod(TextArea t) {
		String which = (t == model) ? "Model" : "Data";

		FileDialog fd = new FileDialog(this, "Open " + which, FileDialog.LOAD);
		fd.setDirectory(lastdir); // back to where we were
		fd.pack();
		fd.show();

		lastdir = fd.getDirectory();
		if (fd.getFile() != null)
			t.setText(readfile(lastdir + fd.getFile()));
	}

	public String readfile(String fname) {
		int n;
		byte buf[] = new byte[1000];
		String s = new String("");
		FileInputStream in;

		try {
			in = new FileInputStream(fname);
		} catch (FileNotFoundException e) {
			System.err.println(e + " can't open " + fname);
			return e + " can't open " + fname;
		}
		try {
			while ((n = in.read(buf)) > 0) {
				String s1 = new String(buf, 0, 0, n);
				s = s + s1;
			}
		} catch (IOException e) {
			System.err.println(e + " readfile error");
			return e + " readfile error";
		}
		return s;
	}

	public void savemod(TextArea t) {
		String which;
		if (t == model)
			which = "Model";
		else if (t == data)
			which = "Data";
		else {
			t = trans;
			which = "Command";
		}

		FileDialog fd = new FileDialog(this, "Save " + which, FileDialog.SAVE);
		fd.setDirectory(lastdir);
		fd.pack();
		fd.show();
		if (fd.getFile() != null)
			writefile(t.getText(), fd.getDirectory() + fd.getFile());
	}

	public void writefile(String s, String fname) {
		FileOutputStream out;

		try {
			out = new FileOutputStream(fname);
			byte buf[] = new byte[s.length()];
			s.getBytes(0, s.length(), buf, 0);
			out.write(buf);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println(e + " can't open " + fname);
		} catch (IOException e) {
			System.err.println(e + " writefile error");
		}
	}

} // end of class Main

// class Notepad //////////////////////////////////////////////////////

class Notepad extends Dialog {
	Main gui;

	public Notepad(Frame parent, String fname) {
		super(parent, "Viewing " + fname, false); // non-modal
		gui = (Main) parent;

		setLayout(new BorderLayout());
		add("Center", new TextArea(gui.readfile(fname)));
		Button ok = new Button("    OK    ");
		add("South", ok);

		move(30, 30);
		pack();
		show();
	}

	public boolean action(Event e, Object arg) {
		if (arg.equals("    OK    ")) {
			hide();
			dispose();
			return true;
		}
		return false;
	}

} // end of class Notepad
