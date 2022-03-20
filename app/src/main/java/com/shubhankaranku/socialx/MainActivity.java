package com.shubhankaranku.socialx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shubhankaranku.socialx.Adapter.customAdapter;
import com.shubhankaranku.socialx.Models.NewsApiResponse;
import com.shubhankaranku.socialx.Models.NewsHeadlines;

import java.util.List;



public class MainActivity extends AppCompatActivity implements SelectListener{

    RecyclerView recyclerView;
    customAdapter adapter;
    ProgressDialog dialog;
    SearchView search_view;
    ImageView menuIcon;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        search_view = findViewById(R.id.search_view);
        menuIcon = findViewById(R.id.options_menu);
        mAuth = FirebaseAuth.getInstance();

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of " + query);
                dialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener, "general", query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuIcon);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_sign_out:
                                if (LoginManager.getInstance() != null){
                                    mAuth.signOut();
                                    LoginManager.getInstance().logOut();
                                    finishAffinity();
                                    openLogin();
                                }else {
                                    mAuth.signOut();
                                    openLogin();
                                }


                                return true;

                            default:
                                return false;
                        }
                    }
                });
                // Showing the popup menu
                popupMenu.show();

            }
        });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.setMessage("Fetching news articles...");
        dialog.setCancelable(true);
        dialog.show();

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, "general", null);
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if (list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
            else {
                showNews(list);
                dialog.dismiss();
            }

        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "An Error Occurred!!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recyclerMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new customAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        CustomTabsIntent.Builder builder =  new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(headlines.getUrl()));
    }

    @Override
    public void onMenuItemClick(MenuItem item) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            openLogin();
        }
    }

    private void openLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}