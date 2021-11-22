package es.exsample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    static boolean open_db=false;
    SQLiteDatabase db;  //データベースオブジェクトの生成
    public void createTable (String path){
        if(!open_db) {
            String str = "data/data/" + path + "/Sample.db";  //データベースの保存先の指定
            db = SQLiteDatabase.openOrCreateDatabase(str, null);  //データベースオブジェクトの生成
            open_db = true;
            String qry0 = "CREATE TABLE IF NOT EXISTS Folder" + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, parent_dir int)";  //テーブル作成のクエリ
            String qry1 = "CREATE TABLE IF NOT EXISTS Files" + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, parent_dir int,URL String,Memo String)";  //テーブル作成のクエリ
            String qry2 = "INSERT INTO Folder(name) SELECT 'Home' WHERE NOT EXISTS (SELECT id FROM Folder WHERE id=1)";
            db.execSQL(qry0);  //クエリの実行 2回目以降の実行する際にはコメントアウトする
            db.execSQL(qry1);  //2回目以降の実行する際にはコメントアウトする
            db.execSQL(qry2);
        }
    }
    public void loadTable(String path){
        if(!open_db) {
            String str = "data/data/" + path + "/Sample.db";  //データベースの保存先の指定
            db = SQLiteDatabase.openOrCreateDatabase(str, null);
            open_db = true;
        }
    }
    public List<Integer> getDirChildren(int id){
        String qry = "SELECT id FROM Folder WHERE parent_dir="+id;
        List<Integer> list = new ArrayList<Integer>();
        Cursor cr = db.rawQuery(qry,null);
        ////////null だからかも
        if (cr==null) return list;
        while(cr.moveToNext()){  //カーソルを一つづつ動かしデータを取得
            int i = cr.getColumnIndex("id");  //データをテーブルの要素ごとに取得
            list.add(cr.getInt(i));
        }
        return list;
    }
    public List<Integer> getFileChildren(int id){
        String qry = "SELECT id FROM Files WHERE parent_dir="+id;
        List<Integer> list = new ArrayList<Integer>();
        Cursor cr = db.rawQuery(qry, null);
        if (cr==null)return list;
        while(cr.moveToNext()){  //カーソルを一つづつ動かしデータを取得
            int i = cr.getColumnIndex("id");  //データをテーブルの要素ごとに取得
            list.add(cr.getInt(i));
        }
        return list;
    }
    public String getURL(int id){
        String qry = "SELECT URL FROM Files WHERE id="+id;
        Cursor cr = db.rawQuery(qry, null);
        if (cr==null)return null;
        int i;
        if (cr.moveToNext()) {i = cr.getColumnIndex("URL");}
        else return null;
        return cr.getString(i);
    }
    public String getFileName(int id){
        String qry = "SELECT name FROM Files WHERE id="+id;
        Cursor cr = db.rawQuery(qry, null);
        if (cr==null)return null;
        int i;
        if (cr.moveToNext()) {i = cr.getColumnIndex("name");}
        else return null;

        return cr.getString(i);
    }
    public String getDirName(int id){
        String qry = "SELECT name FROM Folder WHERE id="+id;
        Cursor cr = db.rawQuery(qry, null);
        if (cr==null)return null;
        int i;
        if (cr.moveToNext()) {i = cr.getColumnIndex("name");}
        else return null;

        return cr.getString(i);
    }
    public String getMemo(int id){
        String qry = "SELECT Memo FROM Files WHERE id="+id;
        Cursor cr = db.rawQuery(qry, null);
        if (cr==null)return null;
        int i;
        if (cr.moveToNext()) {i = cr.getColumnIndex("Memo");}
        else return null;

        return cr.getString(i);
    }
    public void setMemo(int id,String str){
        String qry = "update Files set Memo='"+str+"' WHERE id="+id;
        db.execSQL(qry);
    }
    public void setNewFile(String name,int parent_dir,String url){
        String qry = "INSERT INTO Files(name,parent_dir,URL) VALUES ('"+name+"',"+parent_dir+",'"+url+"')";
        db.execSQL(qry);
    }
    public void setNewFolder(String name,int parent_dir){
        String qry = "INSERT INTO Folder(name,parent_dir) VALUES ('"+name+"',"+parent_dir+")";
        db.execSQL(qry);
    }
    public void deleteFile(int id){
        String qry = "DELETE FROM Files WHERE id="+id;
        db.execSQL(qry);
    }
    public void deleteFolder(int id){
        String qry = "DELETE FROM Folder WHERE id="+id;
        String qry1 = "DELETE FROM Folder WHERE parent_dir="+id;
        String qry2 ="DELETE FROM Files WHERE parent_dir="+id;
        db.execSQL(qry);
    }
    public void closeDB(){
        if (open_db){db.close();open_db=false;}
    }

}