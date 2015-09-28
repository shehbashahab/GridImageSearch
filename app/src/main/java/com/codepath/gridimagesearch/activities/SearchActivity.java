package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.helpers.EndlessScrollListener;
import com.codepath.gridimagesearch.helpers.NetworkUtil;
import com.codepath.gridimagesearch.helpers.Toaster;
import com.codepath.gridimagesearch.models.ImageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    final static private Double PROTOCOL_VERSION_NUMBER = 1.0;
    final static private int PAGE_SIZE = 8;
    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private String sizeFilter = "";
    private String colorFilter = "";
    private String typeFilter = "";
    private String siteFilter = "";
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toaster.init(this);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
    }

    private void setupViews() {
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                sizeFilter = data.getStringExtra("sizeValue");
                colorFilter = data.getStringExtra("colorValue");
                typeFilter = data.getStringExtra("imageTypeValue");
                siteFilter = data.getStringExtra("siteValue");
            }
        }
    }

    private void customLoadMoreDataFromApi(int offset) {
        loadSearch((offset - 1) * PAGE_SIZE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    query = queryText;
                    clearResults();
                    loadSearch(0);
                    return true;
                } else {
                    Toaster.toast("No Internet Connection");
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), AdvancedSearchActivity.class);
            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearResults() {
        aImageResults.clear();
    }

    private String applySearchFilters() {
        String optionalParms = "";

        if (!sizeFilter.isEmpty()) {
            optionalParms = optionalParms.concat("&imgsz=" + sizeFilter);
        } if (!colorFilter.isEmpty()) {
            optionalParms = optionalParms.concat("&imgcolor=" + colorFilter);
        } if (!typeFilter.isEmpty()) {
            optionalParms = optionalParms.concat("&imgtype=" + typeFilter);
        }  if (!siteFilter.isEmpty()) {
            optionalParms = optionalParms.concat("&imgtype=" + siteFilter);
        }

        Log.d("DEBUG", "Optional Parms: " + optionalParms);
        return optionalParms;
    }

    private String queryBuilder(int offset) {
        String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images";
        return baseUrl + "?v=" + PROTOCOL_VERSION_NUMBER + "&q=" + query + "&rsz=" + PAGE_SIZE + "&start=" + offset + applySearchFilters();
    }

    private void loadSearch(int offset) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(queryBuilder(offset), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }

        });
    }
}