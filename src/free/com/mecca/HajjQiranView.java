package free.com.mecca;

import android.app.Activity;
import android.os.Bundle;

public class HajjQiranView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(((Mecca)getApplication()).getLanguageUsed() ==1)
			setContentView(R.layout.hajj_qiran_view);
		else
			setContentView(R.layout.ar_hajj_qiran_view);
			
	}

}
