package free.com.mecca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class enMenu extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_en);
		((Mecca)getApplication()).setLanguage(1);
		View mapLay = (View)findViewById(R.id.mapCom_en); 
		mapLay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(enMenu.this,Map.class);
				startActivity(intent);
			}
		});
		
		View Manask = (View)findViewById(R.id.Manask_en);
		Manask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(enMenu.this,EnHajjOmraMenu.class);
				startActivity(intent);
				
			}
		});
		View About = (View)findViewById(R.id.About_en);
		About.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(enMenu.this,AboutView.class);
				startActivity(intent);
				
			}
		});
	}
	
}
