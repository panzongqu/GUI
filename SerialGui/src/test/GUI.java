import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;

import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class GUI {

	public Robot bot;
	public Vector<String> ports = new Vector<String>();
	public Vector<String> XBEEs = new Vector<String>();

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				JFrame ui = new JFrame("CONTROL");
				ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ui.add(new Control(new GUI()));
			 	ui.pack();
				ui.setVisible(true);
				ui.setLocationRelativeTo(null);
				//ui.setExtendedState(JFrame.MAXIMIZED_BOTH);

			}
		});
	}

}

class Robot {

	public Port port;
	public Command command;
	public long destination;
	private boolean alive;
	private JFrame commandFrame;

	private enum Codes {

		FORWARD ('w'),
		BACKWARD ('s'),
		ANTICLOCKWISE ('a'),
		CLOCKWISE ('d'),
		STOP ('x'),
		UPWARD ('i'),
		DOWNWARD ('k'),
		LEFTWARD ('j'),
		RIGHTWARD ('l');

		public char code;

		private Codes(char value) {
			this.code = value;
		}

	}

	public Robot(String requestPort, String setDestination) {

		System.out.print("\n"+Feedback.timeStamp()+":\tconstructingRobot");

		port = new Port();
		alive = port.findPort(requestPort);
		if(!alive)
			return;
		port.openPort();

		destination = Long.parseLong(setDestination, 16);

		command = new Command(this);

		commandFrame = new JFrame("COMMAND");
		commandFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		commandFrame.add(command);
		commandFrame.pack();
		commandFrame.setVisible(true);
		commandFrame.setLocationRelativeTo(null);

		System.out.print("\n"+Feedback.timeStamp()+":\tconstructedRobot");

	}

	public void reset() {

		System.out.print("\n"+Feedback.timeStamp()+":\tdestroyingRobot");

		if(alive) {
			port = null;
			command.robot = null;
			command = null;
			commandFrame.dispose();
			alive = false;
		}

		System.out.print("\n"+Feedback.timeStamp()+":\tdestroyedRobot");

	}

	public void transmit(double r, double theta) {

		System.out.print("\n"+Feedback.timeStamp()+":\ttransmitting");
		
		Codes code;

		if(r<Command.SIZE/8)
			code = Codes.STOP;

		else if(r<Command.SIZE/4)

			if(theta<Math.PI/4 && theta>-Math.PI/4)
				code = Codes.CLOCKWISE;
			else if(theta<Math.PI*3/4 && theta>Math.PI/4)
				code = Codes.FORWARD;
			else if(theta>-Math.PI*3/4 && theta<-Math.PI/4)
				code = Codes.BACKWARD;
			else
				code = Codes.ANTICLOCKWISE;

		else if(r<Command.SIZE*3/8)

			if(theta<Math.PI/4 && theta>-Math.PI/4)
				code = Codes.RIGHTWARD;
			else if(theta<Math.PI*3/4 && theta>Math.PI/4)
				code = Codes.UPWARD;
			else if(theta>-Math.PI*3/4 && theta<-Math.PI/4)
				code = Codes.DOWNWARD;
			else
				code = Codes.LEFTWARD;

		else {
		
			System.out.print("\n"+Feedback.timeStamp()+":\t!transmitted");
			return;

		}

		port.writeData(code.code, destination);

		System.out.print("\n"+Feedback.timeStamp()+":\ttransmitted");

	}

	public boolean isAlive() {
		return alive;
	}

}

class Port {

	private SerialPort serialPort;
	private CommPortIdentifier portId;
	private OutputStream output;
	private static final int TIME_OUT = 2000, DATA_RATE = 9600;

	public Port() {
	}

	public boolean findPort(String portName) {

		System.out.print("\n"+Feedback.timeStamp()+":\tfindingPort");

		try {
			portId = CommPortIdentifier.getPortIdentifier(portName);
		}

		catch(Exception e) {
			System.err.println(e.toString());
			System.out.print("\n"+Feedback.timeStamp()+":\t!foundPort");
			return false;
		}
		
		System.out.print("\n"+Feedback.timeStamp()+":\tfoundPort");
		return true;

	}

	public void openPort()   {

		System.out.print("\n"+Feedback.timeStamp()+":\topeningPort");

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE,SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			output = serialPort.getOutputStream();
		}

