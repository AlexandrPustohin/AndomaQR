package com.example.andomaqr;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DocList extends AppCompatActivity {
    private TextView txvDoc;
    private TextView txvSend;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_list);
        txvDoc = findViewById(R.id.doc_guid_view);
        txvSend = findViewById(R.id.send_doc_view);
    }
}
