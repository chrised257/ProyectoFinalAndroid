/*
 * throughCode - Instruction transmitter for using with compatible robot using mobile devices
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

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("Assert")
public class InstruccionAdapter extends ArrayAdapter< Comando> {
	
	private Context context;
	int layoutResourceId;
	List<Comando> listacomandos;
	
	public InstruccionAdapter(Context context, int resource,
			List<Comando> comandos) {
		super(context, resource, comandos);
		this.context = context;
		this.layoutResourceId = resource;
		this.listacomandos = comandos;
	}
	
	@SuppressLint("NewApi")
	public View getView(int position,View convertView,ViewGroup parent){
		View row = convertView;
		
		if(row == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(layoutResourceId, parent, false);
		}
		
		ImageView comandoImagen = (ImageView)row.findViewById(R.id.imageView1);
		TextView tipoInstruccion = (TextView)row.findViewById(R.id.tipoInstruccion);
		TextView secuenciaDatos = (TextView)row.findViewById(R.id.secuenciaDatos);
		TextView secuenciaInstruccion = (TextView)row.findViewById(R.id.secuenciaInstruccion);
		
		secuenciaDatos.setTextColor(Color.rgb(0, 128, 255));
		secuenciaInstruccion.setTextColor(Color.rgb(0, 204, 102));
		
		Comando comando = listacomandos.get(position);
		ImageView imagen = comando.getImage();
		
		
		comandoImagen.setImageDrawable(imagen.getDrawable()); //ASIGNANDO DRAWABLE PROVENIENTE		
		tipoInstruccion.setText(comando.getTipo());
		secuenciaDatos.setText(comando.getSecuencia());
		secuenciaInstruccion.setText(comando.getIndicacionSecuencia());
		return row;
	}
	
}
