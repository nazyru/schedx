
package com.nazir.schedx.remainder;

import android.content.*;
import android.util.Log;

public class RebootReceiver extends BroadcastReceiver
{

    public RebootReceiver()
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        
        context.startService(new Intent(context, RebootService.class));
    }
}
