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
