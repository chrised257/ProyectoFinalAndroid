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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

public class GenericDialog extends Activity {

	//Views for getting data from user
	protected EditText durationEdit;
	protected View colorView;
	protected SeekBar frequencySB, redSB, greenSB, blueSB;
	protected CheckBox led1Check, led2Check, led3Check, led4Check;
	protected String instructionType;
	
	//Array with active status for each LED
	private boolean [] mActiveLeds = {false, false, false, false};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_dialog);
		
		Intent intent = getIntent();
		//Get instruction type for filtering views
		instructionType = intent.getStringExtra("instruction_type");
		
		Button cancelButton = (Button)findViewById(R.id.cancelButton);
		Button acceptButton = (Button)findViewById(R.id.acceptButton);
		View frequencyLayout = (View)findViewById(R.id.FrequencyLayout);
		View ledLayout = (View)findViewById(R.id.LedLayout);
		View RBGLayout = (View)findViewById(R.id.RGBLayout);
		
		durationEdit = (EditText)findViewById(R.id.durationEdit);
		frequencySB = (SeekBar)findViewById(R.id.frequencySeekBar);
		led1Check = (CheckBox)findViewById(R.id.led1CheckBox);
		led2Check = (CheckBox)findViewById(R.id.led2CheckBox);
		led3Check = (CheckBox)findViewById(R.id.led3CheckBox);
		led4Check = (CheckBox)findViewById(R.id.led4CheckBox);
		colorView = (View)findViewById(R.id.colorText);
		redSB = (SeekBar)findViewById(R.id.redSeekBar);
		greenSB = (SeekBar)findViewById(R.id.greenSeekBar);
		blueSB = (SeekBar)findViewById(R.id.blueSeekBar);
		
		//Sound frequency for the buzzer. Range go from 20 Hz to 1024 Hz.
		frequencySB.setMax(1004);
		//Each color channel value can be up to 255
		redSB.setMax(255);
		greenSB.setMax(255);
		blueSB.setMax(255);
		colorView.setBackgroundColor(Color.argb(255, redSB.getProgress(), greenSB.getProgress(), blueSB.getProgress()));
		
		//Filter view by instruction type
		switch(instructionType){
		case "move_fwd":
		case "move_back":
		case "move_left":
		case "move_right":
			frequencyLayout.setVisibility(View.GONE);
			ledLayout.setVisibility(View.GONE);
			RBGLayout.setVisibility(View.GONE);
			break;
		case "buzz":
			ledLayout.setVisibility(View.GONE);
			RBGLayout.setVisibility(View.GONE);
			break;
		case "led":
			durationEdit.setVisibility(View.GONE);
			frequencyLayout.setVisibility(View.GONE);
			RBGLayout.setVisibility(View.GONE);
			break;
		case "ledRGB":
			durationEdit.setVisibility(View.GONE);
			frequencyLayout.setVisibility(View.GONE);
			ledLayout.setVisibility(View.GONE);
			redSB.setOnSeekBarChangeListener(new ColorSeekBarListener());
			greenSB.setOnSeekBarChangeListener(new ColorSeekBarListener());
			blueSB.setOnSeekBarChangeListener(new ColorSeekBarListener());
			break;
		}
		
		cancelButton.setOnClickListener(new CancelListener());
		acceptButton.setOnClickListener(new AcceptListener());
	}
	
	
	public void onCheckboxClicked(View view) {
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.led1CheckBox:
	            mActiveLeds[0] = checked;
	            break;
	        case R.id.led2CheckBox:
	            mActiveLeds[1] = checked;
	            break;
	        case R.id.led3CheckBox:
	            mActiveLeds[2] = checked;
	            break;
	        case R.id.led4CheckBox:
	            mActiveLeds[3] = checked;
	            break;
	    }
	}
	
	
	protected class ColorSeekBarListener implements SeekBar.OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			colorView.setBackgroundColor(Color.argb(255, redSB.getProgress(), greenSB.getProgress(), blueSB.getProgress()));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
		
	}
	
	protected class CancelListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED, null);
			finish();
		}
	}
	
	protected class AcceptListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("instruction_type", instructionType);
			float durationValue = 0.0f;
			int milliseconds = 0;
			
			switch(instructionType){
			case "move_fwd":
			case "move_back":
			case "move_left":
			case "move_right":
				try{
					durationValue = Float.parseFloat(durationEdit.getText().toString());
				}
				catch (NullPointerException nptre){
					durationValue = 0.0f;
				}
				catch (NumberFormatException nfe){
					durationValue = 0.0f;
				}
				milliseconds = (int)(durationValue * 1000);
				intent.putExtra("duration", milliseconds);
				break;
			case "buzz":
				try{
					durationValue = Float.parseFloat(durationEdit.getText().toString());
				}
				catch (NullPointerException nptre){
					durationValue = 0.0f;
				}
				catch (NumberFormatException nfe){
					durationValue = 0.0f;
				}
				milliseconds = (int)(durationValue * 1000);
				intent.putExtra("duration", milliseconds);
				intent.putExtra("frequency", frequencySB.getProgress() + 20);
				break;
			case "led":
				intent.putExtra("activeLeds", mActiveLeds);
				break;
			case "ledRGB":
				intent.putExtra("color", Color.argb(255, redSB.getProgress(), greenSB.getProgress(), blueSB.getProgress()));
				break;
			}
			
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}