package com.ncs.nusiss.bookservice;

import com.ncs.nusiss.bookservice.book.Book;
import com.ncs.nusiss.bookservice.book.Genre;
import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.ncs.nusiss.bookservice.BookServiceConstants.COVER_IMAGE_FILE_NAME;

public class MockData {
    public static Book getMockBook() {
        Book book = new Book();
        book.setBookTitle("test title");
        book.setSummary("test summary");
        book.setPointsRequiredForChapter(0);
        book.setGenreList(List.of(Genre.Comedy,Genre.Fantasy));
        book.setAuthorId("authorId");
        return book;
    }

    public static Chapter getMockChapter() {
        Chapter chapter = new Chapter();
        chapter.setChapterNo(1);
        chapter.setChapterTitle("test chapter title");
        return chapter;
    }

    public static MockMultipartFile getMockImage() throws IOException {
        return new MockMultipartFile(COVER_IMAGE_FILE_NAME,"coverimage.jpg", MediaType.IMAGE_JPEG_VALUE, new FileInputStream(new File("src/test/resources/files/coverimage.jpg")));
    }

    public static MockMultipartFile getExceedLimitSizeImage() throws IOException {
        return new MockMultipartFile(COVER_IMAGE_FILE_NAME,"morethan500kb.png", MediaType.IMAGE_PNG_VALUE, new FileInputStream(new File("src/test/resources/files/morethan500kb.png")));
    }

    public static MockMultipartFile getIncorrectHeightAndWidthImage() throws IOException {
        return new MockMultipartFile(COVER_IMAGE_FILE_NAME,"not1080x1080.jpg", MediaType.IMAGE_JPEG_VALUE, new FileInputStream(new File("src/test/resources/files/not1080x1080.jpg")));
    }

    public static MockMultipartFile getTestPdfFile(String name) throws IOException {
        return new MockMultipartFile(name,"test.pdf", MediaType.APPLICATION_PDF_VALUE, new FileInputStream(new File("src/test/resources/files/test.pdf")));
    }

    public static MockMultipartFile getMoreThan2MBPdfFile(String name) throws IOException {
        return new MockMultipartFile(name,"moreThan2MB.pdf", MediaType.APPLICATION_PDF_VALUE, new FileInputStream(new File("src/test/resources/files/moreThan2MB.pdf")));
    }
}
