package com.serial.app;

import gnu.io.CommPortIdentifier;  
   
import java.util.Enumeration;  
   
public class ListAvailablePorts {  
   
	private Enumeration ports;
	
    public void list() {  
        ports = CommPortIdentifier.getPortIdentifiers();  
          
        while(ports.hasMoreElements())  
            System.out.println(((CommPortIdentifier)ports.nextElement()).getName());  
    }  
   
    
    
    public static void main(String[] args) {  
        new ListAvailablePorts().list();  
    }  
} 
