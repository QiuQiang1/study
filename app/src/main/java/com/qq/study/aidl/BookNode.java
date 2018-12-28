package com.qq.study.aidl;

/**
 * Created by  QiuQ on  2018-12-24.
 */

//view链表
public class BookNode {
    private Book book;
    private BookNode next;
    //head不存放数据 只是指向第一个存储数据节点的作用
    private BookNode head;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public BookNode getNext() {
        return next;
    }

    public void setNext(BookNode next) {
        this.next = next;
    }

    public BookNode getHead() {
        return head;
    }

    public void setHead(BookNode head) {
        this.head = head;
    }

    public BookNode() {
    }

    public BookNode(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


}

