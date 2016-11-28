package com.example.nuonuo.findingeasy;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 1888;

    InterstitialAd mInterstitialAd;

    public static MainActivity mMainActivity;

    private ListView lv_itemlist = null;
    private EditText et_search = null;
    private Button   bn_additem = null;

    public String mSearchStr = "";


    public List<String> mStringListTemp = new ArrayList<String>();
    public ArrayList<Item> mItems = new ArrayList<Item>();
    public ArrayAdapter<String> madapterTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainActivity = this;


        lv_itemlist = (ListView) findViewById(R.id.listView2);
        bn_additem = (Button) findViewById(R.id.bn_input);
        et_search = (EditText) findViewById(R.id.text_searching);

        //create an interstitial ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2988059555730671/4456037342");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent, CAMERA_REQUEST);
            }
        });

        requestNewInterstitial();


        //et_search.setText("Search...");
        et_search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mSearchStr = et_search.getText().toString();
                updateListViewContent();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void afterTextChanged(Editable s){}
        });


        lv_itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent listitemedit = new Intent(mMainActivity, ItemInputActivity.class);
                listitemedit.putExtra(Item.KEY_INTENT_ISNEWITEM, false);
                listitemedit.putExtra(Item.KEY_INTENT_ITEM, mItems.get(position));
                listitemedit.putExtra(Item.KEY_INTENT_ITEM_POSITION, position);
                startActivity(listitemedit);
            }
        });

        bn_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mMainActivity, ItemInputActivity.class);
//                startActivity(intent);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();

                }
            }
        });

    }

    

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID))
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK ) {
            Intent listitemedit = new Intent(mMainActivity, ItemInputActivity.class);
            listitemedit.putExtras( data.getExtras() );
            listitemedit.putExtra(Item.KEY_INTENT_ISNEWITEM, true);
            startActivity(listitemedit);
        }
    }

    public void updateListViewContent() {
        ArrayList<String> liststr = new ArrayList<String>();

        for ( Item i : mItems ) {
            if (!mSearchStr.isEmpty() && !i.tagName.contains(mSearchStr))
                continue;
            liststr.add(i.toListViewString());
        }

        ArrayAdapter<String> strAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                liststr );

        lv_itemlist.setAdapter(strAdapter);
        strAdapter.notifyDataSetChanged();
    }

    public ListView getListView () {
        return lv_itemlist;
    }
}
