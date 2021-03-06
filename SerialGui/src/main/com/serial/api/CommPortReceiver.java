package com.serial.api;

import java.io.IOException;  
import java.io.InputStream;  
  
public class CommPortReceiver extends Thread {  
   
    private InputStream in;  
    private Protocol protocol = new ProtocolImpl(); 
    private volatile boolean running;
  
    public CommPortReceiver(InputStream in) {  
        this.in = in;  
        this.running=true;
    }  
      
    public void exit(){
    	this.running=false;
    	while(this.isAlive()){
    		
    	}
    }
    
    public void run() {  
        try {  
            int b;  
            while(running) {  
                  
                // if stream is not bound in.read() method returns -1  
                while((b = in.read()) != -1) {  
                    protocol.onReceive((byte) b);  
                }  
                protocol.onStreamClosed();  
                  
                // wait 10ms when stream is broken and check again  
                sleep(10);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }   
    }  
} 
