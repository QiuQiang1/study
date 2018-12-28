package com.qq.study.aidl;

import android.util.Log;

/**
 * Created by  QiuQ on  2018-12-24.
 */
public class BookNodeList {
    private BookNode data;
    private BookNode next;
    //head不存放数据 只是指向第一个存储数据节点的作用
    private BookNode head;
    private int mSize = 0;

    public BookNodeList(BookNode data) {
        this.data = data;
        this.data.setIndex(mSize);
    }

    public BookNodeList() {
    }

    public void addNode(BookNode viewNode) {
        if (viewNode != null) {
            if (head == null) {
                head = new BookNode();
            }
            BookNode temp = head;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(viewNode);
            mSize++;
        }
    }

    public BookNode getNode(int index) {
        if (index < mSize) {
            BookNode temp = head;
            int count = 0;
            if (head != null) {
                while (temp.getNext() != null) {
                    if (count < mSize) {
                        temp = temp.getNext();
                        if (count == index) {
                            return temp;
                        }
                        count++;
                    }
                }
            }
        }
        return null;
    }

    //在index之后插入
    public void insertNodeByIndex(int index, BookNode node) {
        if (index < mSize && node != null) {
            if (head == null) {
                head = new BookNode();
            }
            BookNode temp = head;
            while (temp.getNext() != null) {
                if (temp.getIndex() == index) {
                    temp.setNext(node);
                    mSize++;
                    break;
                }
                temp = temp.getNext();
            }
        }
    }


    public int getSize() {
        return mSize;
    }
}
