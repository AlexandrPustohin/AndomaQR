package com.example.andomaqr.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.andomaqr.R;
import com.example.andomaqr.db.AndomaSQLiteOpenHelper;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ScanFragment extends androidx.fragment.app.Fragment implements View.OnClickListener {
    private Context mContext;
    private Activity mActivity;
    private DecoratedBarcodeView mBarcodeView;
    private BeepManager mBeepManager;
    private SQLiteDatabase db = null;
    private AndomaSQLiteOpenHelper andomaSQLHelper;
    private boolean for_doc_scan;
    public ScanFragment() {

    }
    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    private void doScan() {
        mBarcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                mBarcodeView.pause();
                //mBeepManager.playBeepSoundAndVibrate();  //Звук при сканировании

                if (result != null
                        && !TextUtils.isEmpty(result.getText())
                        && !TextUtils.isEmpty(result.getBarcodeFormat().name())) {



                    if (result.getBitmap() != null) {
                        result.getText();
                        //Toast.makeText(mContext, getString(Integer.parseInt(result.getText())),
                        //        Toast.LENGTH_SHORT).show();
                    }
                    //сохранять в документы или строки документы
                    //в зависимости от переданного признака

                    saveOnDB(result.getText());
                    //TODO обработка результата
                    Toast.makeText(mContext, for_doc_scan ? "Для сканирования документа":"Что то другое",
                            Toast.LENGTH_SHORT).show();

                    //Intent intent = new Intent(mContext, ScanResultActivity.class);
                    //intent.putExtra(IntentKey.MODEL, code);
                    //startActivity(intent);
                } else {
                    mBarcodeView.resume();
                    doScan();
                    Toast.makeText(mContext, getString(R.string.error_occured_while_scanning),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    private void saveOnDB(String res){

        AndomaSQLiteOpenHelper.insertDocument(getWriteDB(), res);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null) {
            return;
        } else {
            mActivity = getActivity();
        }
        andomaSQLHelper = new AndomaSQLiteOpenHelper(super.getContext());
        initializeViews(view);

        Toast.makeText(mContext, for_doc_scan ? "Для сканирования документа":"Что то другое",
                Toast.LENGTH_SHORT).show();

        doPreRequisites();
        //setListeners();
        doScan();
    }

    private void initializeViews(@NonNull View view) {
       mBarcodeView = view.findViewById(R.id.barcode_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            for_doc_scan = bundle.getBoolean("data");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBarcodeView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        doPreRequisites();
        mBarcodeView.resume();
        doScan();
    }

    private void doPreRequisites() {
        //mBeepManager = new BeepManager(mActivity);
        //TODO
        /*mBeepManager.setVibrateEnabled(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.VIBRATE));
        mBeepManager.setBeepEnabled(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.PLAY_SOUND));
        mBarcodeView.setStatusText(AppConstants.EMPTY_STRING);*/
    }

    @Override
    public void onClick(View view) {

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

    private void printToast(String message){
        Toast toast = Toast.makeText(super.getContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }


}
