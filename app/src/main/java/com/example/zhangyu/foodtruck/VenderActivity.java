package com.example.zhangyu.foodtruck;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.zhangyu.foodtruck.db_Layout.Dao_Sqlite;


public class VenderActivity extends Activity {

    private TextView nameView;
    private TextView occurrenceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender);

        nameView = (TextView) findViewById(R.id.vendor_name);
        occurrenceView = (TextView) findViewById(R.id.occurrence);

        String name = getIntent().getStringExtra("name");
        nameView.setText(name);

        Dao_Sqlite dao = new Dao_Sqlite(this);
        occurrenceView.setText(dao.getVendorOccurrences(name) + "");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vender, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
