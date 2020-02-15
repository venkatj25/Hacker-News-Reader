package com.jv.hnreader.ui.interfaces;

import java.util.List;

public interface DataLoader<T> {
    int getSize();
    List<T> getItems(int from, int count);
}
