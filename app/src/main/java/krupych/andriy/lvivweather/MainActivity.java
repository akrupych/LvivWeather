package krupych.andriy.lvivweather;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text_view);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    String url = "http://api.openweathermap.org/data/2.5/weather?q=Lviv,ua&lang=ua";
                    return IOUtils.toString(new URL(url).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                mTextView.setText(result);
            }
        }.execute();
    }

}
