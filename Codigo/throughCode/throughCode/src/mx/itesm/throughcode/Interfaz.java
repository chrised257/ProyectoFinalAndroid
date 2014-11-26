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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class Interfaz extends Activity {
	
	/*ACTIVITY*/
	 ListView myCommandList;
	 ListView listCommandsToSend;
	 
	/*PARA EL BLUETOOTH*/
	private BluetoothAdapter mBluetoothAdapter;		        //Adaptador de módulo BT
	private BluetoothSocket socket;										//Socket de conexión
	private InputStream is;								    						//InputStream
	private OutputStream os;													//Output Stream for BT Connection
	private BroadcastReceiver btMonitor = null;					//Monitor que escucha todos los eventos Bluetooth
	private boolean okConnection;										//Bandera de conexión establecida 1: si  ; 0:no
	private String RobotName;							   						//String del nombre del Robot
	private Set<BluetoothDevice> pairedDevices;			   	//Auxiliar para guardar los objetos Bluetooth device de los dispositivos pareados
	private ArrayList<String> list;						   						//Array String que guarda los nombres de los dispositivos pareados
	boolean searchDevices = false;
	 CommandsAdapter  sendAdaptador;									//Adaptador del ListView para los comandos a enviar
	List<ImageView> instruccionesList;									//List donde están las instrucciones a enviar.
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interfaz);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		//Para conectarse con el otro dispositivo
		Bundle datos = getIntent().getExtras();
		 RobotName = (String) datos.get("nombreRobot");
		Toast.makeText(getApplicationContext(), "Conectando a " + RobotName + "...", Toast.LENGTH_SHORT).show();
		 //Asynchronous thread for Bluetooth Connection
			AsyncBluetoothConnection connect = new AsyncBluetoothConnection();  //Find Robot's name between devices
			connect.execute();
		 
			setupBTMonitor(); //Enables btMonitor to check the connection's state
		///////////////////////////////////////////////////////////////////////////////////////////////////
			
	   myCommandList = (ListView)findViewById(R.id.listView1);
	   listCommandsToSend = (ListView)findViewById(R.id.listCommandsToSend);
	   Button enviar = (Button)findViewById(R.id.aboutButton);
	   
	   final CommandsAdapter miAdaptador = new CommandsAdapter(getApplicationContext(),
					R.layout.row_comandos,getDataForListView(Interfaz.this));
	   
	    sendAdaptador = new CommandsAdapter(getApplicationContext(),
			   										R.layout.row_comandos, instruccionesList);
					
	   //Listener para botón enviar
	   			enviar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
				
					}
				});
	   			
	   	//Listener para lista de comandos disponibles
				   OnItemClickListener seleccion = new OnItemClickListener(){
			
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							//view.setOnTouchListener(new MyTouchListener());
					    	
							ImageView addedView = new ImageView(getApplicationContext());
							
							switch(position)
							{
							case 0:
												addedView.setImageResource(R.drawable.frente);
										break;
							case 1:
												addedView.setImageResource(R.drawable.atras);
										break;
							case 2:
												addedView.setImageResource(R.drawable.izquierda);
										break;
							case 3:
												addedView.setImageResource(R.drawable.derecha);
										break;
							case 4:
												addedView.setImageResource(R.drawable.led);
										break;
							case 5:
												addedView.setImageResource(R.drawable.rgb);
										break;
							case 6:
												addedView.setImageResource(R.drawable.buzzer);
										break;
										
									default:
										break;
							}
							instruccionesList.add(addedView);
							sendAdaptador.notifyDataSetChanged();
							Toast.makeText(Interfaz.this,  "Item selected: " + position  , Toast.LENGTH_LONG).show();				
						}
			       	
			       };
			       
	  //Listener para el list view de las instrucciones a enviar.
				   OnItemClickListener envio = new OnItemClickListener(){
			
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
					    	Toast.makeText(Interfaz.this, "Comando por enviar",  Toast.LENGTH_SHORT).show();				
						}
			       	
			       };
			       
				
       listCommandsToSend.setOnItemClickListener(envio);
       listCommandsToSend.setAdapter(sendAdaptador);
       listCommandsToSend.setOnDragListener(new MyDragListener());
       
       myCommandList.setOnItemClickListener(seleccion);
       myCommandList.setAdapter(miAdaptador);
	    
	}
	
	 public List<ImageView> getDataForListView(Context context)
     {
     	ImageView comando;
     	List<ImageView> listCommands = new ArrayList<ImageView>();
     			
     			instruccionesList = new ArrayList<ImageView>();//Se inicializa la lista de instrucciones también
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.frente);
     	listCommands.add(comando);
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.atras);
     	listCommands.add(comando);
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.izquierda);
     	listCommands.add(comando);
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.derecha);
     	listCommands.add(comando);
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.led);
     	listCommands.add(comando);
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.rgb);
     	listCommands.add(comando);
     	
     	comando = new ImageView(context);
     	comando.setImageResource(R.drawable.buzzer);
     	listCommands.add(comando);
     	
     	return listCommands;
     }

	private final class MyTouchListener implements OnTouchListener {
		    
			public boolean onTouch(View view, MotionEvent motionEvent) {
		     
				    	if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
						       
				    			ClipData data = ClipData.newPlainText("", "");
						        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
						        view.startDrag(data, shadowBuilder, view, 0);
						        view.setVisibility(View.INVISIBLE);
						        
				        return true;
				      } 
				      
				      else {
				        return false;
				      }
		    	}
		 }
	  
	  class MyDragListener implements View.OnDragListener {

			private int action;

		    public boolean onDrag(View v, DragEvent event) {
		      action = event.getAction();
		      switch (action) {
		      case DragEvent.ACTION_DRAG_STARTED:
		    	  		Log.d("ACTION_DRAG_STARTED", "Empecé mi acción de drag");
		        break;
		      case DragEvent.ACTION_DRAG_ENTERED:
		    	  		Log.d("ACTION_DRAG_ENTERED", "Entré a mi acción de drag.");
		        break;
		      case DragEvent.ACTION_DRAG_EXITED:
		    	  		Log.d("ACTION_DRAG_EXITED", "Salí de mi acción de drag.");
		        break;
		      case DragEvent.ACTION_DROP:
					        /////////////////////////////////////
					    	////SECCION DEL DRAG AND DROP////
					    	////////////////////////////////////
		    	  			
		        break;
		      case DragEvent.ACTION_DRAG_ENDED:
		    	  		Log.d("ACTION_DRAG_ENDED", "Terminé de mover mi objeto.");
		      default:
		        break;
		      }
		      return true;
		    }
		  }
	  
	  /*PARA GENERAR EL MENU DE GUARDAR Y ABRIR*/
	  public boolean onCreateOptionsMenu(Menu menu)
	    {
	    	MenuInflater menuInflater = getMenuInflater();
	    	menuInflater.inflate(R.menu.menu_dispositivos,menu);
	    	return true;
	    }
	  
	  /*CUANDO ES SELECCIONADO CADA OPCION DE MENU*/
	  public boolean onOptionsItemSelected(MenuItem item)
	    {
	    	//Log.e("onOptionSelected",item.toString());
	    	switch(item.getItemId())
	    	{
	    	   case R.id.open:
	    		   Toast.makeText(Interfaz.this,"Abriendo...", Toast.LENGTH_SHORT).show();
	    		return true;
	   	
	    	   case R.id.save:
	    		   Toast.makeText(Interfaz.this,"Guardando...", Toast.LENGTH_SHORT).show();
	    		   return true;
	    		   
	    	   default:
	    		   return super.onOptionsItemSelected(item);
	    	}
	    }
	  
	  /*///////////////////////////////////////////////////////////////
	   * EMPIEZA CÓDIGO DEL BLUETOOTH PARA REALIZAR LA CONEXIÓN.///
	   *///////////////////////////////////////////////////////////////
	  
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
			
										findRobot();
										
										return true;
									}
									
									@Override
									protected void onPreExecute(){
									}
					    }
}
