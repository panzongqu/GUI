package com.serial.app;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

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
import com.sun.org.apache.bcel.internal.generic.RETURN;

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
	
   public String serialConnect() {  
	   
       SerialConfigureData currentConfigureData=dialog.getCurrentConfigureData();
	   if(dialog.isNewConfigureData()){
		   dialog.clearNewConfigureDataFlag();
	
		   CommPortIdentifier portIdentifier;
			try {
				portIdentifier = CommPortIdentifier.getPortIdentifier(currentConfigureData.getPortName());
			} catch (NoSuchPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("NoSuchPort");
				return "NoSuchPort";
			}
		   if(portIdentifier.isCurrentlyOwned()){
			   try {
				serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
				       		currentConfigureData.getStopBits(), currentConfigureData.getParity());
				} catch (UnsupportedCommOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	           serialPort.setDTR(currentConfigureData.getdtrDsr());
	           serialPort.setRTS(currentConfigureData.getRtsCts());	 
		   }else{				   
			   try {
				serialPort = (SerialPort) portIdentifier.open("MySerial", 2000);
			} catch (PortInUseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("PortInUse");
				return "PortInUse";
			}
			   try {
				serialPort.setSerialPortParams(currentConfigureData.getBaudRate(), currentConfigureData.getDataBits(), 
				       		currentConfigureData.getStopBits(), currentConfigureData.getParity());
			} catch (UnsupportedCommOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	           serialPort.setDTR(currentConfigureData.getdtrDsr());
	           serialPort.setRTS(currentConfigureData.getRtsCts());	 
	                      
	           try {
				CommPortSender.setWriterStream(serialPort.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		            	            
	           try {
				commPortReceiver = new CommPortReceiver(serialPort.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	           commPortReceiver.start();				   							   
		   }		   
	   }   
	   return null;
   }  	


}
