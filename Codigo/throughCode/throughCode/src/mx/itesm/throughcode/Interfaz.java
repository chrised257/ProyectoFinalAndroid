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
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class Interfaz extends Activity {
	
	/*ACTIVITY*/
	 ListView myCommandList;
	 ListView listCommandsToSend;
	 
	/*PARA EL BLUETOOTH*/
	private BluetoothAdapter mBluetoothAdapter;		        //Adapter for BT module
	private BluetoothSocket socket;										//Connection's socket
	private InputStream is;								    						//InputStream
	private OutputStream os;													//Output Stream for BT Connection
	private BroadcastReceiver btMonitor = null;
	private boolean okConnection;										//Flag's connection
	private String RobotName;							   						//Robot's name String
	private Set<BluetoothDevice> pairedDevices;			   	//Aux. to save paired devices located at the phone 
	private ArrayList<String> list;						   						//Array String that saves the paired devices' names
	ArrayAdapter<String> myAdapter;
	boolean searchDevices = false;
	 CommandsAdapter  sendAdaptador;
	List<ImageView> instruccionesList;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interfaz);
		
	   myCommandList = (ListView)findViewById(R.id.listView1);
	   listCommandsToSend = (ListView)findViewById(R.id.listCommandsToSend);
	   
	   instruccionesList = new ArrayList<ImageView>();//INICIALIZANDO LISTA DE INSTRUCCIONES
	   ImageView imagen = new ImageView(this);
	   instruccionesList.add(imagen);
	    sendAdaptador = new CommandsAdapter(getApplicationContext(),
			   										R.layout.row_comandos, instruccionesList);
	    
	    OnDragListener evento = new OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				Toast.makeText(Interfaz.this,"Solté...",Toast.LENGTH_SHORT).show();
				return false;
			}
		};
	    
		listCommandsToSend.setOnDragListener(evento);
		
	   final CommandsAdapter miAdaptador = new CommandsAdapter(getApplicationContext(),
			   								R.layout.row_comandos,getDataForListView(Interfaz.this));
		
				   OnItemClickListener seleccion = new OnItemClickListener(){
			
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							view.setOnTouchListener(new MyTouchListener());
					    	Toast.makeText(Interfaz.this,  "Comando seleccionado" , Toast.LENGTH_SHORT).show();				
						}
			       	
			       };
			       
				   OnItemClickListener envio = new OnItemClickListener(){
			
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
					    	Toast.makeText(Interfaz.this, "Comando por enviar",  Toast.LENGTH_SHORT).show();				
						}
			       	
			       };
       
       listCommandsToSend.setOnItemClickListener(envio);
       listCommandsToSend.setAdapter(sendAdaptador);
       
       myCommandList.setOnItemClickListener(seleccion);
       myCommandList.setAdapter(miAdaptador);
	    
	}
	
	 public List<ImageView> getDataForListView(Context context)
     {
     	ImageView comando;
     	List<ImageView> listCommands = new ArrayList<ImageView>();
     	
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
						        view.setVisibility(View.VISIBLE);
						        
				        return true;
				      } 
				      
				      else {
				        return false;
				      }
		    	}
		 }
	  
	  class MyDragListener implements OnDragListener {

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
		        // Dropped, reassign View to ViewGroup
		        View view = (View) event.getLocalState();
		        ViewGroup owner = (ViewGroup) view.getParent();
		        //owner.removeView(view);
		       
		        ListView container = (ListView) v;
		        
		        ImageView oldView = (ImageView) view;
		        ImageView newView = new ImageView(getApplicationContext());
		        newView.setImageBitmap(((BitmapDrawable) oldView.getDrawable()).getBitmap());
		        
		        instruccionesList.add(newView);
		        sendAdaptador.notifyDataSetChanged();
		        view.setVisibility(View.VISIBLE);

		        
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
