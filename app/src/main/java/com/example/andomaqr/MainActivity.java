package com.example.andomaqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andomaqr.db.AndomaSQLiteOpenHelper;
import com.example.andomaqr.scan.ScanFragment;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db = null;
    private Cursor docCursor;
    private AndomaSQLiteOpenHelper andomaSQLHelper;
    private SimpleCursorAdapter docAdapter;
    private CursorAdapter cursorAdapter;


    private ListView lstDocView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        andomaSQLHelper = new AndomaSQLiteOpenHelper(getApplicationContext());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstDocView = findViewById(R.id.list);
        lstDocView.setAdapter(getAdapterList());
        cursorAdapter = (CursorAdapter) lstDocView.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId(); ;
        if (id ==R.id.action_settings){
            return true;
        } else  if(id == R.id.action_next) {
            // Запускаем вторую активность для сканирования
            Intent intent = new Intent(MainActivity.this, FragmentScanActivity.class);
            intent.putExtra(FinalStaticString.FOR_DOC_GUID_SCANNING, true);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private  void changCursor(){
        //смена курсора для обновления после сохранения в БД
        docCursor = getAllData();
        cursorAdapter.changeCursor(docCursor);
    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //смена курсора для обновления после сохранения в БД
        changCursor();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    //

    public Cursor getAllData() {
        return getReadDB().query(AndomaSQLiteOpenHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    private ListAdapter getAdapterList() {

        //получаем данные из бд в виде курсора
        docCursor = getAllData(); //db.rawQuery("select * from "+ AndomaSQLiteOpenHelper.TABLE_NAME, null);
        printToast("Записей в БД: "+docCursor.getCount());
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {AndomaSQLiteOpenHelper.GUID_DOC, AndomaSQLiteOpenHelper.SEND};
        // создаем адаптер, передаем в него курсор
        docAdapter = new SimpleCursorAdapter(this, R.layout.doc_list,
                docCursor, headers, new int[]{R.id.doc_guid_view, R.id.send_doc_view}, 0);
        //header.setText("Найдено элементов: " +  userCursor.getCount());
        return docAdapter;

    }

    private SQLiteDatabase getWriteDB (){
        try {
            db =andomaSQLHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            printToast("Ошибка БД! "+ex.getMessage());
        }
        return db;
    }

    private SQLiteDatabase getReadDB (){
        try {
            db = andomaSQLHelper.getReadableDatabase();
        }
        catch (SQLiteException ex){
            printToast("Ошибка БД! "+ex.getMessage());
        }
        return db;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        docCursor.close();
    }

    private void printToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }
}