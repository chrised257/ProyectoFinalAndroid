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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ConexionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conexion);
		
		final Button conectar = (Button) findViewById(R.id.conectarButton);
		
		OnClickListener registro = new OnClickListener(){
			
			@Override
			public void onClick(View v){
				
				Intent intent = new Intent (ConexionActivity.this, Interfaz.class);
				startActivity(intent);
				
			}
			
		};
		
		conectar.setOnClickListener(registro);
		
	}
	
}
	
	/*Clase para decodificar el envío 
	 * y recibimiento de datos a través de Bluetooth
	 */
	
	/*public class BluetoothDecoder {
		private BluetoothAdapter BA;
		private Set<BluetoothDevice>pairedDevices;
		
	//INICIA BLUETOOTH	
		public void initBluetooth(){
			BA = BluetoothAdapter.getDefaultAdapter();
			if(BA ==null)
			{
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Bluetooth Error...", Toast.LENGTH_SHORT);
			}
			
			else if(!BA.isEnabled()){
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Activando Bluetooth...", Toast.LENGTH_SHORT);
				toast.show();
				BA.enable();
			}
			
			Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOn, 0);    
		}
		
		public void analizeDevices(){
			pairedDevices = BA.getBondedDevices();
		}
		
		//APAGA EL BLUETOOTH
	    public void off(View view){
		      BA.disable();
		      Toast.makeText(getApplicationContext(),"Turned off" ,
		      Toast.LENGTH_LONG).show();
		}
	}
}*/
