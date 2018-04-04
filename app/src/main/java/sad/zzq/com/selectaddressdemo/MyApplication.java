package sad.zzq.com.selectaddressdemo;

import android.app.Application;
import android.content.Context;

/**
 *
 * Created by zzq on 2016/12/2 0002.
 */

public class MyApplication extends Application {
    public static MyApplication App;
    private static Context context;
    private final String TAG = "TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //LeakCanary.install(this);
        App = this;

    }
    public static Context getContext(){
        return context;
    }
}