		catch (Exception e) {
			System.err.println(e.toString());
			System.out.print("\n"+Feedback.timeStamp()+":\t!openedPort");
		}

		System.out.print("\n"+Feedback.timeStamp()+":\topenedPort");

	}

	public void closePort() {

		System.out.print("\n"+Feedback.timeStamp()+":\tclosingPort");

		try {
			serialPort.close();
			serialPort = null;
			output.close();
			output = null;
		}

		catch(Exception e) {
			System.err.println(e.toString());
			System.out.print("\n"+Feedback.timeStamp()+":\t!closedPort");
		}

		System.out.print("\n"+Feedback.timeStamp()+":\tclosedPort");

	}

	public void writeData(char character, long destination) {

		System.out.print("\n"+Feedback.timeStamp()+":\twritingData");

		try {
			byte array[]=new byte[19];
			array[0] = (byte)0x7E;
			array[1] = (byte)0x00;
			array[2] = (byte)0x0F;
			array[3] = (byte)0x10;
			array[4] = (byte)0x01;
			for(int i = 5; i < 13; ++i)
				array[i] = (byte)(((destination >> 8*(12 - i))) & 0xFF);
			array[13] = (byte)0xFF;
			array[14] = (byte)0xFE;
			array[15] = (byte)0x00;
			array[16] = (byte)0x00;
			array[17] = (byte)character;
			int checksum = 0;
			for(int i = 3; i < 18; ++i)
				checksum += array[i];
			array[18] = (byte)(0xFF - (checksum & 0xFF));
			output.write(array);
			output.flush();
		}

		catch (Exception e) {
			System.err.println(e.toString());
			System.out.print("\n"+Feedback.timeStamp()+":\t!writtenData");
		}

		System.out.print("\n"+Feedback.timeStamp()+":\twrittenData");

	}

}

class Command extends JPanel {

	public Robot robot;
	public static final int SIZE = 200;
	private int mouseX, mouseY;
	private boolean mouseClick;

	public Command(Robot bot) {

		robot = bot;
		mouseX = 0;
		mouseY = 0;
		mouseClick = true;

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseClick = false;
				repaint();
			}
			public void mouseReleased(MouseEvent e) {
				mouseClick = true;
				repaint();
			}
			public void mouseClicked(MouseEvent e) {
				robot.transmit(Math.hypot(e.getX()-SIZE/2, SIZE/2-e.getY()),
					Math.atan2(SIZE/2-e.getY(), e.getX()-SIZE/2));
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}
		});

	}

	public Dimension getPreferredSize() {
		return new Dimension(SIZE, SIZE);
	}

	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		RadialGradientPaint radient;
		GradientPaint gradient;

		int X = (int)(SIZE/2+SIZE/2*Math.cos(Math.atan2
			(mouseY-SIZE/2, mouseX-SIZE/2)));
		int Y = (int)(SIZE/2+SIZE/2*Math.sin(Math.atan2
			(mouseY-SIZE/2, mouseX-SIZE/2)));

		if(mouseClick)
			radient = new RadialGradientPaint (mouseX, mouseY, 100,
				new float[]{0, 1}, new Color[]{Color.WHITE, Color.BLACK});
		else
			radient = new RadialGradientPaint (mouseX, mouseY, 100,
				new float[]{0, 1}, new Color[]{Color.WHITE, Color.DARK_GRAY});
		g2d.setPaint(radient);
		g2d.fillRect(0, 0, SIZE, SIZE);

		gradient = new GradientPaint (X, Y, Color.WHITE,
			SIZE/2, SIZE/2, Color.DARK_GRAY, false);
		g2d.setPaint(gradient);
		g2d.fillOval(SIZE/8, SIZE/8, SIZE*3/4, SIZE*3/4);

		gradient = new GradientPaint (X, Y, Color.DARK_GRAY,
			SIZE/2, SIZE/2, Color.WHITE, false);
		g2d.setPaint(gradient);
		g2d.fillOval(SIZE/4, SIZE/4, SIZE/2, SIZE/2);

		gradient = new GradientPaint (X, Y, Color.WHITE,
			SIZE/2, SIZE/2, Color.DARK_GRAY, false);
		g2d.setPaint(gradient);
		g2d.fillOval(SIZE*3/8, SIZE*3/8, SIZE/4, SIZE/4);

		g2d.setPaint(Color.WHITE);
		g2d.drawOval(SIZE/8, SIZE/8, SIZE*3/4, SIZE*3/4);
		g2d.drawOval(SIZE/4, SIZE/4, SIZE/2, SIZE/2);
		g2d.drawOval(SIZE*3/8, SIZE*3/8, SIZE/4, SIZE/4);

		for(int i=-3; i<4; i+=2)
			g2d.drawLine((int)(SIZE/2+SIZE*3/8*Math.cos(Math.PI*i/4)),
				(int)(SIZE/2+SIZE*3/8*Math.sin(Math.PI*i/4)),
				(int)(SIZE/2+SIZE/8*Math.cos(Math.PI*i/4)),
				(int)(SIZE/2+SIZE/8*Math.sin(Math.PI*i/4)));

	}

}

