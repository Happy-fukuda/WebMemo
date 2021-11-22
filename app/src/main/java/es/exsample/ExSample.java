//ExSample3_03.java ビューフリッパーに関するサンプル
package es.exsample;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExSample extends AppCompatActivity {

    float x;
    boolean create_db=false;
    String str;
    public int current_dir;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_dir=1;
        setContentView(R.layout.activity_main);
        listDisplay();

    }
    @Override
    protected void onResume() {
        super.onResume();
        listDisplay();

    }

    public void listDisplay(){
        DataManager db=new DataManager();
        //起動時一回だけ生成（存在していたら生成しない）する
        if(!create_db) {db.createTable(getPackageName());create_db=true;}
        else db.loadTable(getPackageName());

        //フォルダとファイルの表示

        //folder優先
        List<ImageData> data_ls=new ArrayList<ImageData>();
        for(int id:db.getDirChildren(current_dir)) data_ls.add(new ImageData(R.drawable.folder,db.getDirName(id),id,true));
        for(int id:db.getFileChildren(current_dir)) data_ls.add(new ImageData(R.drawable.file,db.getFileName(id),id,false));
        db.closeDB();

        RecyclerView recycler_view = (RecyclerView) findViewById(R.id.recycler_view);  //リサイクラービューの取得
        recycler_view.setHasFixedSize(true);  //リサイクラービューのサイズを固定

        //GridLayoutManager manager =new GridLayoutManager(this,2,RecyclerView.VERTICAL, false); //リニアレイアウトマネージャーの取得
        //manager.setOrientation(LinearLayoutManager.VERTICAL); //リニアレイアウトを縦に使用
        //GridLayoutManager manager = new GridLayoutManager(this, 2);

        recycler_view.setLayoutManager(new GridLayoutManager(this,2,RecyclerView.VERTICAL, false));  //リサイクラービューにレイアウトマネージャーを設定
        RecyclerAdapter adapter=new RecyclerAdapter(data_ls);
        adapter.setOnItemClickListener(new RecyclerAdapter.onItemClickListener() {
            @Override
            public void onClick(View view,int id) {
                current_dir=id;
                listDisplay();
            }
        });
        RecyclerView.Adapter adapter2=adapter; //リサイクラービューに渡す値を準備
        recycler_view.setAdapter(adapter2);  //リサイクラービューに値を設定
    }


    public boolean onCreateOptionsMenu(Menu menu){  //メニューの項目の設定
        menu.add(Menu.NONE, 0, 0, "URL追加");
        menu.add(Menu.NONE, 1, 1, "フォルダ作成");
        menu.add(Menu.NONE, 2, 2, "削除");
        menu.add(Menu.NONE, 3, 3, "Topへ戻る");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem mi){  //メニュー選択時の処理
        Intent intent = new Intent(this, MakeFile.class);
        //AlertDialog.Builder dl = new AlertDialog.Builder(ExSample.this); //ダイアログの生成
        switch(mi.getItemId()){  //メニュー選択時の具体的な処理
            case 0:
                intent.putExtra("ID", current_dir);
                intent.putExtra("File",true);
                intent.putExtra("make",true);
                startActivity(intent);
                break;
            case 1:
                intent.putExtra("ID", current_dir);
                intent.putExtra("File",false);
                intent.putExtra("make",true);
                startActivity(intent);
                break;
            case 2:
                intent.putExtra("ID", current_dir);
                intent.putExtra("File",false);
                intent.putExtra("make",false);
                startActivity(intent);
                break;
            case 3:
                current_dir=1;
                listDisplay();
                break;
        }
        //listDisplay();
        return true;
    }
}
