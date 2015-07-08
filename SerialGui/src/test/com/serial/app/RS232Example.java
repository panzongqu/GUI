package com.serial.app;

import java.io.IOException;
import java.util.concurrent.Delayed;

import gnu.io.CommPortIdentifier;  
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;  
import gnu.io.UnsupportedCommOperationException;

import com.serial.api.*;
public class RS232Example {  
     
	SerialPort serialPort;
	CommPortIdentifier portIdentifier;
	CommPortReceiver commPortReceiver;
    public void connect(String portName) {  
        try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("NoSuchPort");
		}  
   
        if (portIdentifier.isCurrentlyOwned()) {  
            System.out.println("Port in use!");  
        } else {  
            // points who owns the port and connection timeout  
            try {
				serialPort = (SerialPort) portIdentifier.open("RS232Example", 2000);
			} catch (PortInUseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("PortInUse");
				return;
			}  
              
            // setup connection parameters  
            try {
				serialPort.setSerialPortParams(  
				    115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
   
            // setup serial port writer  
            try {
				CommPortSender.setWriterStream(serialPort.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
              
            // setup serial port reader  
            try {
				commPortReceiver = new CommPortReceiver(serialPort.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            commPortReceiver.start();
        }  
        System.out.println(portIdentifier.getName());  
    }  
      
    @SuppressWarnings("deprecation")
	public synchronized void disconnect() throws IOException{
    	commPortReceiver.exit();
    	serialPort.getOutputStream().close();
    	serialPort.getInputStream().close();
    	serialPort.removeEventListener();
    	serialPort.close();
    }
    
    public static void main(String[] args) throws Exception {  
          
        // connects to the port which name (e.g. COM1) is in the first argument  
        //new RS232Example().connect(args[0]);  
    	RS232Example RS232=new RS232Example();
    	RS232.connect("COM16");
        // send HELO message through serial port using protocol implementation  
        //CommPortSender.send(new ProtocolImpl().getMessage("\r"));  
        
        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        RS232.disconnect();
        System.out.println("2S");
    	new RS232Example().connect("COM1");
        //CommPortSender.send(new ProtocolImpl().getMessage("\r"));  
        //CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM1");  
        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        } 
        System.out.println("2S");  
        RS232.disconnect();
        
    	new RS232Example().connect("COM16");
        // send HELO message through serial port using protocol implementation  
        //CommPortSender.send(new ProtocolImpl().getMessage("\r"));         
        //if (portIdentifier.isCurrentlyOwned()) {  
       //     System.out.println("Port in use!");  
       // } 
    }  
} 

