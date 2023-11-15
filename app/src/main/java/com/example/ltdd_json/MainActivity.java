 package com.example.ltdd_json;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;

 public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> arrayList;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lvcontent);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        lv.setAdapter(adapter);
        new ReadJSON().execute("https://dummyjson.com/posts");

    }


    private class ReadJSON extends AsyncTask<String, Void, String> {
        StringBuilder stringBuilder = new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String st ="";
                while((st = bufferedReader.readLine())!= null){
                    stringBuilder.append(st);
                }

                bufferedReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("posts");
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObjectSP = jsonArray.getJSONObject(i);
                    String id = jsonObjectSP.getString("id");
                    String title = jsonObjectSP.getString("title");
                    String body = jsonObjectSP.getString("body");
                    String userId = jsonObjectSP.getString("userId");
                    JSONArray arTags = jsonObjectSP.getJSONArray("tags");
                    String reactions = jsonObjectSP.getString("reactions");
                    arrayList.add("id: "+id + "-" + title + "\n" +body + "\nUser ID: " +userId + "\nTags: " +arTags + "\nReactions: " + reactions);

                adapter.notifyDataSetChanged();
                }



            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }
    }

}