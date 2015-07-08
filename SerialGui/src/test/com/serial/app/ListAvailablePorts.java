package com.serial.app;

import gnu.io.CommPortIdentifier;  
   

import gnu.io.NoSuchPortException;

import java.util.Enumeration;  
   
public class ListAvailablePorts {  
   
	private Enumeration ports;
	
    public void list() {  
        ports = CommPortIdentifier.getPortIdentifiers();  
          
        while(ports.hasMoreElements()){
        	String name=((CommPortIdentifier)ports.nextElement()).getName();
            System.out.println(name);  
            try {
				System.out.println(CommPortIdentifier.getPortIdentifier(name).isCurrentlyOwned());
			} catch (NoSuchPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	
        }  
    }  
   
    
    
    public static void main(String[] args) {  
        new ListAvailablePorts().list();  
    }  
} 