class Control extends JPanel {

	private GUI gui;

	private JTextArea output;
	private JButton more;
	private JButton less;
	private JComboBox<String> listSource, listDestination;
	private int mouseX, mouseY;

	public Control(GUI ui) {

		gui = ui;
		mouseX = 0;
		mouseY = 0;

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}
		});

		setLayout(new GridBagLayout());
		GridBagConstraints place = new GridBagConstraints();

		output = new JTextArea(4, 25);
		output.setEditable(false);
		System.setOut(new PrintStream(new Feedback(output)));
		place.gridx = 0;
		place.gridy = 1;
		place.gridwidth = 4;
		place.fill = GridBagConstraints.HORIZONTAL;
		place.insets = new Insets(0, 10, 10, 10);
		add(new JScrollPane(output), place);

		more = new JButton("BOOT BOT");
		place.gridx = 2;
		place.gridy = 0;
		place.gridwidth = 1;
		place.insets = new Insets(10, 10, 10, 10);
		add(more, place);

		more.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(gui.bot != null) {
					gui.bot.reset();
					gui.bot = null;
				}
				Robot newRobot = new Robot(
					(String)listSource.getSelectedItem(),
					(String)listDestination.getSelectedItem());
				if(newRobot.isAlive())
					gui.bot = newRobot;
			}
		});

		less = new JButton("RESET ROBOT");
		place.gridx = 3;
		add(less, place);

		less.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(gui.bot != null) {
					gui.bot.reset();
					gui.bot = null;
				}
			}
		});

		final DefaultComboBoxModel<String> modelports = 
			new DefaultComboBoxModel<String>(gui.ports);
		listSource = new JComboBox<String>(modelports);
		listSource.setForeground(Color.BLUE);
		listSource.setFont(new Font("Arial", Font.BOLD, 14));
		listSource.setMaximumRowCount(10);
		listSource.setEditable(true);
		place.gridx = 0;
		add(listSource, place);

		listSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox cb = (JComboBox)event.getSource();
				if(cb.getSelectedIndex()<0)
					gui.ports.add((String)cb.getSelectedItem());
			}
		});

		final DefaultComboBoxModel<String> modelXBEEs = 
			new DefaultComboBoxModel<String>(gui.XBEEs);
		listDestination = new JComboBox<String>();
		listDestination.setForeground(Color.BLUE);
		listDestination.setFont(new Font("Arial", Font.BOLD, 14));
		listDestination.setMaximumRowCount(10);
		listDestination.setEditable(true);
		place.gridx = 1;
		add(listDestination, place);

		listDestination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox cb = (JComboBox)event.getSource();
				if(cb.getSelectedIndex()<0)
					gui.XBEEs.add((String)cb.getSelectedItem());
			}
		});

	}

	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		GradientPaint gradient = new GradientPaint(0, 0, Color.BLACK,
				getWidth()/2, getHeight()/2, Color.WHITE, true);
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		RadialGradientPaint radient = new RadialGradientPaint (mouseX, mouseY,
			1000, new float[]{0, 1}, new Color[]{Color.WHITE, Color.DARK_GRAY});
		g2d.setPaint(radient);
		g2d.fillRect(0, 0, getWidth(), getHeight());

	}

}

class Feedback extends OutputStream {

	private JTextArea output;

	public Feedback(JTextArea area) {
		output = area;
	}

	public void write(int bytes) {

		try {
			output.append(String.valueOf((char)bytes));
		}

		catch (Exception e) {
			System.err.println(e.toString());
		}

	}

	public static String timeStamp() {
		return new SimpleDateFormat("HH:mm:ss").format(
			Calendar.getInstance().getTime());
	}

}
