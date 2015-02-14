package free.com.mecca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Launcher extends Activity {

	ImageButton arBtn;
	ImageButton enBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		
		arBtn  = (ImageButton)findViewById(R.id.arabicBtn);
		enBtn = (ImageButton)findViewById(R.id.englishBtn);
		
		
		arBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// arabic 
			((Mecca)getApplication()).setLanguage(0);
			Intent intent = new Intent(Launcher.this,ArabicMenu.class);
			startActivity(intent);
			
			}
		});
		
		enBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//english
			((Mecca)getApplication()).setLanguage(1);
			Intent intent = new Intent(Launcher.this,enMenu.class);
			startActivity(intent);
			}
		});
	}
	

}
