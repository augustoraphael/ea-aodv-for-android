package android.TextMessenger.view;

import android.TextMessenger.view.Connect;
import android.TextMessenger.view.R;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ButtonListner implements OnClickListener{
	Activity parent;

	public ButtonListner(Activity parent) {
		this.parent = parent;
	}

	//@Override
	public void onClick(View v) {
		if(v.equals(parent.findViewById(R.id.connectButton))){
			Log.d("KLIK", "DER BLEV KLIKKET");
			Connect c = (Connect)parent;
			c.clickConnect();
		
		}
	}
}
