package com.fh.shop.api.common;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

public class Page implements Serializable {

    @TableField(exist = false)
    private int draw;
    @TableField(exist = false)
    private int start;
    @TableField(exist = false)
    private int length;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
