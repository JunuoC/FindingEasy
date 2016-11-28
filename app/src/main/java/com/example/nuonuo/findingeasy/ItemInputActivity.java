package com.example.nuonuo.findingeasy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by NuoNuo on 10/29/16.
 */

public class ItemInputActivity extends Activity implements LocationListener {

    private ItemInputActivity mItemInputActivity;

    private TextView titleInput;
    private TextView locationInput;
    private Spinner category;
    private ImageView mImageView;
    private Button confirmButton;

    private LocationManager mlocManager = null;

    private Item curItem = null;
    private Location lastestLoc = null;

    private ArrayAdapter<Item.ItemCat> ItemCatergoryAdapter = null;

    private boolean isNewItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_item);

        mItemInputActivity = this;

        titleInput = (TextView) findViewById(R.id.item_title_input);
        locationInput = (TextView) findViewById(R.id.item_gps_input);
        category = (Spinner) findViewById(R.id.spinner);
        mImageView = (ImageView) findViewById(R.id.image_item);
        confirmButton = (Button) findViewById(R.id.button_confirm);

        // set spinner content
        ItemCatergoryAdapter = new ArrayAdapter<Item.ItemCat>(this, android.R.layout.simple_list_item_1, Item.ItemCat.values());
        category.setAdapter(ItemCatergoryAdapter);

        Intent currentIntent = getIntent();
        isNewItem = (boolean) currentIntent.getExtras().get(Item.KEY_INTENT_ISNEWITEM);
        if ( isNewItem ) {
            curItem = new Item();
            // get location
            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lastestLoc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationInput.setText(getAddressString(lastestLoc));

            // set image from the photo passed by intent
            Bitmap photo = (Bitmap) getIntent().getExtras().get("data");
            curItem.photo = new BitmapDataObject(photo);
            mImageView.setImageBitmap(photo);
        } else {
            curItem = (Item) currentIntent.getExtras().get(Item.KEY_INTENT_ITEM);
            titleInput.setText(curItem.tagName);
            locationInput.setText(curItem.loc);
            category.setSelection(ItemCatergoryAdapter.getPosition(curItem.cat));
            mImageView.setImageBitmap(curItem.photo.getBitmap());
        }


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemContent(curItem);
                if ( isNewItem )
                    MainActivity.mMainActivity.mItems.add(curItem);
                else {
                    int pos = (Integer) getIntent().getExtras().get(Item.KEY_INTENT_ITEM_POSITION);
                    MainActivity.mMainActivity.mItems.remove(pos);
                    MainActivity.mMainActivity.mItems.add(pos, curItem);
                }
                MainActivity.mMainActivity.updateListViewContent();
//                MainActivity.mMainActivity.mStringListTemp.add(curItem.toListViewString());
//                MainActivity.mMainActivity.madapterTemp.notifyDataSetChanged();
                mItemInputActivity.finish();
            }
        });
    }

    public void updateItemContent (Item i) {
        // get content from TV, SPINNER, and load it into Item
        i.setTagName( titleInput.getText().toString() );
        i.setCategorty( (Item.ItemCat) category.getSelectedItem() );
        i.setLocation(getAddressString(lastestLoc));
    }

    private String getAddressString(Location l) {
        String str = l != null ? String.format("%1$.4f,%2$.4f",lastestLoc.getLatitude(), lastestLoc.getLongitude()) : "";
        return str;

        /* sth wrong with the following code

        String strAdd = "Unparsable location!";
        if ( null == l )
            return strAdd;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                return strAdd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;

        */
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
