package com.ncs.nusiss.bookservice;

import java.util.ArrayList;
import java.util.List;

public final class BookServiceConstants {
    private BookServiceConstants(){}

    public static final int COVER_IMAGE_HEIGHT = 1080;
    public static final int COVER_IMAGE_WIDTH = 1080;
    public static final long COVER_IMAGE_MAX_SIZE_IN_BYTES = 512000; //0.5KB
    public static final List<String> COVER_IMAGE_PERMITTED_EXTENSIONS = new ArrayList<>(List.of("image/jpeg", "image/png", "image/jpg"));

    public static final int CHAPTER_PDF_MAX_SIZE_IN_BYTES = 5120000; // 5KB
}
