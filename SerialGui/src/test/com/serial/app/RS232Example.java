package com.serial.app;

import gnu.io.CommPortIdentifier;  
import gnu.io.SerialPort;  

import com.serial.api.*;
public class RS232Example {  
	private SerialPort serialPort;
	private CommPortIdentifier portIdentifier;
	
    public void connect(String portName) throws Exception {  
       portIdentifier = CommPortIdentifier.getPortIdentifier(portName);  
   
        if (portIdentifier.isCurrentlyOwned()) {  
            System.out.println("Port in use!");  
        } else {  
            // points who owns the port and connection timeout  
            serialPort = (SerialPort) portIdentifier.open("RS232Example", 2000);  
              
            // setup connection parameters  
            serialPort.setSerialPortParams(  
                115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);  
   
            
            // setup serial port writer  
            CommPortSender.setWriterStream(serialPort.getOutputStream());  
              
            // setup serial port reader  
            new CommPortReceiver(serialPort.getInputStream()).start();  
        }  
        System.out.println(serialPort.getName());
        System.out.println(portIdentifier.getName()); 
    }  
      
    public static void main(String[] args) throws Exception {  
          
        // connects to the port which name (e.g. COM1) is in the first argument  
        //new RS232Example().connect(args[0]);  
    	new RS232Example().connect("COM16");
        // send HELO message through serial port using protocol implementation  
        CommPortSender.send(new ProtocolImpl().getMessage("\r"));  
        
 
       // CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM16");  
        
      /*  if (portIdentifier.isCurrentlyOwned()) {  
            System.out.println("Port in use!");  
            System.out.println(portIdentifier.getName()); 
        } */
    }  
} 

