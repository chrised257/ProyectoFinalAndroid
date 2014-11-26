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
 * Ing. Mario Isidro de la Fuente Martínez <mario.delafuente@itesm.mx>
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
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ConexionActivity extends Activity {
	
	
	private ListView devicesList;						   						//ListView to enlist founded devices
	private Button conectar; 
	private Button about;
	private ImageButton informacion;
	private BluetoothAdapter mBluetoothAdapter;		        //Adaptador para el módulo BT
	private String RobotName;							   						//String que contiene el nombre del Robot
	private Set<BluetoothDevice> pairedDevices;			   	//Lista de objetos BluetoothDevice de dispositivos pareados
	private ArrayList<String> list;						   						//Array que contiene los Strings de los nombres de dispositivos pareados

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conexion);
		
		conectar = (Button) findViewById(R.id.conectarButton);
		devicesList = (ListView)findViewById(R.id.listView1);
		informacion = (ImageButton)findViewById(R.id.helpButton);
		about = (Button) findViewById(R.id.aboutButton);
		
		onBluetooth(); //Turn on BT when it's turned off
		listDevices();  //List PairedDevices in a ListView

		
				 OnItemClickListener robotListener = new OnItemClickListener(){
						
							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								
								RobotName = list.get(position).toString();
								devicesList.setSelection(position);
								//Toast.makeText(ConexionActivity.this, RobotName + "was selected", Toast.LENGTH_SHORT).show();
								
								Intent intent = new Intent(ConexionActivity.this,Interfaz.class);
								intent.putExtra("nombreRobot", RobotName);//Nombre del dispositivo a conectar.
								startActivity(intent);
								
							}
					};
					
					
					OnClickListener registro = new OnClickListener(){
						
							public void onClick(View v){
								
							}
						};
						
		conectar.setOnClickListener(registro);
		devicesList.setOnItemClickListener(robotListener);
		
OnClickListener regabout = new OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		
        		Intent intent = new Intent (ConexionActivity.this, AcercaActivity.class);
        		startActivity(intent);
        	}
        };
        about.setOnClickListener(regabout);
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
				    Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
				    
				    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				    									R.layout.row_list_paired_devices,list);
				    devicesList.setAdapter(adapter);
				}
			}
	
}