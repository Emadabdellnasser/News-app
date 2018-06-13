package com.example.emad.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<News_object>> {
    private String requested_url = "https://content.guardianapis.com/search?api-key=3b07e54d-37f4-4bf0-b349-5d2b8574338b&show-tags=contributor";
    private android.app.LoaderManager loaderManager;
    private static int loaded_news_id = 1;
    private myAdapter madapter;
    private ProgressBar progbar;
    ListView lv;
    TextView emptytxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listview);
        emptytxt = (TextView) findViewById(R.id.empty_view);
        progbar = (ProgressBar) findViewById(R.id.progBar);
        progbar.setVisibility(View.GONE);
        lv.setEmptyView(emptytxt);
//reference to the inner class array adapter
        madapter = new myAdapter(this, new ArrayList<News_object>());
        lv.setAdapter(madapter);
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //checking network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            //loading data
            loaderManager = getLoaderManager();
            loaderManager.initLoader(loaded_news_id, null, MainActivity.this);
        } else {
            emptytxt.setText("check your network connection");
        }

//when the user press on any item in the list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                News_object news_object = madapter.getItem(i);
                String link = news_object.getLink();
                if (link != null) {
                    Uri newsUri = Uri.parse(link);
                    Intent bnewssite = new Intent(Intent.ACTION_VIEW, newsUri);
                    startActivity(bnewssite);
                } else {
                    Toast t = Toast.makeText(MainActivity.this, "no link provided", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }

    //loadmanager functions
    @Override
    public android.content.Loader<List<News_object>> onCreateLoader(int i, Bundle bundle) {
        progbar.setVisibility(View.VISIBLE);
        Uri main_uri = Uri.parse(requested_url);
        Uri.Builder URI_builder = main_uri.buildUpon();
        return new Asynclass(this, URI_builder.toString());
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News_object>> loader) {
        madapter.clear();
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News_object>> loader, List<News_object> news) {
        progbar.setVisibility(View.GONE);
        madapter.clear();
        //if have data update list
        if (news != null && !news.isEmpty()) {
            madapter.addAll(news);
        } else {
            emptytxt.setText("ther is no results");
        }
    }

    //the adapter class
    class myAdapter extends ArrayAdapter<News_object> {
        ArrayList<News_object> NEWSLIST;
        Context context;
        LayoutInflater linf;

        public myAdapter(Context context, ArrayList<News_object> objects) {
            super(context, 0, objects);
            NEWSLIST = objects;
            this.context = context;
            linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //List checking
            View listItemView = convertView;
            holder_of_view holder;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item, parent, false);
                // using holder in findview
                holder = new holder_of_view();
                holder.title = (TextView) listItemView.findViewById(R.id.title_text);
                holder.Name = (TextView) listItemView.findViewById(R.id.name_text);
                holder.date = (TextView) listItemView.findViewById(R.id.date_text);
                holder.author = (TextView) listItemView.findViewById(R.id.author_text);
                listItemView.setTag(holder);
            } else {
                holder = (holder_of_view) listItemView.getTag();
            }
            //set data
            holder.Name.setText(NEWSLIST.get(position).getName());
            holder.title.setText(NEWSLIST.get(position).getTitle());
            holder.date.setText(NEWSLIST.get(position).getDate());
            holder.author.setText(NEWSLIST.get(position).getAuthor());

            return listItemView;
        }

        //the holder of the class
        class holder_of_view {
            public TextView Name;
            public TextView title;
            public TextView date;
            public TextView author;


        }
    }
}
