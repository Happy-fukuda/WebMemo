package es.exsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class SubActivity extends AppCompatActivity {
    float x;
    DataManager db;
    ViewFlipper vf;
    EditText ed;
    Button change;
    Button bt;
    Button finish;
    WebView myWebView;
    int file_id;
    boolean Memo_f=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        file_id = intent.getIntExtra("ID",0);
        db=new DataManager();
        db.loadTable(getPackageName());
        setContentView(R.layout.sub_main);
        vf=(ViewFlipper)findViewById(R.id.flipper);
        ed=(EditText)findViewById(R.id.text_memo);
        bt=(Button)findViewById(R.id.hozonbutton);
        finish=(Button)findViewById(R.id.finishbutton);
        change=(Button)findViewById(R.id.changeButton);
        myWebView=(WebView)findViewById(R.id.webview);
        String memo= db.getMemo(file_id);
        ed.setText(memo);
        change.setOnClickListener(new webViewClickListener());
        bt.setOnClickListener(new TextViewTouchListener());
        finish.setOnClickListener(new FinishClickListener());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(db.getURL(file_id));
    }

    class TextViewTouchListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            SpannableStringBuilder sb = (SpannableStringBuilder)ed.getText();
            db.setMemo(file_id,sb.toString());
        }
    }
    class FinishClickListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            db.closeDB();
            finish();
        }
    }
    class webViewClickListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            if(Memo_f){
                Memo_f=false;
                TranslateAnimation inanim = new TranslateAnimation(ed.getWidth(), 0, 0, 0); //入ってくるビューのアニメーション
                inanim.setDuration(200);  //アニメーションの動作スピード（時間）の設定
                TranslateAnimation outanim = new TranslateAnimation(0,-ed.getWidth(), 0, 0);  //出ていくビューのアニメーション
                outanim.setDuration(200);  //アニメーションの動作スピード（時間）の設定
                vf.setInAnimation(inanim);  //入ってくるアニメーションの設定
                vf.setOutAnimation(outanim);  //出ていくアニメーションの設定
                vf.showNext();
            }
            else{
                Memo_f=true;
                TranslateAnimation inanim = new TranslateAnimation(-myWebView.getWidth(), 0, 0, 0);
                inanim.setDuration(200);
                TranslateAnimation outanim = new TranslateAnimation(0,myWebView.getWidth(), 0, 0);
                outanim.setDuration(200);
                vf.setInAnimation(inanim);
                vf.setOutAnimation(outanim);
                vf.showPrevious();
            }
        }
    }
}