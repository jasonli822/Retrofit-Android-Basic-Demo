package com.test.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.retrofit.api.GitHubService;
import com.test.retrofit.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String API_BASE = "https://api.github.com"; // Base URL

    private Button searchButton;
    private TextView resultTextView;
    private EditText accountEditText;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }

    private void initView() {
        searchButton = (Button) findViewById(R.id.button);
        resultTextView = (TextView) findViewById(R.id.tv_result);
        accountEditText = (EditText) findViewById(R.id.edit);
        pbar = (ProgressBar) findViewById(R.id.pb);
        pbar.setVisibility(View.INVISIBLE);
    }

    private void setListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = accountEditText.getText().toString();
                pbar.setVisibility(View.VISIBLE);

                // Create a very simple REST adapter which points the GitHub API.
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create an instance of our GitHub API interface.
                GitHubService gitHubService = retrofit.create(GitHubService.class);

                // Create a call instance for looking up User
                Call<User> call = gitHubService.getUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            User user = response.body();
                            resultTextView.setText("GitHub Name:" + user.getName() +
                                    "\nAvatar url:" + user.getAvatar_url());
                        } else {
                            resultTextView.setText("Can't find!");
                        }
                        pbar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        resultTextView.setText(t.getMessage());
                        pbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}
