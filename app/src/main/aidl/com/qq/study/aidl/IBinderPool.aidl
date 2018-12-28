// IBinderPool.aidl
package com.qq.study.aidl;
import android.os.Binder;
// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
