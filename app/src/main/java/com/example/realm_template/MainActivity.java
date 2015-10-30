package com.example.realm_template;

import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realm_template.databinding.ActivityMainBinding;

import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Inject
    Realm realm;

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text1;
        public final TextView text2;

        public ViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text2 = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getComponent().inject(this);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final RecyclerView list = binding.list;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
        realm = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_data:
                importData();
                return true;
            case R.id.action_increment_all_ages:
                incrementAge();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void importData() {
    }

    private void incrementAge() {
    }

}
