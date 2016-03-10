package net.suweya.recycleviewexsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.suweya.recyclerviewex.EndlessRecyclerView;
import net.suweya.recyclerviewex.RecycleViewEx;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final RecycleViewEx view = (RecycleViewEx) findViewById(R.id.recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);
        ArrayList<String> data = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            data.add("Item " + i);
        }
        final SimpleAdapter adapter = new SimpleAdapter(data);
        view.setAdapter(adapter);
        final Handler handler = new Handler();
        view.setPageIndex(2);
        view.setOnLoadNextPageListener(new EndlessRecyclerView.OnLoadNextPageListener() {
            @Override
            public void loadNextPage(int page) {
                Log.e("loadNextPage", "page index : " + page);
                switch (page) {
                    case 2:
                        mock(handler, adapter, view, 2000, 25, page);
                        break;
                    case 3:
                        mock(handler, adapter, view, 3000, 25, page);
                        break;
                    case 4:
                        mock(handler, adapter, view, 4000, 15, page);
                        break;
                    default:
                        mock(handler, adapter, view, 2000, 0, page);
                        break;
                }
            }
        });
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.removeCallbacksAndMessages(null);
                view.loadMoreCancel();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (true) {
                            ArrayList<String> data = new ArrayList<>(25);
                            for (int i = 0; i < 25; i++) {
                                data.add("Refresh Item " + 1 + " " + i);
                            }
                            adapter.onRefresh(data);
                            view.setStateOnRefreshSuccess();
                        } else {
                            Toast.makeText(MainActivity.this, "Refresh Error", Toast.LENGTH_SHORT).show();
                            view.setStateOnRefreshError();
                        }
                    }
                }, 5000);
            }
        });
    }

    private void mock(Handler handler, final SimpleAdapter adapter,
                      final RecycleViewEx view,
                      int delay, final int size, final int index) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (true) {
                    ArrayList<String> data = new ArrayList<String>(size);
                    for (int i = 0; i < size; i++) {
                        data.add("Item " + index + " " + i);
                    }
                    adapter.addAll(data);
                    view.loadMoreComplete();
                } else {
                    view.loadMoreError();
                }
            }
        }, delay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
