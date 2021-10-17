package com.ncs.nusiss.bookservice;

import java.util.ArrayList;
import java.util.List;

public final class BookServiceConstants {
    private BookServiceConstants(){}
    public static final String COVER_IMAGE_FILE_NAME = "image";
    public static final int COVER_IMAGE_HEIGHT = 1080;
    public static final int COVER_IMAGE_WIDTH = 1080;
    public static final long COVER_IMAGE_MAX_SIZE_IN_BYTES = 512000; //500KB
    public static final List<String> COVER_IMAGE_PERMITTED_EXTENSIONS = new ArrayList<>(List.of("image/jpeg", "image/png", "image/jpg"));

    public static final String CHAPTER_FILE_NAME = "chapterFile";
    public static final List<String> CHAPTER_FILE_PERMITTED_EXTENSIONS = new ArrayList<>(List.of("application/pdf"));
    public static final long CHAPTER_PDF_MAX_SIZE_IN_BYTES = 2097152; // 2MB
}
