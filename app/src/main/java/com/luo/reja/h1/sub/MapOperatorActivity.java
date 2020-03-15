package com.luo.reja.h1.sub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.luo.reja.h1.R;
import com.luo.reja.h1.model.Note;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MapOperatorActivity extends AppCompatActivity {
    private static final String TAG = MapOperatorActivity.class.getName();

    /**
     * Basic Observable, Observer, Subscriber example
     * Introduced CompositeDisposable and DisposableObserver
     * The observable emits custom data type (Note) instead of primitive data types
     * ----
     * .map() operator is used to turn the note into all uppercase letters
     * ----
     * You can also notice we got rid of the below declarations
     * Observable<Note> notesObservable = getNotesObservable();
     * DisposableObserver<Note> notesObserver = getNotesObserver();
     */

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_operator);
    }

    public void mapOperator(View v) {
        callMapOperator();
    }

    public void goToRxJavaOperator(View v) {
        Intent intent = new Intent(this, RxJavaOperatorActivity.class);
        startActivity(intent);
    }

    private void callMapOperator() {
        compositeDisposable.add(
            getNoteObservable().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(new Function<Note, Note>() {
                @Override
                public Note apply(Note note) throws Exception {
                    note.setNote(note.getNote().toUpperCase());
                    return note;
                }
            })
            .subscribeWith(getNotesObserver())
        );
    }

    private List<Note> prepareNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "buy tooth paste!"));
        notes.add(new Note(2, "call brother!"));
        notes.add(new Note(3, "watch narcos tonight!"));
        notes.add(new Note(4, "pay power bill!"));
        return notes;
    }

    private Observable<Note> getNoteObservable() {
        final List<Note> notes = new ArrayList<>();
        notes.addAll(prepareNotes());
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note n : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(n);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private DisposableObserver<Note> getNotesObserver() {
        return new DisposableObserver<Note>() {
            @Override
            public void onNext(Note note) {
                Log.d(TAG, "Note: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All notes are emitted!");
            }
        };
    }
}
