package com.ncs.nusiss.bookservice;

import com.ncs.nusiss.bookservice.book.Book;
import com.ncs.nusiss.bookservice.book.Genre;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static Book getMockBook() throws IOException {
        Book book = new Book();
        book.setTitle("test title");
        book.setSummary("test summary");
        book.setGenreList(List.of(Genre.Comedy,Genre.Fantasy));
        book.setAuthorId("authorId");
        return book;
    }

    public static MockMultipartFile getMockImage() throws IOException {
        return new MockMultipartFile("image","coverimage.jpg", MediaType.IMAGE_JPEG_VALUE, new FileInputStream(new File("src/test/resources/images/coverimage.jpg")));
    }

    public static MockMultipartFile getWrongSizeImage() throws IOException {
        byte[] bytes = new byte[1024 * 1024 * 10];
        return new MockMultipartFile("image", "file1.jpg", "image/jpg", bytes);
    }
}
