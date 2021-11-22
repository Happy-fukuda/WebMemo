//ExSampleService.java サービスとノーティフィケーションに関するサンプル

package es.exsample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Random;

public class ExSampleService extends Service{
	NotificationManager nm;
	Random r;
	String[] str = {"兼六園", "21世紀美術館", "近江町市場", "東茶屋街"};
	
	public IBinder onBind(Intent it){
		return null;
	}
	
	public void onCreate(){
		nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);  //ノーティフィケーションマネージャの取得
		r = new Random();  //ランダムな数値の生成用オブジェクト
	}
	
	public int onStartCommand(Intent it, int flag, int id){
		Intent i = new Intent(this, ExSample.class);  //ノーティフィケーションからインテント元を呼び出し
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

		Notification nf = new Notification.Builder(this).setContentTitle("サンプル").setContentText("設定画面に移動します。")
				.setContentIntent(pi).setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis()).build();  //ノーティフィケーションの設定
		nm.notify(0, nf);  //ノーティフィケーションの開始
		
		int m  = r.nextInt(str.length);  //ランダムな数値の生成
		Toast.makeText(this, str[m], Toast.LENGTH_LONG).show();  //トーストでテキストを表示
		return START_STICKY;
	}
	
	public void onDestroy(){
		nm.cancel(0);
	}
}