package com.example.myapplication.glidetest;


import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.bumptech.glide.load.Key;

public class StringSignature implements Key {
    private final String id;
    private final MessageDigest md;

    public StringSignature(String id) {
        this.id = id;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        md.update(id.getBytes(CHARSET));
        byte[] digest = md.digest();
        messageDigest.update(digest);
    }

    @NonNull
    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringSignature) {
            StringSignature other = (StringSignature) o;
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}