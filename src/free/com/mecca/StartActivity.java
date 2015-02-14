package free.com.mecca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		Thread timer = new Thread()
		{
			public void run()
			{
				try
				{
				sleep(3000);	
				}catch(InterruptedException ex)
				{
					ex.printStackTrace();
				}
				finally{
					Intent openMenu = new Intent(getApplicationContext(),Launcher.class);
					startActivity(openMenu);
					finish();
				}
				
			}
		};
		timer.start();
	}

}
