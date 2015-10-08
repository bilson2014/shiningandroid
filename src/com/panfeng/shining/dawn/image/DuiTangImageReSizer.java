package com.panfeng.shining.dawn.image;

import in.srain.cube.image.impl.DefaultImageReSizer;

public class DuiTangImageReSizer extends DefaultImageReSizer {

    private static DuiTangImageReSizer sInstance;

    public static DuiTangImageReSizer getInstance() {
        if (sInstance == null) {
            sInstance = new DuiTangImageReSizer();
        }
        return sInstance;
    }
}
