package com.phantomvk.democenter.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.phantomvk.democenter.R;

public class AidlActivity extends AppCompatActivity implements View.OnClickListener {
    private IManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        findViewById(R.id.buttonAddBookIn).setOnClickListener(this);
        findViewById(R.id.buttonAddBookOut).setOnClickListener(this);
        findViewById(R.id.buttonAddBookInout).setOnClickListener(this);

        Intent i = new Intent(this, ManagerService.class);
        bindService(i, new Connection(), BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        if (manager == null) return;
        try {
            Book book = new Book("Handbook", 128);
            switch (view.getId()) {
                case R.id.buttonAddBookIn: {
                    Log.i("Client", "addBookIn start: " + book.toString());
                    manager.addBookIn(book);
                    Log.i("Client", "addBookIn end:   " + book.toString());
                    break;
                }

                case R.id.buttonAddBookOut: {
                    Log.i("Client", "addBookOut start: " + book.toString());
                    manager.addBookOut(book);
                    Log.i("Client", "addBookOut end:   " + book.toString());
                    break;
                }

                case R.id.buttonAddBookInout: {
                    Log.i("Client", "addBookInout start: " + book.toString());
                    manager.addBookInout(book);
                    Log.i("Client", "addBookInout end:   " + book.toString());
                }
            }
        } catch (RemoteException ignore) {
        }
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