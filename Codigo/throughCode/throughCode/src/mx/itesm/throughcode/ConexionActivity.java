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

import android.app.Activity;
import android.os.Bundle;

public class ConexionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion);
    }
}
