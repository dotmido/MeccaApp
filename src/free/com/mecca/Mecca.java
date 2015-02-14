package free.com.mecca;

import android.app.Application;

public class Mecca extends Application {
	public static int language;
	
	public int getLanguageUsed(){
		return language;
	}
	public void setLanguage( int language)
	{
		this.language = language;
	}
	

}
