package free.com.mecca;

import android.app.Activity;
import android.os.Bundle;

public class IhramView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(((Mecca)getApplication()).getLanguageUsed() ==1)
		setContentView(R.layout.ihram_view);
		else
			setContentView(R.layout.ihram_view_ar);
	}

}
