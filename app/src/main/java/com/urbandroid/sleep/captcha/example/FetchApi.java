package com.urbandroid.sleep.captcha.example;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchApi extends AsyncTask {
    String data;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/todos/1");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.data = bufferedReader.readLine();
            String line = "";

            do {
                line = bufferedReader.readLine();
                if (line != null) {
                    this.data += line;
                }
            } while (line != null);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        ReverseCaptchaActivity.captchaText = this.data;
        ReverseCaptchaActivity.captchaTextView.setText(this.data);
    }
}
