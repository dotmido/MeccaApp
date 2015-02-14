package free.com.mecca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ArabicMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_button);
		((Mecca)getApplication()).setLanguage(0);
		View mapLay = (View)findViewById(R.id.mapCom); 
		mapLay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArabicMenu.this,Map.class);
				startActivity(intent);
			}
		});
		
		View Manask = (View)findViewById(R.id.Manask);
		Manask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArabicMenu.this,ArHajjOmraMenu.class);
				startActivity(intent);
				
			}
		});
		
		View About = (View)findViewById(R.id.About);
		About.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArabicMenu.this,AboutView.class);
				startActivity(intent);
				
			}
		});
	}
	
	

}
