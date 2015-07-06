package com.serial.app;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import com.serial.api.SerialConfigureData;

public class ConfigureSerial {
/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.refreshSerialInfo();
			//dialog.testConfigurationData();
			dialog.getSelectedBaudRate();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	private ConfigureSerialGui dialog = new ConfigureSerialGui();
	private SerialConfigureData currentConfigureData = new SerialConfigureData();   
	
	 
	@SuppressWarnings("unchecked")
	public void refreshSerialInfo(){
		Vector<String> portComboBoxItems= new Vector<String>();
		Component[] components = dialog.getComBoBox().getComponents();	
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
		Component[] components = dialog.getComBoBox().getComponents();
		System.out.println(components.length);
		for(int i=0; i<components.length; i++){
			System.out.println(components[i].getName());
			@SuppressWarnings("rawtypes")
			JComboBox comboBox = (JComboBox) components[i];
			System.out.println(comboBox.getSelectedItem());		
		}
	}

	
	public SerialConfigureData getCurrentConfigureData(){
		currentConfigureData.setPortName(getSelectedPortName());
		currentConfigureData.setBaudRate(getSelectedBaudRate());
		currentConfigureData.setDataBits(getSelectedDataBits());
		currentConfigureData.setStopBits(getSelectedStopBits());		
		currentConfigureData.setParity(getSelectedParity());
		currentConfigureData.setDtrDsr(getSelectedCheckBox("DTR/DSR"));
		currentConfigureData.setRtsCts(getSelectedCheckBox("RTS/CTS"));
		currentConfigureData.setXonXoff(getSelectedCheckBox("XON/XOFF"));	
		return currentConfigureData;	
	}

	
	private boolean getSelectedCheckBox(String name){
		Component[] components = dialog.getCheckBoBox().getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals(name)){
				JCheckBox checkBox = (JCheckBox) components[i];		
				return checkBox.isSelected();	
			
			}
		}
		return false;			
	}	
	
	private String getSelectedPortName(){
		Component[] components = dialog.getComBoBox().getComponents();
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
		Component[] components = dialog.getComBoBox().getComponents();
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
		Component[] components = dialog.getComBoBox().getComponents();
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
		Component[] components = dialog.getComBoBox().getComponents();
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
		Component[] components = dialog.getComBoBox().getComponents();
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
		
}
