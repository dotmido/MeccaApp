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
public class EnHajjOmraMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.en_hajj_omra);
		
		ImageButton IhramBtn = (ImageButton)findViewById(R.id.EnahramBtn);
		IhramBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(EnHajjOmraMenu.this,IhramView.class);
				startActivity(intent);
			}
		});
		
		ImageButton umrahBtn = (ImageButton)findViewById(R.id.EnumrahBtn);
		umrahBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnHajjOmraMenu.this,UmrahView.class);
				startActivity(intent);
				
			}
		});
		ImageButton HajjIfradBtn = (ImageButton)findViewById(R.id.EnHajjIfradBtn);
		HajjIfradBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnHajjOmraMenu.this,HajjIfradView.class);
				startActivity(intent);
				
			}
		});
		ImageButton HajjQiranBtn = (ImageButton)findViewById(R.id.EnHajjQiranBtn);
		HajjQiranBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnHajjOmraMenu.this,HajjQiranView.class);
				startActivity(intent);
				
			}
		});
		ImageButton HajjTamattuBtn = (ImageButton)findViewById(R.id.EnHajjTamattuBtn);
		HajjTamattuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnHajjOmraMenu.this,HajjTamattuView.class);
				startActivity(intent);
				
			}
		});
	}

}
