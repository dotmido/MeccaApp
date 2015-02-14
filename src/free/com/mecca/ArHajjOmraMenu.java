/**
 * 
 */
package free.com.mecca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author Lenovo
 *
 */
public class ArHajjOmraMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ar_hajj_omra);
		
		ImageButton IhramBtn = (ImageButton)findViewById(R.id.ArahramBtn);
		IhramBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(ArHajjOmraMenu.this,IhramView.class);
				startActivity(intent);
			}
		});
		ImageButton umrahBtn = (ImageButton)findViewById(R.id.ArumrahBtn);
		umrahBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(ArHajjOmraMenu.this,UmrahView.class);
				startActivity(intent);
			}
		});
		ImageButton HajjIfradBtn = (ImageButton)findViewById(R.id.ArHajjIfradBtn);
		HajjIfradBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArHajjOmraMenu.this,HajjIfradView.class);
				startActivity(intent);
				
			}
		});
		ImageButton HajjTamattuBtn = (ImageButton)findViewById(R.id.ArHajjTamattuBtn);
		HajjTamattuBtn.setOnClickListener(new OnClickListener() {
			//Edit later
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArHajjOmraMenu.this,HajjTamattuView.class);
				startActivity(intent);
				
			}
		});
		ImageButton HajjQiranBtn = (ImageButton)findViewById(R.id.ArHajjQiranBtn);
		HajjQiranBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArHajjOmraMenu.this,HajjQiranView.class);
				startActivity(intent);
				
			}
		});
		
	}

}
