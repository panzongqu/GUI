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
		Component[] components = cbbpanel.getComponents();	
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers(); 
       while(ports.hasMoreElements()) {
       	portComboBoxItems.add(((CommPortIdentifier)ports.nextElement()).getName());	        		        	
       } 		
		
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("po_comboBox")){
				@SuppressWarnings("rawtypes")
				JComboBox portComboBox = (JComboBox) components[i];	
				portComboBox.setModel(new DefaultComboBoxModel<String>(portComboBoxItems));
				break;
			}
		}
	}
	
	public void testConfigurationData()
	{
		Component[] components = cbbpanel.getComponents();
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
		Component[] components = chckpanel.getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals(name)){
				@SuppressWarnings("rawtypes")
				JCheckBox checkBox = (JCheckBox) components[i];		
				return checkBox.isSelected();	
			
			}
		}
		return false;			
	}	
	
	private String getSelectedPortName(){
		Component[] components = cbbpanel.getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("po_comboBox")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];
				return comboBox.getSelectedItem().toString();		
			}
		}
		return null;
	}
	
	private int getSelectedBaudRate(){
		Component[] components = cbbpanel.getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("ba_comboBox")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];		
				return Integer.parseInt((String)comboBox.getSelectedItem());	
			}
		}
		return 0;
	}

	private int getSelectedDataBits(){
		Component[] components = cbbpanel.getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("da_comboBox")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];		
				return Integer.parseInt((String)comboBox.getSelectedItem());	
			}
		}
		return 0;
	}

	private int getSelectedStopBits(){
		Component[] components = cbbpanel.getComponents();
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("st_comboBox")){
				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) components[i];		
				return Integer.parseInt((String)comboBox.getSelectedItem());	
			}
		}
		return 0;
	}
	
	private int getSelectedParity(){
		Component[] components = cbbpanel.getComponents();
		System.out.println(components.length);
		for(int i=0; i<components.length; i++){
			if(components[i].getName().toString().equals("pa_comboBox")){
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
