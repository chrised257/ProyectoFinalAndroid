package mx.itesm.throughcode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AcercaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca);
		
		TextView blog = (TextView)findViewById(R.id.tvBlog);
		
        blog.setText(
                Html.fromHtml(
                    "<b>Blog:</b> <a href=\"http://throughcode.blogspot.mx\"> Haz click aquí</a> " +
                    "para ingresar a nuestro blog."));
        blog.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
