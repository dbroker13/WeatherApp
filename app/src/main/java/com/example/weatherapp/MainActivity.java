package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ExecutionException;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    public class DownloadWeather extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            HttpURLConnection urlConnection = null;

            URL url = null;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    char current = (char) data;

                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "failed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String description = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("City", jsonObject.getString("name"));
                description += jsonObject.getString("name") + " ";
                JSONArray arr = new JSONArray(weatherInfo);
                TextView descriptionView = (TextView) findViewById(R.id.textView);
                for (int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    description += jsonPart.getString("main") + " ";
                    description += jsonPart.getString("description") + " ";
                    Log.i("Main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));


                }
                descriptionView.setText(description);
             //   Log.i("Weather is", weatherInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getWeather(View view){
        TextView inputText = (TextView) findViewById(R.id.inputText);
        String city = inputText.getText().toString();
        //String city = inputText.toString()
//        String city = (String) inputText.getText();
        //Log.i("Info", city);

        String apiCall = "https://api.openweathermap.org/data/2.5/weather?q=";
        String apiKey = "&appid=c53c48b7f938627a400eeda1db07d1b3";
        String fullTitle = apiCall + city + apiKey;
        DownloadWeather task = new DownloadWeather();
        task.execute(fullTitle);
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCenter.start(getApplication(), "a8b074fa-128c-479b-a138-c91df64fc318",
                Analytics.class, Crashes.class);

//        String city = "Lima,OH,US";
//        String apiCall = "https://api.openweathermap.org/data/2.5/weather?q=";
//        String apiKey = "&appid=c53c48b7f938627a400eeda1db07d1b3";
//        String fullTitle = apiCall + city + apiKey;
//        DownloadWeather task = new DownloadWeather();
//        task.execute(fullTitle);

        //DownloadWeather task = new DownloadWeather();
        //task.execute("https://api.openweathermap.org/data/2.5/weather?q=Lima,OH,US&appid=c53c48b7f938627a400eeda1db07d1b3");


    }
}