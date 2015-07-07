package com.serial.app;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import com.serial.api.CommPortReceiver;
import com.serial.api.CommPortSender;
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
	private ConfigureSerialGui dialog;
	private SerialPort serialPort;
	private CommPortIdentifier portIdentifier;
	
	public ConfigureSerial(){
		dialog = new ConfigureSerialGui();
	}
	 
   public void serialConnect() throws Exception {  
	   
       SerialConfigureData currentConfigureData=dialog.getCurrentConfigureData();
	   if(dialog.isNewConfigureData()){
		   dialog.clearNewConfigureDataFlag();
	
		   if(currentConfigureData.getPortName().equals(portIdentifier.getName())){
	           serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
	              		currentConfigureData.getStopBits(), currentConfigureData.getParity()); 
	              serialPort.setDTR(currentConfigureData.getdtrDsr());
	              serialPort.setRTS(currentConfigureData.getRtsCts());
		   }else{
			   
			   
		   }
		   
		   
		   
		   portIdentifier = CommPortIdentifier.getPortIdentifier(currentConfigureData.getPortName());
		   
		   if(portIdentifier.isCurrentlyOwned()){
			   if(currentConfigureData.getPortName().equals(currentPortName)){   
		           serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
		              		currentConfigureData.getStopBits(), currentConfigureData.getParity()); 
		              serialPort.setDTR(currentConfigureData.getdtrDsr());
		              serialPort.setRTS(currentConfigureData.getRtsCts());
			   }else{
				   System.out.println("Port in use!");  
			   }			   
		   }else{
	           serialPort = (SerialPort) portIdentifier.open("MySerial", 2000);   
	           serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
	           		currentConfigureData.getStopBits(), currentConfigureData.getParity()); 
	           serialPort.setDTR(currentConfigureData.getdtrDsr());
	           serialPort.setRTS(currentConfigureData.getRtsCts());	 
	           
	           currentPortName = currentConfigureData.getPortName();
	           
	            CommPortSender.setWriterStream(serialPort.getOutputStream());  
	            new CommPortReceiver(serialPort.getInputStream()).start();  
		   }	   
	   }else{
		   //default com
	   }
	   
     /*  if (portIdentifier.isCurrentlyOwned()) {  
           System.out.println("Port in use!");  
       } else {  
    	   
           serialPort = (SerialPort) portIdentifier.open("MySerial", 2000);   
           serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
           		currentConfigureData.getStopBits(), currentConfigureData.getParity()); 
           serialPort.setDTR(currentConfigureData.getdtrDsr());
           serialPort.setRTS(currentConfigureData.getRtsCts());
       }  */
   }  	
}
