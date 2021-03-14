package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    List<ResolveInfo> myList;
    List<String> packageNameList = new ArrayList<String>();
    List<String> appName = new ArrayList<String>();
    List<Drawable> logoList = new ArrayList<Drawable>();

    //Test URL for accepting the Payments using some dummy details.
    // hardcoded, in real time application, this has to be created by making API calls to gateway (Razorpay)
    String intentURI = "upi://pay?pa=razorpay@icici&pn=my2ndCompany&tr=EZV2021031322302877107229&tn=my2ndCompanyTestflow&am=1000&cu=INR&mc=5411";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUpiSupportedApps();
        myList = getUpiSupportedApps();
        packageNameList = getPackageNameList(myList);
        appName = getAppName(myList);
        logoList = getPackageLogoList(myList);


        //Creating View
        ListView listView = (ListView) findViewById(R.id.list);
        //For Populating List of Data
        customPackageList custompackagelist = new customPackageList(this, appName, logoList);
        listView.setAdapter(custompackagelist);

        //Make payment request
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(intentURI));
                i.setPackage(packageNameList.get(position));

               try {
                   if(i!= null){
                       startActivityForResult(i, position);
                   }
                   else{
                       Toast.makeText(getApplicationContext(),"Intent is Null:"+packageNameList.get(position), Toast.LENGTH_LONG).show();

                   }
                }
               catch (Exception e){
                   Log.e("Error log: ", String.valueOf(e));
               }
            }
        });


    }

    //Fetch ResolveInfo for all the apps which support UPI payment (upi://pay)
    @SuppressLint("WrongConstant")
    private  List<ResolveInfo> getUpiSupportedApps(){
        Intent intent = new Intent();
        intent.setData(Uri.parse("upi://pay"));
        return context.getPackageManager().queryIntentActivities(intent,131072);
    }

    //Fetch the package name list from teh ResolveInfor of the Packages
    private  List<String> getPackageNameList(List<ResolveInfo> myList){
        List<String > packageName = new ArrayList<String>();
        for (ResolveInfo iterator:myList) {
            Log.e("PackageName",iterator.activityInfo.packageName);
            packageName.add((String)iterator.activityInfo.packageName);
        }
        return  packageName;
    }

    //Fetch the logo for the app using app package name.
    private  List<Drawable> getPackageLogoList(List<ResolveInfo> myList){
        List<Drawable> drawableList = new ArrayList<Drawable>();
        for (ResolveInfo iterator:myList) {
            try {
                drawableList.add(getPackageManager().getApplicationIcon(iterator.activityInfo.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return  drawableList;
    }

    //Fetch App name using app packange name
    private List<String> getAppName(List<ResolveInfo> myList){
        List<String> appNameList = new ArrayList<String>();
        ApplicationInfo appInfo = null;
        for(ResolveInfo iterator:myList){
            try {
                appInfo = getPackageManager().getApplicationInfo(iterator.activityInfo.packageName,PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appNameList.add((String) getPackageManager().getApplicationLabel(appInfo));
        }

        return appNameList;
    }

    //Catch callback from the UPI apps using onActivityResult function.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(),"Got a callback from: "+appName.get(requestCode), Toast.LENGTH_LONG).show();
    }
}



//*****Refactored Code******
//        imageView = (ImageView) findViewById(R.id.imageView2);

//        for (ResolveInfo iterator:myList) {
//            Log.e("PackageName",iterator.activityInfo.packageName);
//        }
//        Drawable appIcon = null;
//        try {
//            appIcon = getPackageManager().getApplicationIcon(myList.get(2).activityInfo.packageName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }


//        imageView.setImageDrawable(appIcon);