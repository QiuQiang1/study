package com.qq.study.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.qq.study.R;


public class BinderPoolActivity extends AppCompatActivity {
    private ISecurityCenter iSecurityCenter;
    private IBookManager iBookManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        context = this;
        bindService();

    }

    private void bindService() {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.qq.aidlservice");
            intent.setAction("com.qq.aidlservice.BinderPoolService");
            bindService(intent, connection, Service.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ui线程运行
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBinderPool iBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                IBinder iBinder = iBinderPool.queryBinder(0);
                ICompute iCompute = ICompute.Stub.asInterface(iBinder);
                int add = iCompute.add(2, 2);
                Log.e("add", add + "");

                IBinder iBinder1 = iBinderPool.queryBinder(1);
                ISecurityCenter iSecurityCenter = ISecurityCenter.Stub.asInterface(iBinder1);
                String encrypt = iSecurityCenter.encrypt("来生若是缘未尽,宁负苍天不负卿");
                Log.e("encrypt", encrypt );
                String decrypt = iSecurityCenter.decrypt(encrypt);
                Log.e("decrypt", decrypt );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBookManager = null;
        }
    };


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


}
