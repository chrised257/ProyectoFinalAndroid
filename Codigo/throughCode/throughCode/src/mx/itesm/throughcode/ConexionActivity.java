/*
 * throughCode - Instruction transmitter for use with compatible robot using mobile devices
 * 
 * Copyright (C) 2014 - ITESM
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authors:
 *
 * ITESM representatives
 * Ing. Martha Sordia Salinas <msordia@itesm.mx>
 *
 * ITESM students
 * Christian Eduardo Rodriguez Palacios <christian.2500@hotmail.com>
 * Jesus Ramirez Nava <jesus.isdr@gmail.com>
 * Eugenio Sanchez Garza <sanchezgarzae@gmail.com>
 */
package mx.itesm.throughcode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
<<<<<<< HEAD
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
=======
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
>>>>>>> origin/Jesús
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
<<<<<<< HEAD
=======
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
>>>>>>> origin/Jesús
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ConexionActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
	ArrayAdapter<String> myAdapter;

	private String RobotName;							   //Robot's name String
	private Set<BluetoothDevice> pairedDevices;			   //Aux. to save paired devices located at the phone 
	private ListView devicesList;						   //ListView to enlist founded devices
	private ArrayList<String> list;						   //Array String that saves the paired devices' names
	private Button conectar; 
	
	private BluetoothAdapter mBluetoothAdapter;		        //Adapter for BT module
	private BluetoothSocket socket;							//Connection's socket
	private InputStream is;								    //InputStream
	private OutputStream os;								//Output Stream for Bluetooth Connection
	private BroadcastReceiver btMonitor = null;
	private boolean okConnection;							//Flag's connection
	
	//Getters and Setters section
	public BluetoothAdapter getBluetoothAdapter(){
		return this.mBluetoothAdapter;
	}
	
	public BluetoothSocket getBluetoothSocket(){
		return this.socket;
	}
	
	public InputStream getInputStream(){
		return this.is;
	}
	public OutputStream getOutputStream(){
		return this.os;
	}
	public BroadcastReceiver getBroadcastReceiver(){
		return this.btMonitor;
	}
	//End Getters and Setters section
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conexion);
<<<<<<< HEAD
		final Button conectar = (Button) findViewById(R.id.conectarButton);
		final ListView devicesListView = (ListView) findViewById(R.id.listView1);
		devicesListView.setAdapter(myAdapter);
		/*
		 * /////////////////////////////////// Validating that BLUETOOTH
		 * exists//
		 */// ////////////////////////////////
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),
					"Device does not support Bluetooth", Toast.LENGTH_SHORT)
					.show();
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 1);
		}
		/*
		 * ///////////////////////////////////////////////////// BLUETOOTH
		 * Paired Devices association with ListView//
		 */// //////////////////////////////////////////////////
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				myAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}
		/*
		 * ////////////////////////////////////////////////////// BLUETOOTH
		 * Discovery action and ListView association//
		 */// ///////////////////////////////////////////////////
		mBluetoothAdapter.startDiscovery();
		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a
					// ListView
					myAdapter
							.add(device.getName() + "\n" + device.getAddress());
				}
			}
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister
												// during onDestroy

		OnClickListener registro = new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ConexionActivity.this,
						Interfaz.class);
				startActivity(intent);
			}
		};
		conectar.setOnClickListener(registro);
	}
	/*
	 * ////////////////////////////////////////// CONNECT BLUETOOTH AS
	 * SERVER//////////////
	 */// ///////////////////////////////////////
	/*
	 * private class AcceptThread extends Thread { private final
	 * BluetoothServerSocket mmServerSocket; public AcceptThread() { // Use a
	 * temporary object that is later assigned to mmServerSocket, // because
	 * mmServerSocket is final BluetoothServerSocket tmp = null; try { //
	 * MY_UUID is the app's UUID string, also used by the client code tmp =
	 * mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID); }
	 * catch (IOException e) { } mmServerSocket = tmp; } public void run() {
	 * BluetoothSocket socket = null; // Keep listening until exception occurs
	 * or a socket is returned while (true) { try { socket =
	 * mmServerSocket.accept(); } catch (IOException e) { break; } // If a
	 * connection was accepted if (socket != null) { // Do work to manage the
	 * connection (in a separate thread) manageConnectedSocket(socket);
	 * mmServerSocket.close(); break; } } }
	 */
	/** Will cancel the listening socket, and cause the thread to finish */
	/*
	 * public void cancel() { try { mmServerSocket.close(); } catch (IOException
	 * e) { } } }
	 */
	/*
	 * ////////////////////////// BLUETOOTH AS CLIENT//////
	 */// ///////////////////////
	/*
	 * private class ConnectThread extends Thread { private final
	 * BluetoothSocket mmSocket; private final BluetoothDevice mmDevice; public
	 * ConnectThread(BluetoothDevice device) { // Use a temporary object that is
	 * later assigned to mmSocket, // because mmSocket is final BluetoothSocket
	 * tmp = null; mmDevice = device; // Get a BluetoothSocket to connect with
	 * the given BluetoothDevice try { // MY_UUID is the app's UUID string, also
	 * used by the server code tmp =
	 * device.createRfcommSocketToServiceRecord(MY_UUID); } catch (IOException
	 * e) { } mmSocket = tmp; } public void run() { // Cancel discovery because
	 * it will slow down the connection mBluetoothAdapter.cancelDiscovery(); try
	 * { // Connect the device through the socket. This will block // until it
	 * succeeds or throws an exception mmSocket.connect(); } catch (IOException
	 * connectException) { // Unable to connect; close the socket and get out
	 * try { mmSocket.close(); } catch (IOException closeException) { } return;
	 * } // Do work to manage the connection (in a separate thread)
	 * manageConnectedSocket(mmSocket); }
	 */
	/** Will cancel an in-progress connection, and close the socket */
	/*
	 * public void cancel() { try { mmSocket.close(); } catch (IOException e) {
	 * } } }
	 */
}
=======
		
		conectar = (Button) findViewById(R.id.conectarButton);
		devicesList = (ListView)findViewById(R.id.listView1);

		onBluetooth(); //Turn on BT when it's turned off
		listDevices();  //List PairedDevices in a ListView
		conectar.setEnabled(true); //Disables button before the connection
		setupBTMonitor(); //Enables btMonitor to check the connection's state

		
				 OnItemClickListener robotListener = new OnItemClickListener(){
						
							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								
								RobotName = list.get(position).toString();
								
								//Asynchronous thread for Bluetooth Connection
								AsyncBluetoothConnection connect = new AsyncBluetoothConnection();  //Find Robot's name between devices
								connect.execute();
								
							}
					};
					
					
					OnClickListener registro = new OnClickListener(){
						
							public void onClick(View v){
								handleConnected();
								sendData("1");				
								Log.i("ENVIANDO DATOS","ENVIE UN 1");
								Intent intent = new Intent (ConexionActivity.this, Interfaz.class);
								startActivity(intent);
							}
						};
						
		conectar.setOnClickListener(registro);
		devicesList.setOnItemClickListener(robotListener);
	}
	
	/*
	 * Function that creates the interface to know BT has connected successfully
	 */
	private void setupBTMonitor() {
		btMonitor = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						"android.bluetooth.device.action.ACL_CONNECTED")) {
					handleConnected();
				}
				if (intent.getAction().equals(
						"android.bluetooth.device.action.ACL_DISCONNECTED")) {
						handleDisconnected();
				}
			}
		};
	}
