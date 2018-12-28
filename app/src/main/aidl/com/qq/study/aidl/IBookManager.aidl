package com.qq.study.aidl;
// Declare any non-default types here with import statements
import com.qq.study.aidl.Book;
import com.qq.study.aidl.NewBookListener;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(NewBookListener newBookListener);
    void unregisterListener(NewBookListener newBookListener);
}