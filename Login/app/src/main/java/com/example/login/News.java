package com.example.login;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] localDataSet = {"1", "2", "3"};
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        queue = Volley.newRequestQueue(this);
        getNews();

    }

    public void getNews() {

        String url ="http://newsapi.org/v2/everything?q=bitcoin&from=2021-01-04&sortBy=publishedAt&apiKey=c941d355494d4e0fae5f9e02f668c3ad";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    try {
                        JSONObject jsonobj = new JSONObject(response);

                        JSONArray arrayArticles = jsonobj.getJSONArray("articles");

                        List<NewsData> news = new ArrayList<>();

                        for(int i = 0, j = arrayArticles.length(); i < j; i++) {
                            JSONObject obj = arrayArticles.getJSONObject(i);

                            Log.d("NEWS",obj.toString());

                            NewsData newsData = new NewsData();
                            newsData.setTitle(obj.getString("title"));
                            newsData.setDescription(obj.getString("description"));
                            newsData.setUrlToImage(obj.getString("urlToImage"));
                            news.add(newsData);

                        }

                        mAdapter = new CustomAdapter (news);
                        mRecyclerView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


}