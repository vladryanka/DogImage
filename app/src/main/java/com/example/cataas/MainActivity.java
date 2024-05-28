package com.example.cataas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;

    private Button buttonLoadImage;
    private ProgressBar progressBar;
    private ImageView imageViewDog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.loadDogImage();
        mainViewModel.getError().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isError) {
                        if (isError)
                        Toast.makeText(MainActivity.this,
                                R.string.internet_connection_error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        mainViewModel.getimageIsLoaded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loaded) {
                if (!loaded)
                    progressBar.setVisibility(View.VISIBLE);
                else progressBar.setVisibility(View.GONE);
            }
        });
        mainViewModel.getDogImage().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                Glide.with(MainActivity.this)
                        .load(dogImage.getMessage())
                        .into(imageViewDog);
            }
        });
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.loadDogImage();
            }
        });

    }


    private void initViews(){
        buttonLoadImage = findViewById(R.id.buttonLoadImage);
        progressBar = findViewById(R.id.progressBar);
        imageViewDog = findViewById(R.id.imageViewDog);
    }

}