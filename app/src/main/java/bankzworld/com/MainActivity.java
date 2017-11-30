package bankzworld.com;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBox;
    private TextView mDisplayUrl, mDisplayResult, mErrorMessage;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBox = (EditText) findViewById(R.id.et_search_querry);
        mDisplayUrl = (TextView) findViewById(R.id.display_url_result);
        mDisplayResult = (TextView) findViewById(R.id.results);
        mErrorMessage = (TextView) findViewById(R.id.error_message);
        mPb = (ProgressBar) findViewById(R.id.pb);
    }

    void makeGithubSearchQuery() {
        String githubQuery = mSearchBox.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mDisplayUrl.setText(githubSearchUrl.toString());

        // calls the background trade
        new BackgroundTask().execute(githubSearchUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When connecting, it is best practice to make all your internet call on a background thread
     * in other not to overload or put too much work on the main thread which may lead to the application
     * crashing
     **/

    public class BackgroundTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            mPb.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                mPb.setVisibility(View.GONE);
                mDisplayResult.setText(s);
            } else {
                mPb.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}
