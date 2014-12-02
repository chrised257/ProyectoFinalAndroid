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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Interfaz extends Activity {
	/* ACTIVITY */
	ListView myCommandList;
	ListView listCommandsToSend;
	EditText archivosText;
	TextView controlInsTextView;
	int idDraggedData;

	/* PARA EL BLUETOOTH */
	private BluetoothAdapter mBluetoothAdapter; // Adapter for BT module
	private BluetoothSocket socket; // Connection's socket
	private InputStream is; // InputStream
	private OutputStream os; // Output Stream for BT Connection
	private BroadcastReceiver btMonitor = null; // Monitor que escucha todos los
												// eventos Bluetooth

	private boolean okConnection; // Bandera de conexión establecida 1: si ;
									// 0:no
	private String RobotName; // String del nombre del Robot
	private Set<BluetoothDevice> pairedDevices; // Auxiliar para guardar los
												// objetos Bluetooth device de
												// los dispositivos pareados
	boolean searchDevices = false;
	InstruccionAdapter sendAdaptador; // Adaptador del ListView para los
										// comandos a enviar
	List<Comando> instruccionesList; // List donde están las instrucciones a
										// enviar.
	ArrayList<Integer> secuenciaInstrucciones; // Guarda la secuencia de
												// instrucciones a enviar
	List<ImageView> listCommands; // Lista de los comandos disponibles

	// Constante para identificar intents de retorno.
	private final int ID_REQUEST = 222;
	// Posición en instruccionesList donde se almacenaran los datos de regreso
	// del dialog.
	private int mIntentPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interfaz);

		// /////////////////////////////////////////////////////////////////////////////////////////////////
		// Para conectarse con el otro dispositivo
		Bundle datos = getIntent().getExtras();
		RobotName = (String) datos.get("nombreRobot");
		Toast.makeText(getApplicationContext(),
				"Conectando a " + RobotName + "...", Toast.LENGTH_SHORT).show();
		// Asynchronous thread for Bluetooth Connection
		AsyncBluetoothConnection connect = new AsyncBluetoothConnection(); // Find
																			// Robot's
																			// name
																			// between
																			// devices
		connect.execute();

		setupBTMonitor(); // Enables btMonitor to check the connection's state
		// /////////////////////////////////////////////////////////////////////////////////////////////////

		myCommandList = (ListView) findViewById(R.id.listView1);
		listCommandsToSend = (ListView) findViewById(R.id.listCommandsToSend);
		Button enviar = (Button) findViewById(R.id.botonEnviar);
		archivosText = (EditText) findViewById(R.id.archivosText);
		// /////////////////////////////////////////////////////////////////////////////////////////////////

		secuenciaInstrucciones = new ArrayList<Integer>(); // Inicializando la
															// secuencia con que
															// se enviarán las
															// instruccines

		final CommandsAdapter miAdaptador = new CommandsAdapter(
				getApplicationContext(), R.layout.row_comandos,
				getDataForListView(Interfaz.this));

		sendAdaptador = new InstruccionAdapter(getApplicationContext(),
				R.layout.row_commands_tosend, instruccionesList);

		// Listener para botón enviar
		enviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleConnected();
				int i;
				for (i = 0; i < secuenciaInstrucciones.size(); i++) {
					sendData(secuenciaInstrucciones.get(i).toString());
					try {
						Toast.makeText(getApplicationContext(),
								secuenciaInstrucciones.get(i).toString()/*
																		 * is.read
																		 * ()
																		 */,
								Toast.LENGTH_SHORT).show();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

		// Listener para lista de comandos disponibles
		OnItemClickListener seleccion = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// view.setOnTouchListener(new MyTouchListener());
				Comando comando = new Comando();
				ImageView addedView = new ImageView(getApplicationContext());

				switch (position) {
				case 0:
					addedView.setImageResource(R.drawable.frente);
					comando.setSecuencia("0");
					comando.setTipoInstruccion("move_fwd");
					comando.setIndicacionSecuencia("tiempo");
					break;
				case 1:
					addedView.setImageResource(R.drawable.atras);
					comando.setTipoInstruccion("move_back");
					comando.setSecuencia("0");
					comando.setIndicacionSecuencia("tiempo");
					break;
				case 2:
					addedView.setImageResource(R.drawable.izquierda);
					comando.setTipoInstruccion("move_left");
					comando.setSecuencia("0");
					comando.setIndicacionSecuencia("tiempo");
					break;
				case 3:
					addedView.setImageResource(R.drawable.derecha);
					comando.setTipoInstruccion("move_right");
					comando.setSecuencia("0");
					comando.setIndicacionSecuencia("tiempo");
					break;
				case 4:
					addedView.setImageResource(R.drawable.led);
					comando.setTipoInstruccion("led");
					comando.setSecuencia("off, off, off, off");
					comando.setIndicacionSecuencia("on/off, on/off, on/off, on/off");
					break;
				case 5:
					addedView.setImageResource(R.drawable.rgb);
					comando.setTipoInstruccion("ledRGB");
					comando.setSecuencia("0,0,0");
					comando.setIndicacionSecuencia("R, G, B");
					break;
				case 6:
					addedView.setImageResource(R.drawable.buzzer);
					comando.setTipoInstruccion("buzz");
					comando.setSecuencia("0,0");
					comando.setIndicacionSecuencia("tiempo, frecuencia");
					break;

				default:
					addedView.setImageResource(R.drawable.ic_launcher);
					break;
				}

				comando.setImage(addedView);
				instruccionesList.add(comando);
				sendAdaptador.notifyDataSetChanged();
				secuenciaInstrucciones.add(position);
			}

		};

		// Listener para el list view de las instrucciones a enviar.
		OnItemClickListener envio = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// TODO: Intent
				Intent intent = new Intent(Interfaz.this, GenericDialog.class);
				String value = instruccionesList.get(position).getTipo();
				intent.putExtra("instruction_type", value);
				mIntentPosition = position;
				startActivityForResult(intent, ID_REQUEST);
			}
		};

		// Listener para borrar instrucciones con un click largo
		OnItemLongClickListener borrar = new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				instruccionesList.remove(position);
				secuenciaInstrucciones.remove(position);
				sendAdaptador.notifyDataSetChanged();
				return true;
			}
		};

		listCommandsToSend.setOnItemLongClickListener(borrar);

		listCommandsToSend.setOnItemClickListener(envio);
		listCommandsToSend.setAdapter(sendAdaptador);
		listCommandsToSend.setOnDragListener(new MyDragListener());

		myCommandList.setOnItemClickListener(seleccion);
		myCommandList.setAdapter(miAdaptador);

	}

	// Recuperar los datos del intent resultante y almacenarlos en la posicion
	// correspondiente.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID_REQUEST) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				Comando cmd = instruccionesList.get(mIntentPosition);
				int duracion = 0, frecuencia = 20;

				switch (bundle.getString("instruction_type")) {
				case "move_fwd":
				case "move_back":
				case "move_left":
				case "move_right":
					duracion = bundle.getInt("duration");
					cmd.setTiempo(duracion);
					cmd.setSecuencia(Integer.toString(duracion));
					break;
				case "buzz":
					duracion = bundle.getInt("duration");
					frecuencia = bundle.getInt("frequency");
					cmd.setTiempo(duracion);
					cmd.setFrecuencia(frecuencia);
					cmd.setSecuencia(Integer.toString(duracion) + ","
							+ Integer.toString(frecuencia));
					break;
				case "led":
					boolean[] ledsActivos = bundle
							.getBooleanArray("activeLeds");
					int led1 = ledsActivos[0] ? 1 : 0;
					int led2 = ledsActivos[1] ? 2 : 0;
					int led3 = ledsActivos[2] ? 4 : 0;
					int led4 = ledsActivos[3] ? 8 : 0;
					int ledBitmask = led1 | led2 | led3 | led4;
					cmd.setLED(ledBitmask);
					String ledSecuencia = "";
					ledSecuencia = ledSecuencia.concat(ledsActivos[0] ? "on,"
							: "off,");
					ledSecuencia = ledSecuencia.concat(ledsActivos[1] ? "on,"
							: "off,");
					ledSecuencia = ledSecuencia.concat(ledsActivos[2] ? "on,"
							: "off,");
					ledSecuencia = ledSecuencia.concat(ledsActivos[3] ? "on"
							: "off");
					cmd.setSecuencia(ledSecuencia);
					break;
				case "ledRGB":
					int color = bundle.getInt("color");
					int rojo = Color.red(color);
					int verde = Color.green(color);
					int azul = Color.blue(color);
					cmd = instruccionesList.get(mIntentPosition);
					cmd.setRGB(rojo, verde, azul);
					cmd.setSecuencia(Integer.toString(rojo) + ","
							+ Integer.toString(verde) + ","
							+ Integer.toString(azul));
					break;
				}

				// Actualizar lista
				((BaseAdapter) listCommandsToSend.getAdapter())
						.notifyDataSetChanged();
			}
		}
	}

	public List<ImageView> getDataForListView(Context context) {
		ImageView comando;
		listCommands = new ArrayList<ImageView>();

		instruccionesList = new ArrayList<Comando>();// Se inicializa la lista
														// de instrucciones
														// también

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
				View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.VISIBLE);

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
				// ///////////////////////////////////
				// //SECCION DEL DRAG AND DROP////
				// //////////////////////////////////

				ImageView addedView = new ImageView(getApplicationContext());
				Comando comando = new Comando();
				String identificador = "atras";

				switch (identificador) {
				case "frente":
					addedView.setImageResource(R.drawable.frente);
					break;
				case "atras":
					addedView.setImageResource(R.drawable.atras);
					break;
				case "izquierda":
					addedView.setImageResource(R.drawable.izquierda);
					break;
				case "derecha":
					addedView.setImageResource(R.drawable.derecha);
					break;
				case "led":
					addedView.setImageResource(R.drawable.led);
					break;
				case "rgb":
					addedView.setImageResource(R.drawable.rgb);
					break;
				case "buzzer":
					addedView.setImageResource(R.drawable.buzzer);
					break;

				default:
					addedView.setImageResource(R.drawable.ic_launcher);
					break;
				}
				comando.setImage(addedView);
				instruccionesList.add(comando);
				sendAdaptador.notifyDataSetChanged();

				// secuenciaInstrucciones.add(identificador);
				Toast.makeText(Interfaz.this, identificador, Toast.LENGTH_SHORT)
						.show();

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("ACTION_DRAG_ENDED", "Terminé de mover mi objeto.");

			default:
				break;
			}
			return true;
		}
	}

	/* PARA GENERAR EL MENU DE GUARDAR Y ABRIR */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_dispositivos, menu);
		return true;
	}

	/* CUANDO ES SELECCIONADO CADA OPCION DE MENU */
	public boolean onOptionsItemSelected(MenuItem item) {
		// Log.e("onOptionSelected",item.toString());
		String filename = archivosText.getText().toString();
		String fileContents = null;
		
		switch (item.getItemId()) {
		case R.id.open:
			FileInputStream istream = null;
			List<Comando> instruccionesCargadas = new ArrayList<Comando>();
			Comando comando = new Comando();
			 
			try{
				istream = openFileInput(filename);
				int bufferSize = istream.available();
				byte [] buffer = new byte [bufferSize];
				istream.read(buffer);
				fileContents = new String(buffer);
				
				//TODO: Interpretar archivo
				String [] cmds = fileContents.split("#");
				for(int i = 0; i < cmds.length; i++){
					String[] elementos = cmds[i].split(",");
					comando = new Comando();
					ImageView addedView = new ImageView(getApplicationContext());
					for(int j = 0; j < elementos.length; j++){
						String tipo = elementos[0];
						comando.setTipoInstruccion(tipo);
						int tiempo = 0, frecuencia = 20, ledBitmask = 0;
						
						switch (tipo) {
						case "move_fwd":
							addedView.setImageResource(R.drawable.frente);
							tiempo = Integer.parseInt(elementos[1]);
							comando.setTiempo(tiempo);
							comando.setIndicacionSecuencia("tiempo");
							comando.setSecuencia(Integer.toString(tiempo));
							break;
						case "move_back":
							addedView.setImageResource(R.drawable.atras);
							tiempo = Integer.parseInt(elementos[1]);
							comando.setTiempo(tiempo);
							comando.setIndicacionSecuencia("tiempo");
							comando.setSecuencia(Integer.toString(tiempo));
							break;
						case "move_left":
							addedView.setImageResource(R.drawable.izquierda);
							tiempo = Integer.parseInt(elementos[1]);
							comando.setTiempo(tiempo);
							comando.setIndicacionSecuencia("tiempo");
							comando.setSecuencia(Integer.toString(tiempo));
							break;
						case "move_right":
							addedView.setImageResource(R.drawable.derecha);
							tiempo = Integer.parseInt(elementos[1]);
							comando.setTiempo(tiempo);
							comando.setIndicacionSecuencia("tiempo");
							comando.setSecuencia(Integer.toString(tiempo));
							break;
						case "buzz":
							addedView.setImageResource(R.drawable.buzzer);
							tiempo = Integer.parseInt(elementos[1]);
							frecuencia = Integer.parseInt(elementos[2]);
							comando.setTiempo(tiempo);
							comando.setFrecuencia(frecuencia);
							comando.setIndicacionSecuencia("tiempo, frecuencia");
							comando.setSecuencia(Integer.toString(tiempo) + ","
									+ Integer.toString(frecuencia));
							break;
						case "led":
							addedView.setImageResource(R.drawable.led);
							ledBitmask = Integer.parseInt(elementos[1]);
							comando.setLED(ledBitmask);
							comando.setIndicacionSecuencia("on/off, on/off, on/off, on/off");
							int led1 = ledBitmask & 1;
							int led2 = ledBitmask & 2;
							int led3 = ledBitmask & 4;
							int led4 = ledBitmask & 8;
							String ledSecuencia = "";
							ledSecuencia = ledSecuencia.concat(led1 == 1 ? "on,"
									: "off,");
							ledSecuencia = ledSecuencia.concat(led2 == 2? "on,"
									: "off,");
							ledSecuencia = ledSecuencia.concat(led3 == 4 ? "on,"
									: "off,");
							ledSecuencia = ledSecuencia.concat(led4 == 8? "on"
									: "off");
							comando.setSecuencia(ledSecuencia);
							break;
						case "ledRGB":
							addedView.setImageResource(R.drawable.rgb);
							int r = Integer.parseInt(elementos[1]);
							int g = Integer.parseInt(elementos[2]);
							int b = Integer.parseInt(elementos[3]);
							comando.setRGB(r, g, b);
							comando.setIndicacionSecuencia("R, G, B");
							comando.setSecuencia(Integer.toString(r) + ","
									+ Integer.toString(g) + ","
									+ Integer.toString(b));
							break;
						}
					}
					comando.setImage(addedView);
					instruccionesCargadas.add(comando);
				}
				
				istream.close();
				instruccionesList = instruccionesCargadas;
				sendAdaptador = new InstruccionAdapter(getApplicationContext(),
						R.layout.row_commands_tosend, instruccionesList);
				listCommandsToSend.setAdapter(sendAdaptador);
				Toast.makeText(Interfaz.this, "Se abrió el archivo " + filename, Toast.LENGTH_LONG)
				.show();
			}
			catch (FileNotFoundException e){
				Toast.makeText(Interfaz.this, "No se encontró el archivo " + filename, Toast.LENGTH_LONG)
				.show();
				return false;
			}
			catch (IOException e){
				Toast.makeText(Interfaz.this, "Error al cargar los datos del archivo.", Toast.LENGTH_LONG)
				.show();
				return false;
			}
			
			return true;
			
		case R.id.save:
			FileOutputStream ostream = null;
			try{
				ostream = openFileOutput(filename, Context.MODE_PRIVATE);
				fileContents = "";
				//TODO: Guardar archivo
				for( int i = 0; i < instruccionesList.size(); i++ ){
					fileContents = fileContents.concat(instruccionesList.get(i).getTipo());
					
					switch (instruccionesList.get(i).getTipo()) {
					case "move_fwd":
					case "move_back":
					case "move_left":
					case "move_right":
						fileContents = fileContents.concat("," + instruccionesList.get(i).getTiempo());
						break;
					case "buzz":
						fileContents = fileContents.concat("," + instruccionesList.get(i).getTiempo());
						fileContents = fileContents.concat("," + instruccionesList.get(i).getFrecuencia());
						break;
					case "led":
						fileContents = fileContents.concat("," + instruccionesList.get(i).getLED());
						break;
					case "ledRGB":
						fileContents = fileContents.concat("," + instruccionesList.get(i).getR());
						fileContents = fileContents.concat("," + instruccionesList.get(i).getG());
						fileContents = fileContents.concat("," + instruccionesList.get(i).getB());
						break;
					}
					
					//Aun no es el ultimo de los elementos de la lista, agregar separador
					if ( !(i + 1 >= instruccionesList.size()) )
						fileContents = fileContents.concat("#");
				}
				
				ostream.write(fileContents.getBytes());
				ostream.close();
				Toast.makeText(Interfaz.this, "Se guardó el archivo " + filename, Toast.LENGTH_LONG)
				.show();
			}
			catch (FileNotFoundException e){
				Toast.makeText(Interfaz.this, "No se encontró el archivo " + filename, Toast.LENGTH_LONG)
				.show();
				return false;
			}
			catch (IOException e){
				Toast.makeText(Interfaz.this, "Error al guardar los datos del archivo.", Toast.LENGTH_LONG)
				.show();
				return false;
			}
			return true;

		case R.id.delete:
			if(deleteFile(filename)){
				Toast.makeText(Interfaz.this, "Se ha borrado el archivo " + filename, Toast.LENGTH_LONG)
				.show();
			}
			else{
				Toast.makeText(Interfaz.this, "No se encontró el archivo " + filename, Toast.LENGTH_LONG)
				.show();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * ///////////////////////////////////////////////////////////////
	 *  EMPIEZA CÓDIGO DEL BLUETOOTH PARA REALIZAR LA CONEXIÓN.///
	 */// ////////////////////////////////////////////////////////////

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
	 * When devices are connected (Devices are now really connected)
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

	private void handleDisconnected() {
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
			Log.e("Find-Robot-Function",
					"Failed in findRobot() " + e.getMessage());
		}
	}

	/*
	 * To establish connection with the selected device. Return true if
	 * connection was successful and false if it wasn't
	 */

	private boolean connectToRobot(BluetoothDevice bd) {
		try {
			socket = bd.createRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			Log.e("CONNECTION A ROBOT", "Estoy por hacer true");
			return true;
		} catch (Exception e) {
			Log.e("CONNECTION TO ROBOT",
					"Error interacting with remote device [" + e.getMessage()
							+ "]");
			return false;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// ///////////////CLASE PARA LA CONEXION BLUETOOTH EN SEGUNDO
	// PLANO////////////
	// ///////////////////////////////////////////////////////////////////////////////////

	private class AsyncBluetoothConnection extends
			AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			findRobot();

			return true;
		}

		@Override
		protected void onPreExecute() {
		}
	}
}
