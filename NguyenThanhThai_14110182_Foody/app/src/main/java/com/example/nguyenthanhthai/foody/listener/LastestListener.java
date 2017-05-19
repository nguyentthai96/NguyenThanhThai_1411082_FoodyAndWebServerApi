package com.example.nguyenthanhthai.foody.listener;

import com.example.nguyenthanhthai.foody.model.Lastest;

/**
 * Created by NguyenThanhThai on 5/18/2017.
 */

public interface LastestListener {
    void onItemClick(Lastest lastest);
    void onItemClick(Long lastestId);
}
