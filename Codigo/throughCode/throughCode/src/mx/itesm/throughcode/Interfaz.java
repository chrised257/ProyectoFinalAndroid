package mx.itesm.throughcode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;


public class Interfaz extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interfaz);
		
	    findViewById(R.id.frenteButton).setOnTouchListener(new MyTouchListener());
	    findViewById(R.id.atrasButton).setOnTouchListener(new MyTouchListener());
	    findViewById(R.id.derechaButton).setOnTouchListener(new MyTouchListener());
	    findViewById(R.id.izquierdaButton).setOnTouchListener(new MyTouchListener());
	    findViewById(R.id.ledButton).setOnTouchListener(new MyTouchListener());
	    findViewById(R.id.rgbButton).setOnTouchListener(new MyTouchListener());
	    findViewById(R.id.buzzerButton).setOnTouchListener(new MyTouchListener());
	    
	    findViewById(R.id.linear1).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear2).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear3).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear4).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear5).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear6).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear7).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear8).setOnDragListener(new MyDragListener());
	    findViewById(R.id.linear11).setOnDragListener(new MyDragListener());
	    
	}
	
	  @SuppressLint("NewApi")
	private final class MyTouchListener implements OnTouchListener {
		    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
	  
	  class MyDragListener implements OnDragListener {
		    Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
		    Drawable normalShape = getResources().getDrawable(R.drawable.shape);
			private int action;

		    public boolean onDrag(View v, DragEvent event) {
		      action = event.getAction();
		      switch (event.getAction()) {
		      case DragEvent.ACTION_DRAG_STARTED:
		        // do nothing
		        break;
		      case DragEvent.ACTION_DRAG_ENTERED:
		        v.setBackgroundDrawable(enterShape);
		        break;
		      case DragEvent.ACTION_DRAG_EXITED:
		        v.setBackgroundDrawable(normalShape);
		        break;
		      case DragEvent.ACTION_DROP:
		        // Dropped, reassign View to ViewGroup
		        View view = (View) event.getLocalState();
		        ViewGroup owner = (ViewGroup) view.getParent();
		        owner.removeView(view);
		        LinearLayout container = (LinearLayout) v;
		        container.addView(view);
		        view.setVisibility(View.VISIBLE);
		        break;
		      case DragEvent.ACTION_DRAG_ENDED:
		        v.setBackgroundDrawable(normalShape);
		      default:
		        break;
		      }
		      return true;
		    }
		  }
}
