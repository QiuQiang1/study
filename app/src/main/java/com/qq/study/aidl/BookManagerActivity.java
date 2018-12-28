package com.qq.study.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.study.BaseActivity;
import com.qq.study.R;

import java.util.List;



public class BookManagerActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "BookManagerActivity";
    private static final int ON_NEW_BOOK_ARRIVED = 10;
    private TextView showMsg;
    private static TextView showHint;

    private EditText bookName;
    private Button sendBtn;
    private Button getBtn;
    private int id = 3;
    private IBookManager iBookManager;


    //监听服务器进程是否中断
    private IBinder.DeathRecipient death = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iBookManager != null) {
                try {
                    iBookManager.asBinder().linkToDeath(death, 0);
                    iBookManager = null;
                    bindService();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        init();
    }

    private void init() {
        showMsg = findViewById(R.id.showMsg);
        showHint = findViewById(R.id.showHint);
        bookName = findViewById(R.id.bookName);
        sendBtn = findViewById(R.id.sendBtn);
        getBtn = findViewById(R.id.getBtn);
        sendBtn.setOnClickListener(this);
        getBtn.setOnClickListener(this);
        bindService();
    }

    private void bindService() {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.qq.aidlservice");
            intent.setAction("aidl.service.book");
            bindService(intent, connection, Service.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                if (!bookName.getText().toString().equals("")) {
                    addBook();
                } else {
                    Toast.makeText(this, "请输入书名", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.getBtn:
                getData(iBookManager);
                break;
        }
    }

    private void addBook() {
        Book book = new Book(id, bookName.getText().toString());
        id++;
        try {
            if (iBookManager != null) {
                iBookManager.addBook(book);
            } else {
                showHint.setText("iBookManager is null!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void getData(IBookManager manager) {
        if (manager != null) {
            showMsg.setText("");
            try {
                List<Book> bookList = manager.getBookList();
                if (bookList.size() > 0) {
                    Toast.makeText(this, "获取成功!", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0, len = bookList.size(); i < len; i++) {
                    String data = bookList.get(i).getBookId() + "  " + bookList.get(i).getBookName();
                    showMsg.setText(showMsg.getText() + data);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            showHint.setText("iBookManager is null!");
        }
    }

    //ui线程运行
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            showHint.setText("绑定成功!");
            iBookManager = IBookManager.Stub.asInterface(service);
            service.unlinkToDeath(death,0);//设置死亡代理
            try {
                iBookManager.registerListener(newBookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBookManager = null;
        }
    };

    private NewBookListener newBookListener = new NewBookListener.Stub() {

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Message msg = handler.obtainMessage();
            msg.what = ON_NEW_BOOK_ARRIVED;
            msg.obj = book.getBookName();
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new MsgHandler(this);

    private static class MsgHandler extends Handler {
        private Context context;

        public MsgHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ON_NEW_BOOK_ARRIVED:
                    showHint.setText("收到一本新书!" + msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        try {
            if (iBookManager != null && iBookManager.asBinder().isBinderAlive()) {
                iBookManager.unregisterListener(newBookListener);
                Log.e("unregisterListener", "unregisterListener   " + newBookListener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
