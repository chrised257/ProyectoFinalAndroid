/*
 * 	throughCode - Instruction transmitter for use with compatible robot using mobile devices
 *	
 *	Copyright (C) 2014 - ITESM
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Authors:
 *
 *  	ITESM representatives
 *			Ing. Martha Sordia Salinas <msordia@itesm.mx>
 *
 *		ITESM students
 *			Christian Eduardo Rodriguez Palacios <christian.2500@hotmail.com>
 *			Jesus Ramirez Nava <jesus.isdr@gmail.com>
 *			Eugenio Sanchez Garza <sanchezgarzae@gmail.com>
 */

package mx.itesm.throughcode;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ConexionActivity extends Activity {

	BluetoothAdapter mBluetoothAdapter;
	ArrayAdapter<String> myAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conexion);
		
		final Button conectar = (Button) findViewById(R.id.conectarButton);
		final ListView devicesListView = (ListView)findViewById(R.id.listView1);
		
		devicesListView.setAdapter(myAdapter);
		
	/*///////////////////////////////////
	 * Validating that BLUETOOTH exists//
	 *///////////////////////////////////
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
		}
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent,1);
		}
		
	/*/////////////////////////////////////////////////////
	 * BLUETOOTH Paired Devices association with ListView//	
	 */////////////////////////////////////////////////////
	
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        myAdapter.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		/*//////////////////////////////////////////////////////
		 * BLUETOOTH Discovery action and ListView association//	
		 *//////////////////////////////////////////////////////
		mBluetoothAdapter.startDiscovery();
		
		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            myAdapter.add(device.getName() + "\n" + device.getAddress());
		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		
		
		OnClickListener registro = new OnClickListener(){
			
			public void onClick(View v){
				
				
					
				Intent intent = new Intent (ConexionActivity.this, Interfaz.class);
				startActivity(intent);
			}
			
		};
		conectar.setOnClickListener(registro);
	}
	
	/*//////////////////////////////////////////
	 * CONNECT BLUETOOTH AS SERVER//////////////
	 *//////////////////////////////////////////
	
	/*private class AcceptThread extends Thread {
	    private final BluetoothServerSocket mmServerSocket;
	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                manageConnectedSocket(socket);
	                mmServerSocket.close();
	                break;
	            }
	        }
	    }*/
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	   /* public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}*/
	
	/*//////////////////////////
	 * BLUETOOTH AS CLIENT//////
	 *//////////////////////////
	/*private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        manageConnectedSocket(mmSocket);
	    }*/
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    /*public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}*/
}
