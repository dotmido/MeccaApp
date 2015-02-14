package free.com.mecca;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {

	private Context context;
	
	//constructor
	public Connection(Context context)
	{
		this.context = context;
	}
	
	
	//this method check if the application connected to internet or not
	public boolean isConnectingToInternet(){
	ConnectivityManager cManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	if(cManager !=null)
	{
		NetworkInfo [] nInfo  = cManager.getAllNetworkInfo();
		if(nInfo !=null)
		{
			for(int	i=0;i<nInfo.length;i++)
			{
				if(nInfo[i].getState() == NetworkInfo.State.CONNECTED)
				{
					return true;
				}
			}
			
		}
	}
	return false;
	}
}
