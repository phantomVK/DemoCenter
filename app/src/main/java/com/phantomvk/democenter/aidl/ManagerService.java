package com.phantomvk.democenter.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ManagerService extends Service {
    private IManager.Stub mManager = new ManagerServiceImpl();
    private List<Book> mBooks = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mManager;
    }

    private class ManagerServiceImpl extends IManager.Stub {
        @Override
        public void addBookIn(Book book) {
            book.price = 1024;
            mBooks.add(book);
            Log.i("Server", "addBookIn:    " + mBooks.toString());
        }

        @Override
        public void addBookOut(Book book) {
            book.price = 1024;
            mBooks.add(book);
            Log.i("Server", "addBookOut:   " + mBooks.toString());
        }

        @Override
        public void addBookInout(Book book) {
            book.price = 1024;
            mBooks.add(book);
            Log.i("Server", "addBookInout: " + mBooks.toString());
        }
    }
}
