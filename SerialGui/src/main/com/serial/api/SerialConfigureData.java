package com.serial.api;

public class SerialConfigureData {
   	private String portName;
	private int baudRate;
	private int dataBits;
	private int stopBits;
	private int parity;
	private boolean dtrDsr;
	private boolean rtsCts;
	private boolean xonXoff;
	
	private int breakLength;
	
	public SerialConfigureData(){
		portName=null;
	}
	public SerialConfigureData(String com, int x, int y, int z, boolean a){
		portName=com;
		baudRate=x;
		//..
	}
	
	public void setPortName(String com){
		portName=com;
	}
	public void setBaudRate(int value){
		baudRate=value;
	}
	public void setDataBits(int value){
		dataBits=value;
	}
	public void setStopBits(int value){
		stopBits=value;
	}
	public void setParity(int value){
		parity=value;
	}
	public void setDtrDsr(boolean state){
		dtrDsr=state;
	}
	public void setRtsCts(boolean state){
		rtsCts=state;
	}
	public void setXonXoff(boolean state){
		xonXoff=state;
	}
	public void setBreakLength(int value){
		breakLength=value;
	}
	
	public String getPortName(){
		return portName;
	}
	public int getBaudRate(){
		return baudRate;
	}
	public int getDataBits(){
		return dataBits;
	}
	public int getStopBits(){
		return stopBits;
	}
	public int getParity(){
		return parity;
	}
	public boolean getdtrDsr(){
		return dtrDsr;
	}
	public boolean getRtsCts(){
		return rtsCts;
	}
	public boolean getXonXoff(){
		return xonXoff;
	}
	public int getBreakLength(){
		return breakLength;
	}
}
