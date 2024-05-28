package com.example.cataas;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status";
    private static final String TAG = "Doing";
    private MutableLiveData<DogImage> dogImage = new MutableLiveData<>();
    private MutableLiveData<Boolean> imageIsLoaded = new MutableLiveData<>();
    private MutableLiveData<Boolean> error = new MutableLiveData<>();

    public LiveData<Boolean> getError() {
        return error;
    }

    public LiveData<DogImage> getDogImage() {
        return dogImage;
    }
    public LiveData<Boolean> getimageIsLoaded() {
        return imageIsLoaded;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadDogImage() {

        Disposable disposable = loadDogImageRx().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        imageIsLoaded.setValue(false);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        imageIsLoaded.setValue(true);
                    }
                })
                .subscribe(new Consumer<DogImage>() {
                               @Override
                               public void accept(DogImage image) throws Throwable {
                                   error.setValue(false);
                                   dogImage.setValue(image);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                error.setValue(true);
                                Log.d(TAG,"loadDogImage");
                            }
                        });
        compositeDisposable.add(disposable);
    }

    private Single<DogImage> loadDogImageRx() {
        return ApiFactory.getApiService().getDogImage();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
