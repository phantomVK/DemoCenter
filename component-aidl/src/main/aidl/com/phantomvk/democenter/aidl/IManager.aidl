package com.phantomvk.democenter.aidl;
import com.phantomvk.democenter.aidl.Book;

interface IManager {
    void addBookIn(in Book book);
    void addBookOut(out Book book);
    void addBookInout(inout Book book);
}
