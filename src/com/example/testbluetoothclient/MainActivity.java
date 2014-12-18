package com.example.testbluetoothclient;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver receiver;
	private BluetoothDevice device;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Detect if device have bt
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Util.toast(this, "bluetoothAdapter is not supported");
		}else scanNewDevices();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiver == null) return;
		unregisterReceiver(receiver);
	}
	
	/**
	 * Search for new devices.
	 */
	private void scanNewDevices() {
		final List<String> l = new ArrayList<>();
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					l.add(device.getName() + "\n" + device.getAddress());
				}
				if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
					try {
						new Connector(bluetoothAdapter,device).start();
					} catch (Exception e) {
						l.clear();
						l.add("Could not connect to device:");
						l.add(device.getName());
						l.add(e.toString());
						Util.messagBox(MainActivity.this, l);
					}
				}
				if(!l.isEmpty()){
					//Util.messagBox(MainActivity.this, l);
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy
		bluetoothAdapter.startDiscovery();//starts bt device discovering
	}


}
