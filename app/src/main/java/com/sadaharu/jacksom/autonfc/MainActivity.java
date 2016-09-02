package com.sadaharu.jacksom.autonfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mSelectAutoRunApplication;
    private String mPackageName;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectAutoRunApplication = (Button) findViewById(R.id.button_select_auto_run_application);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()),0);


    }

    public void onNewIntent(Intent intent)
    {
         if (mPackageName == null)
             return;

        //获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeNFCTag(detectedTag);
    }

    //写入NFC的Tag
    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{NdefRecord.createApplicationRecord(mPackageName)});
        int size = ndefMessage.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                //判断NFC标签或者贴纸是否可写
                if (!ndef.isWritable()) {
                    return;
                }
                //判断NFC标签的最大尺寸是否小于将写入的尺寸
                if (ndef.getMaxSize() < size)
                {
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e)
        {

        }
    }

    public void onClick_SelectAutoRunApplication(View view)
    {
        Intent intent = new Intent(this,InstalledApplicationListActivity.class);
        startActivityForResult(intent,0);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //让此Activity的优先级高于三重过滤的
        //当此Activity获取焦点时，在所有中设置为栈顶，在此Activity中处理
        if (mNfcAdapter != null)
        {
            mNfcAdapter.enableForegroundDispatch(this,mPendingIntent,null,null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当此Activity失去焦点时，恢复其为默认
        if (mNfcAdapter != null)
        {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}
