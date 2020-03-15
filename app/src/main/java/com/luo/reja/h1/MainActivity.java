package com.luo.reja.h1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.luo.reja.h1.sub.CompositeDisposableActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    /**
     * Disposable: Disposable is used to dispose the subscription when an Observer no longer wants to listen to Observable.
     * In android disposable are very useful in avoiding memory leaks.
     *
     * Let’s say you are making a long running network call and updating the UI.
     * By the time network call completes its work, if the activity / fragment is already destroyed,
     * as the Observer subscription is still alive, it tries to update already destroyed activity.
     * In this case it can throw a memory leak. So using the Disposables, the un-subscription can be when the activity is destroyed.
     */
    private Disposable disposable;

    /**
     * Filter Operator
     * filter() operator filters the data by applying a conditional statement. The data which meets the condition will be emitted and the remaining will be ignored.
     *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void basicObservable(View v) {
        callBasicObservable();
    }

    public void goToCompositeDisposable(View v) {
        Intent intent = new Intent(this, CompositeDisposableActivity.class);
        startActivity(intent);
    }

    private void callBasicObservable() {
        Observable<String> animalObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
        /**
         *
         onSubscribe(): Method will be called when an Observer subscribes to Observable.
         onNext(): This method will be called when Observable starts emitting the data.
         onError(): In case of any error, onError() method will be called.
         onComplete(): When an Observable completes the emission of all the items, onComplete() will be called.


         subscribeOn(Schedulers.io()): This tell the Observable to run the task on a background thread.
         observeOn(AndroidSchedulers.mainThread()): This tells the Observer to receive the data on android UI thread so that you can take any UI related actions.

         */

        Observer<String> animalObserve = getAnimalObserver();
        animalObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribe(getAnimalObserver());

    }

    private Observer<String> getAnimalObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "all items are emitted");
            }
        };
    }

    /**
     * Disposable
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
