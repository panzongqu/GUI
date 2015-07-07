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
	 
	
}
