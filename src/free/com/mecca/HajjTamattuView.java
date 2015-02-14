package free.com.mecca;

import android.app.Activity;
import android.os.Bundle;

public class HajjTamattuView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(((Mecca)getApplication()).getLanguageUsed() ==1)
			setContentView(R.layout.hajj_tamattu_view);
		else
			setContentView(R.layout.ar_hajj_tamattu_view);
	}
	

}
