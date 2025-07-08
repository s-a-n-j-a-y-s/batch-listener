package com.zenith.batchlistener.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.MessageFormat;

/**
 * Created by Sanjay Kumar Senthilvel on 07-07-2025.
 */
public class Pair<L, R> {
    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[{0}:{1}]", left, right);
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }
}
