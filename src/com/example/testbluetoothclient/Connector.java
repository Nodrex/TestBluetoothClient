package com.example.testbluetoothclient;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

//TODO should be changed to implement runnable interface and not extend thread class.
public class Connector extends Thread {

	private BluetoothAdapter bluetoothAdapter;
	private BluetoothSocket socket;

	public Connector(BluetoothAdapter bluetoothAdapter, BluetoothDevice device) throws Exception {
		this.bluetoothAdapter = bluetoothAdapter;
		// Get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.UUID));
		} catch (IOException e) {}

	}

	public void run() {
		// Cancel discovery because it will slow down the connection
		bluetoothAdapter.cancelDiscovery();
		try {
			// Connect the device through the socket. This will block until it succeeds or throws an exception
			socket.connect();
		} catch (IOException connectException) {
			// Unable to connect; close the socket and get out
			connectException.printStackTrace();
			try {
				socket.close();
			} catch (IOException closeException) {
				closeException.printStackTrace();
				}
			return;
		}
		System.out.println(socket);
		try {
			socket.getOutputStream().write("Vision+".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
