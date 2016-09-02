package com.sadaharu.jacksom.autonfc;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadaharu on 2016/9/1.
 * 这个Activity 是显示手机中已经安装的程序的列表
 */
public class InstalledApplicationListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<String> mPackage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packageInfos)
        {
            mPackage.add(packageInfo.applicationInfo.loadLabel(packageManager) + "\n" + packageInfo.packageName);
        }

        //直接将Adapter加载给系统给的一个布局
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,mPackage);
        setListAdapter(arrayAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent();
        intent.putExtra("package_name", mPackage.get(position));
        setResult(1,intent);
        finish();
    }
}
