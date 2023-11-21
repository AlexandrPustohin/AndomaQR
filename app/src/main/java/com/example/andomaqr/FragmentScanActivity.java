package com.example.andomaqr;

import static com.example.andomaqr.FinalStaticString.FOR_DOC_GUID_SCANNING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.andomaqr.scan.ScanFragment;

public class FragmentScanActivity extends AppCompatActivity {
    FrameLayout fragmentScan;
    private boolean for_document_scanning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_scan);
        for_document_scanning = getIntent().getBooleanExtra(FOR_DOC_GUID_SCANNING, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentScan =  findViewById(R.id.container);
        Bundle bundle = new Bundle();
        bundle.putBoolean("data", for_document_scanning);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new ScanFragment().newInstance();

            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


}