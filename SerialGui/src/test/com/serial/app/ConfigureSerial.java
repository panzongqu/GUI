package com.serial.app;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.awt.Component;
import java.io.IOException;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ConfigureSerial configureSerial = new ConfigureSerial();
			while(true){
		        try {
		            Thread.sleep(2000);                 //1000 milliseconds is one second.
		        } catch(InterruptedException ex) {
		            Thread.currentThread().interrupt();
		        }
		        configureSerial.serialConnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ConfigureSerialGui dialog;
	private SerialPort serialPort;
	private CommPortIdentifier portIdentifier;
	private CommPortReceiver commPortReceiver;
	
	public ConfigureSerial(){
		dialog = new ConfigureSerialGui();
		commPortReceiver=null;
	}

	public synchronized void serialDisconnect() throws IOException{
		if(commPortReceiver.equals(null)){			
		}else{
	    	commPortReceiver.exit();			
		}
    	serialPort.getOutputStream().close();
    	serialPort.getInputStream().close();
    	serialPort.removeEventListener();
    	serialPort.close();
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
			   //new connect
			   CommPortIdentifier newPortIdentifier = CommPortIdentifier.getPortIdentifier(currentConfigureData.getPortName());
			   if(newPortIdentifier.isCurrentlyOwned()){
				   System.out.println("Port in use!"); 
			   }else{				   
				   serialPort = (SerialPort) newPortIdentifier.open("MySerial", 2000);
				   serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
			           		currentConfigureData.getStopBits(), currentConfigureData.getParity()); 
		           serialPort.setDTR(currentConfigureData.getdtrDsr());
		           serialPort.setRTS(currentConfigureData.getRtsCts());	 
		           
		           portIdentifier = newPortIdentifier;	           
		           CommPortSender.setWriterStream(serialPort.getOutputStream());		            	            
		           commPortReceiver = new CommPortReceiver(serialPort.getInputStream());  
		           commPortReceiver.start();				   							   
			   }		   
		   }   
	   }else{
		   //default COM1
	   }
   }  	
}