/*
 * When  devices are connected (Devices are now really connected)
 */
	private void handleConnected() {
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			
			okConnection = true;
		
		} catch (Exception e) {
			is = null;
			os = null;
		}
	}
	
	private void handleDisconnected(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Function that sends data to the Robot
	 */
	void sendData(String Dato) {
		try {
			if (okConnection)
				os.write(Dato.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * If there is no BT, it makes a toast. If there is BT enables an intent to turn it on
	 */
	
	private void onBluetooth(){
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
		}
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent,1);
		}
	}
	
	/*
	 * List the paired devices  in the phone and show them in a ListView
	 */
	private void listDevices(){
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		
		list = new ArrayList<String>();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        //list.add(device.getName() + "\n" + device.getAddress());
		    	list.add(device.getName());
		    }
		    Toast.makeText(getApplicationContext(), "Showing Paired Devices",
		    						Toast.LENGTH_SHORT).show();
		    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.row_list_paired_devices,list);
		    devicesList.setAdapter(adapter);
		}
	}
	
	/*
	 * Find Robot between all pairedDevices
	 */
	
	public void findRobot() {
		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			 pairedDevices = mBluetoothAdapter.getBondedDevices();
			Iterator<BluetoothDevice> it = pairedDevices.iterator();
			while (it.hasNext()) {
				BluetoothDevice bd = it.next();
				if (bd.getName().equalsIgnoreCase(RobotName)) {
					connectToRobot(bd);
					return;
				}
			}
		} catch (Exception e) {
			Log.e("Find-Robot-Function", "Failed in findRobot() " + e.getMessage());
		}
	}
	
	/*
	 * To establish connection with the selected device. Return true if connection was successful and false if it wasn't
	 */
	
	private boolean connectToRobot(BluetoothDevice bd) {
		try {
			socket = bd.createRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			Log.e("CONNECTION A ROBOT","Estoy por hacer true");
			return true;
		} catch (Exception e) {
			Log.e("CONNECTION TO ROBOT",
					"Error interacting with remote device [" + e.getMessage() + "]");
			return false;
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	/////////////////CLASE PARA LA CONEXION BLUETOOTH EN SEGUNDO PLANO////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
					private class AsyncBluetoothConnection extends AsyncTask<Void, Integer, Boolean> {
						
								@Override
								protected Boolean doInBackground(Void... params) {
									// TODO Auto-generated method stub
									findRobot();
									
									return true;
								}
								
								@Override
								protected void onPreExecute(){
								}
				    }
}
>>>>>>> origin/Jesús
