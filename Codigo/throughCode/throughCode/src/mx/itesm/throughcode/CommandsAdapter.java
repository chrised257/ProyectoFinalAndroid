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

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

@SuppressLint("Assert")
public class CommandsAdapter extends ArrayAdapter< ImageView> {
	
	private Context context;
	int layoutResourceId;
	List<ImageView> listacomandos;
	
	public CommandsAdapter(Context context, int resource,
			List<ImageView> comandos) {
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
		
		ImageView comando = listacomandos.get(position);
		
		comandoImagen.setImageDrawable(comando.getDrawable()); //ASIGNANDO DRAWABLE PROVENIENTE		
		
		
		
		return row;
	}
	
}
