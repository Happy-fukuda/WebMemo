package es.exsample;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MakeFile extends AppCompatActivity {
    DataManager db;
    EditText name;
    EditText ed;
    Button bt;
    Button ca;
    Spinner sp;
    int file_id;
    List<ImageData> data_ls;
    int index_num=-1;
    LinearLayout[] sub_llp =new LinearLayout[5];
    String[] spinnerItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout llp = new LinearLayout(this);  //親リニアレイアウト
        llp.setOrientation(LinearLayout.VERTICAL);
        setContentView(llp);
        Intent intent = getIntent();
        boolean make_f=intent.getBooleanExtra("make",false);
        boolean file_f=intent.getBooleanExtra("File",false);
        file_id = intent.getIntExtra("ID",0);

        db = new DataManager();
        db.loadTable(getPackageName());
        ca=new Button(this);
        ca.setText("キャンセル");
        ca.setOnClickListener(new CancelTouchListener());
        for (int i=0;i<sub_llp.length;i++) sub_llp[i]=new LinearLayout(this);

        //delete act
        if(!make_f){
            TextView tx_name = new TextView(this);
            TextView textView= new TextView(this);
            tx_name.setText("削除するもの");

            data_ls=new ArrayList<ImageData>();
            for(int id:db.getDirChildren(file_id)) data_ls.add(new ImageData(R.drawable.folder,db.getDirName(id),id,true));
            for(int id:db.getFileChildren(file_id)) data_ls.add(new ImageData(R.drawable.file,db.getFileName(id),id,false));
            spinnerItems=new String[data_ls.size()];
            for (int i=0;i<data_ls.size();i++) spinnerItems[i]=data_ls.get(i).name;
            if(spinnerItems.length!=0) {
                //Spinner spinner = (Spinner) findViewById(R.id.spinner);
                Spinner spinner=new Spinner(this);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        spinnerItems
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    //　アイテムが選択された時
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        String item = (String) spinner.getSelectedItem();
                        index_num = (int) spinner.getSelectedItemPosition();
                        textView.setText(item);
                    }

                    //　アイテムが選択されなかった
                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                });
                sub_llp[2].addView(spinner);
            }
            else {
                TextView file_none=new TextView(this);
                file_none.setText("削除できるものはありません");
                sub_llp[2].addView(file_none);
            }
            bt = new Button(this);
            bt.setText("削除");
            bt.setOnClickListener(new DeleteTouchListener());
            sub_llp[0].addView(tx_name);
            sub_llp[1].addView(textView);

            sub_llp[3].addView(bt);
            sub_llp[3].addView(ca);
            for (int i=0;i<4;i++) llp.addView(sub_llp[i]);
        }

        //file act
        else if(file_f && make_f) {


            TextView tx_name = new TextView(this);
            tx_name.setText("ファイル名");
            name = new EditText(this);
            name.setEms(10);
            TextView tx_url = new TextView(this);
            tx_url.setText("URL");
            ed = new EditText(this);
            ed.setEms(10);
            bt = new Button(this);
            bt.setText("作成");

            bt.setOnClickListener(new TextViewTouchListener());

            sub_llp[0].addView(tx_name);
            sub_llp[1].addView(name);
            sub_llp[2].addView(tx_url);
            sub_llp[3].addView(ed);
            sub_llp[4].addView(bt);
            sub_llp[4].addView(ca);
            for (int i=0;i<5;i++) llp.addView(sub_llp[i]);
        }
        else{

            TextView tx_name = new TextView(this);
            tx_name.setText("フォルダ名");
            name = new EditText(this);
            name.setEms(10);
            bt = new Button(this);
            bt.setText("作成");
            bt.setOnClickListener(new CreateTouchListener());
            sub_llp[0].addView(tx_name);
            sub_llp[1].addView(name);
            sub_llp[3].addView(bt);
            sub_llp[3].addView(ca);
            for (int i=0;i<4;i++) llp.addView(sub_llp[i]);
        }
    }

    class TextViewTouchListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            SpannableStringBuilder sb = (SpannableStringBuilder)ed.getText();
            SpannableStringBuilder na = (SpannableStringBuilder)name.getText();
            if(na!=null && sb!=null) {
                db.setNewFile(na.toString(), file_id, sb.toString());
            }
            db.closeDB();
            finish();
        }
    }
    class CancelTouchListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            db.closeDB();
            finish();
        }
    }
    class DeleteTouchListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            if (index_num!=-1) {
                if (data_ls.get(index_num).Folder) db.deleteFolder(data_ls.get(index_num).ID);
                else db.deleteFile(data_ls.get(index_num).ID);
            }
            db.closeDB();
            finish();
        }
    }
    class CreateTouchListener implements View.OnClickListener {  //フリップ操作時のアニメーション設定
        public void onClick(View v){
            SpannableStringBuilder na = (SpannableStringBuilder)name.getText();
            if(na!=null)
            db.setNewFolder(na.toString(),file_id);
            db.closeDB();
            finish();
        }
    }
}