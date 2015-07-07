package com.serial.app;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Window.Type;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSeparator;

import java.awt.Panel;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;

import java.awt.GridLayout;

import javax.swing.JTextArea;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JEditorPane;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

import java.awt.Component;

import javax.swing.Box;

import java.awt.Dimension;

import javax.swing.SpringLayout;

import java.awt.Font;

import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.serial.api.CommPortReceiver;
import com.serial.api.CommPortSender;
import com.serial.api.SerialConfigureData;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

public class ConfigureSerialGui extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel chckpanel;
	private JPanel lblpanel;
	private JPanel cbbpanel;
	private JPanel buttonPane;
	private JPanel panel;
	
	private SerialConfigureData currentConfigureData = new SerialConfigureData(); 
	/**
	 * Launch the application.
	*/
	public static void main(String[] args) {
		try {
			ConfigureSerialGui dialog = new ConfigureSerialGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.refreshSerialInfo();
			//dialog.testConfigurationData();
			//dialog.getSelectedBaudRate();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public JPanel getComBoBox() {
		return cbbpanel;
	}
	
	public JPanel getCheckBoBox(){
		return chckpanel;
	}

	public SerialConfigureData getCurrentConfigureData(){
		return currentConfigureData;	
	}

	private void updateCurrentConfigureData(){
		currentConfigureData.setPortName(getSelectedPortName());
		currentConfigureData.setBaudRate(getSelectedBaudRate());
		currentConfigureData.setDataBits(getSelectedDataBits());
		currentConfigureData.setStopBits(getSelectedStopBits());		
		currentConfigureData.setParity(getSelectedParity());
		currentConfigureData.setDtrDsr(getSelectedCheckBox("DTR/DSR"));
		currentConfigureData.setRtsCts(getSelectedCheckBox("RTS/CTS"));
		currentConfigureData.setXonXoff(getSelectedCheckBox("XON/XOFF"));		
	}	
	
	@SuppressWarnings("unchecked")
	private void refreshSerialPortInfo(){
		Vector<String> portComboBoxItems= new Vector<String>();
		Component[] components = getComBoBox().getComponents();	
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers(); 
       while(ports.hasMoreElements()) {
       	portComboBoxItems.add(((CommPortIdentifier)ports.nextElement()).getName());	        		        	
       } 		
		
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("Port")){
				@SuppressWarnings("rawtypes")
				JComboBox portComboBox = (JComboBox) components[i];	
				portComboBox.setModel(new DefaultComboBoxModel<String>(portComboBoxItems));
				break;
			}
		}
	}
	
	public void testConfigurationData()
	{
		Component[] components = getComBoBox().getComponents();
		System.out.println(components.length);
		for(int i=0; i<components.length; i++){
			System.out.println(components[i].getName());
			@SuppressWarnings("rawtypes")
			JComboBox comboBox = (JComboBox) components[i];
			System.out.println(comboBox.getSelectedItem());		
		}
	}

	private boolean getSelectedCheckBox(String name){
		Component[] components = getCheckBoBox().getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals(name)){
				JCheckBox checkBox = (JCheckBox) components[i];		
				return checkBox.isSelected();		
			}
		}
		return false;			
	}	
	
	private String getSelectedPortName(){
		Component[] components = getComBoBox().getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("Port")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];
				return comboBox.getSelectedItem().toString();		
			}
		}
		return null;
	}
	
	private int getSelectedBaudRate(){
		Component[] components = getComBoBox().getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("Buad rate")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];		
				return Integer.parseInt((String)comboBox.getSelectedItem());	
			}
		}
		return 0;
	}

	private int getSelectedDataBits(){
		Component[] components = getComBoBox().getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("Data bits")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];		
				return Integer.parseInt((String)comboBox.getSelectedItem());	
			}
		}
		return 0;
	}
	
	private int getSelectedParity(){
		Component[] components = getComBoBox().getComponents();
		System.out.println(components.length);
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("Parity")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];	
				if(comboBox.getSelectedItem().toString().equals("None")){
					return 0;					
				}else if(comboBox.getSelectedItem().toString().equals("Odd")){
					return 1;					
				}else if(comboBox.getSelectedItem().toString().equals("Even")){
					return 2;					
				}else if(comboBox.getSelectedItem().toString().equals("Mark")){
					return 3;					
				}else if(comboBox.getSelectedItem().toString().equals("Space")){
					return 4;					
				}						
			}
		}
		return -1;
	}
	
	private int getSelectedStopBits(){
		Component[] components = getComBoBox().getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("Stop bits")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];		
				return Integer.parseInt((String)comboBox.getSelectedItem());	
			}
		}
		return 0;
	}
	
   public void serialConnect() throws Exception {  
       CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(currentConfigureData.getPortName());  
  
       if (portIdentifier.isCurrentlyOwned()) {  
           System.out.println("Port in use!");  
       } else {  
           SerialPort serialPort = (SerialPort) portIdentifier.open("MySerial", 2000);   
           serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
           		currentConfigureData.getStopBits(), currentConfigureData.getParity()); 
           serialPort.setDTR(currentConfigureData.getdtrDsr());
           serialPort.setRTS(currentConfigureData.getRtsCts());
       }  
   }  
	
	/**
	 * Create the dialog.
	 */
	public ConfigureSerialGui() {
		setTitle("Serial Options");
		setAlwaysOnTop(true);
		setBounds(100, 100, 325, 288);
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, contentPanel, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, contentPanel, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, contentPanel, 143, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, contentPanel, -10, SpringLayout.EAST, getContentPane());
		getContentPane().setLayout(springLayout);
		contentPanel.setToolTipText("");
		contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().add(contentPanel);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			lblpanel = new JPanel();
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblpanel, 10, SpringLayout.WEST, contentPanel);
			contentPanel.add(lblpanel);
			lblpanel.setLayout(new GridLayout(0, 1, 10, 12));
			{
				JLabel lblPort = new JLabel("Port:");
				lblpanel.add(lblPort);
			}
			{
				JLabel lblBuadRate = new JLabel("Buad rate:");
				lblpanel.add(lblBuadRate);
			}
			{
				JLabel lblDataBits = new JLabel("Data bits:");
				lblpanel.add(lblDataBits);
			}
			{
				JLabel lblParity = new JLabel("Parity:");
				lblpanel.add(lblParity);
			}
			{
				JLabel lblStopBits = new JLabel("Stop bits:");
				lblpanel.add(lblStopBits);
			}
		}
		{
			cbbpanel = new JPanel();
			sl_contentPanel.putConstraint(SpringLayout.EAST, cbbpanel, -133, SpringLayout.EAST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblpanel, 0, SpringLayout.NORTH, cbbpanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblpanel, 0, SpringLayout.SOUTH, cbbpanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, lblpanel, -8, SpringLayout.WEST, cbbpanel);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, cbbpanel, 10, SpringLayout.NORTH, contentPanel);
			contentPanel.add(cbbpanel);
			cbbpanel.setLayout(new GridLayout(0, 1, 10, 5));
			{
				JComboBox po_comboBox = new JComboBox();
				po_comboBox.setName("Port");
				po_comboBox.setEditable(true);
				po_comboBox.setPreferredSize(new Dimension(70, 21));
				po_comboBox.setMaximumRowCount(9);
				po_comboBox.setToolTipText("");
				cbbpanel.add(po_comboBox);
			}
			{
				JComboBox ba_comboBox = new JComboBox();
				ba_comboBox.setName("Buad rate");
				ba_comboBox.setModel(new DefaultComboBoxModel(new String[] {"4800", "9600", "115200"}));
				ba_comboBox.setSelectedIndex(2);
				ba_comboBox.setPreferredSize(new Dimension(70, 21));
				ba_comboBox.setMaximumRowCount(9);
				ba_comboBox.setEditable(true);
				ba_comboBox.setToolTipText("");
				cbbpanel.add(ba_comboBox);
			}
			{
				JComboBox da_comboBox = new JComboBox();
				da_comboBox.setName("Data bits");
				da_comboBox.setModel(new DefaultComboBoxModel(new String[] {"5", "6", "7", "8"}));
				da_comboBox.setSelectedIndex(3);
				da_comboBox.setPreferredSize(new Dimension(70, 21));
				da_comboBox.setMaximumRowCount(9);
				cbbpanel.add(da_comboBox);
			}
			{
				JComboBox pa_comboBox = new JComboBox();
				pa_comboBox.setName("Parity");
				pa_comboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "Odd", "Even", "Mark", "Space"}));
				//pa_comboBox.addItem(makeObj("Item 1"));
				pa_comboBox.setMaximumRowCount(9);
				pa_comboBox.setPreferredSize(new Dimension(70, 21));
				pa_comboBox.setFont(new Font("Arial", Font.PLAIN, 10));
				cbbpanel.add(pa_comboBox);
			}
			{
				JComboBox st_comboBox = new JComboBox();
				st_comboBox.setName("Stop bits");
				st_comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "1.5", "2"}));
				st_comboBox.setPreferredSize(new Dimension(70, 21));
				st_comboBox.setMaximumRowCount(9);
				cbbpanel.add(st_comboBox);
			}
		}
		{
			chckpanel = new JPanel();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, chckpanel, 20, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, chckpanel, -10, SpringLayout.EAST, contentPanel);
			chckpanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Flow control", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			contentPanel.add(chckpanel);
			chckpanel.setLayout(new GridLayout(0, 1, 15, 1));
			{
				JCheckBox chckbxDtrdsr = new JCheckBox("DTR/DSR");
				chckbxDtrdsr.setName("DTR/DSR");
				chckpanel.add(chckbxDtrdsr);
			}
			{
				JCheckBox chckbxRtscts = new JCheckBox("RTS/CTS");
				chckbxRtscts.setName("RTS/CTS");
				chckpanel.add(chckbxRtscts);
			}
			{
				JCheckBox chckbxXonxoff = new JCheckBox("XON/XOFF");
				chckbxXonxoff.setName("XON/XOFF");
				chckpanel.add(chckbxXonxoff);
			}
		}
		{
			panel = new JPanel();
			springLayout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, contentPanel);
			springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, contentPanel);
			springLayout.putConstraint(SpringLayout.NORTH, panel, 6, SpringLayout.SOUTH, contentPanel);
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			sl_contentPanel.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, getContentPane());
			sl_contentPanel.putConstraint(SpringLayout.EAST, panel, -86, SpringLayout.WEST, contentPanel);
			getContentPane().add(panel);
			FlowLayout fl_panel = (FlowLayout) panel.getLayout();
			fl_panel.setAlignment(FlowLayout.LEFT);
			{
				JLabel lbl_tip = new JLabel("Serial break length:");
				panel.add(lbl_tip);
			}
			{
				JSpinner spinner = new JSpinner();
				spinner.setModel(new SpinnerNumberModel(new Integer(100), null, null, new Integer(1)));
				spinner.setPreferredSize(new Dimension(60, 22));
				panel.add(spinner);
				sl_contentPanel.putConstraint(SpringLayout.SOUTH, spinner, -10, SpringLayout.SOUTH, contentPanel);
				sl_contentPanel.putConstraint(SpringLayout.EAST, spinner, -28, SpringLayout.EAST, contentPanel);
			}
			{
				JLabel lbl_ms = new JLabel("milliseconds");
				panel.add(lbl_ms);
			}
		}
		{
			buttonPane = new JPanel();
			springLayout.putConstraint(SpringLayout.NORTH, buttonPane, 29, SpringLayout.SOUTH, panel);
			springLayout.putConstraint(SpringLayout.WEST, buttonPane, 38, SpringLayout.WEST, getContentPane());
			springLayout.putConstraint(SpringLayout.SOUTH, buttonPane, 0, SpringLayout.SOUTH, getContentPane());
			springLayout.putConstraint(SpringLayout.EAST, buttonPane, -10, SpringLayout.EAST, getContentPane());
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton applyButton = new JButton("Apply");
				applyButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						updateCurrentConfigureData();
						System.out.println("Apply"); 
					}
				});
				applyButton.setActionCommand("OK");
				buttonPane.add(applyButton);
				getRootPane().setDefaultButton(applyButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel, -61, SpringLayout.NORTH, buttonPane);
	}
}
