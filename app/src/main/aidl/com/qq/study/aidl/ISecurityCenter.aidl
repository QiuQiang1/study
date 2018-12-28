// ISecurityCenter.aidl
package com.qq.study.aidl;
import java.lang.String;
// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
