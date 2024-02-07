package com.phantomvk.democenter.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class AidlActivity extends AppCompatActivity {
    private IManager manager;
    private Connection connection = new Connection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        findViewById(R.id.buttonAddBookIn).setOnClickListener((v) -> {
            if (manager == null) return;
            Book book = new Book("Handbook", 128);
            Log.i("Client", "addBookIn start: " + book);
            try {
                manager.addBookIn(book);
            } catch (RemoteException ignore) {
            }
            Log.i("Client", "addBookIn end:   " + book);
        });

        findViewById(R.id.buttonAddBookOut).setOnClickListener((c) -> {
            if (manager == null) return;
            Book book = new Book("Handbook", 128);
            Log.i("Client", "addBookOut start: " + book);
            try {
                manager.addBookOut(book);
            } catch (RemoteException ignore) {
            }
            Log.i("Client", "addBookOut end:   " + book);
        });

        findViewById(R.id.buttonAddBookInout).setOnClickListener((v) -> {
            if (manager == null) return;
            Book book = new Book("Handbook", 128);
            Log.i("Client", "addBookInout start: " + book);
            try {
                manager.addBookInout(book);
            } catch (RemoteException ignore) {
            }
            Log.i("Client", "addBookInout end:   " + book);
        });

        Intent i = new Intent(this, ManagerService.class);
        bindService(i, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private class Connection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            manager = IManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            manager = null;
        }
    }
}