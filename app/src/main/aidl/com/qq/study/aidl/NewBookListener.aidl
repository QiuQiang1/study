// NewBookListener.aidl
package com.qq.study.aidl;
// Declare any non-default types here with import statements
import com.qq.study.aidl.Book;
interface NewBookListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
 void onNewBookArrived(in Book book);
}
