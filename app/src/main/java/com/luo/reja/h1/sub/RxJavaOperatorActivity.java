package com.luo.reja.h1.sub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.luo.reja.h1.R;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class RxJavaOperatorActivity extends AppCompatActivity {
    private static final String TAG = RxJavaOperatorActivity.class.getName();

    /**
     * All the operators are categorized depending on the kind of work it do.
     * Some operators are used to Create Observables.
     * The operators like create, just, fromArray, range creates an Observable.
     * <p>
     * List of Operator Categorized
     * http://reactivex.io/documentation/operators.html#categorized
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_operator);
    }

    public void chainOperators(View v) {
        callChainOperators();
    }

    private void callChainOperators() {
        Observable<Integer> integerObservable = getIntegerObservable();
        integerObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer + " is even number";
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "All numbers emitted!");
                    }
                });
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.range(1, 20);
    }

}
