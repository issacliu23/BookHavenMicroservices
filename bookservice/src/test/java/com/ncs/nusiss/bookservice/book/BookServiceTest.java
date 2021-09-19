package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.book.chapter.ChapterRepository;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectImageDimensionsException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Optional;

import static com.ncs.nusiss.bookservice.BookServiceConstants.CHAPTER_FILE_NAME;
import static com.ncs.nusiss.bookservice.BookServiceConstants.COVER_IMAGE_FILE_NAME;
import static com.ncs.nusiss.bookservice.MockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private ChapterRepository chapterRepository;

    @Test
    public void whenCreateBookShouldReturnBookWithId() throws IOException, IncorrectImageDimensionsException, IncorrectFileExtensionException {
        String id = "test_id";
        Book request = getMockBook();
        Book mockResponse = getMockBook();
        mockResponse.setBookId(id);
        when(bookRepository.insert(any(Book.class))).thenReturn(mockResponse);
        Book actualResponse = bookService.createBook(request, getMockImage());
        assertEquals(id, actualResponse.getBookId());
        assertEquals(request.getBookTitle(), actualResponse.getBookTitle());
        assertEquals(request.getSummary(), actualResponse.getSummary());
        assertEquals(request.getAuthorId(), actualResponse.getAuthorId());
        assertEquals(request.getGenreList().size(), actualResponse.getGenreList().size());
    }

    @Test
    public void whenCreateBookWithNullShouldThrowIllegalArgumentException() {
        String id = "test_id";
        Book mockResponse = getMockBook();
        mockResponse.setBookId(id);
        when(bookRepository.insert(any(Book.class))).thenReturn(mockResponse);
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(null, null);
        });
    }

    @Test
    public void whenCreateBookWithWrongExtensionImageShouldThrowIncorrectFileExtensionException() {
        String id = "test_id";
        Book request = getMockBook();
        Book mockResponse = getMockBook();
        mockResponse.setBookId(id);
        when(bookRepository.insert(any(Book.class))).thenReturn(mockResponse);
        assertThrows(IncorrectFileExtensionException.class, () -> {
            bookService.createBook(request, getTestPdfFile(COVER_IMAGE_FILE_NAME));
        });
    }

    @Test
    public void whenCreateBookWithWrongDimensionImageShouldThrowIncorrectImageDimensionsException() {
        String id = "test_id";
        Book request = getMockBook();
        Book mockResponse = getMockBook();
        mockResponse.setBookId(id);
        when(bookRepository.insert(any(Book.class))).thenReturn(mockResponse);
        assertThrows(IncorrectImageDimensionsException.class, () -> {
            bookService.createBook(request, getIncorrectHeightAndWidthImage());
        });
    }
    @Test
    public void whenCreateBookWithExceedSizeImageShouldThrowSizeLimitExceededExceptionException() {
        String id = "test_id";
        Book request = getMockBook();
        Book mockResponse = getMockBook();
        mockResponse.setBookId(id);
        when(bookRepository.insert(any(Book.class))).thenReturn(mockResponse);
        assertThrows(SizeLimitExceededException.class, () -> {
            bookService.createBook(request, getExceedLimitSizeImage());
        });
    }

    @Test
    public void whenAddChapterSuccessShouldReturnChapterWithId() throws BookNotFoundException, IOException, IncorrectFileExtensionException {
        String bookId = "bookId", chapterId ="chapterId";
        Chapter request = getMockChapter();

        Book mockBook = getMockBook();
        mockBook.setBookId(bookId);
        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));

        Chapter response = getMockChapter();

        response.setChapterId(chapterId);
        when(chapterRepository.insert(any(Chapter.class))).thenReturn(response);
        when(bookRepository.save(any())).thenReturn(mockBook);

        Chapter returnedChapter = bookService.addChapter(bookId, request, getTestPdfFile(CHAPTER_FILE_NAME));
        assertEquals(request.getChapterNo(), returnedChapter.getChapterNo());
        assertEquals(request.getChapterTitle(), returnedChapter.getChapterTitle());
        assertEquals(chapterId, returnedChapter.getChapterId());

    }
    @Test
    public void whenAddChapterAndBookNotFoundShouldThrowBookNotFoundException() {
        String bookId = "bookId";
        Chapter request = getMockChapter();
        when(bookRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> {
            bookService.addChapter(bookId, request, getTestPdfFile(CHAPTER_FILE_NAME));
        });
    }

    @Test
    public void whenAddChapterAndPdfFileExceedSizeLimitShouldThrowSizeLimitException() {
        String bookId = "bookId";
        Chapter request = getMockChapter();
        when(bookRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(SizeLimitExceededException.class, () -> {
            bookService.addChapter(bookId, request, getMoreThan2MBPdfFile(CHAPTER_FILE_NAME));
        });
    }

    @Test
    public void whenAddChapterWithNullValuesShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.addChapter(null, null, null);
        });
    }

    @Test
    public void whenAddChapterWithExistedChapterNoShouldThrowIllegalArgumentException() throws BookNotFoundException, IOException, IncorrectFileExtensionException {
        String bookId = "bookId", chapterId ="chapterId";
        Book mockBook = getMockBook();
        mockBook.setBookId(bookId);
        Chapter existedChapter = getMockChapter();
        existedChapter.setChapterId("existedId");
        mockBook.addChapter(existedChapter);
        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));

        Chapter request = getMockChapter();
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.addChapter(bookId, request, getTestPdfFile(CHAPTER_FILE_NAME));
        });
    }

    @Test
    public void whenAddChapterWithWrongFileFormatShouldThrowIncorrectFileExtensionException() {
        String bookId = "bookId";
        Chapter request = getMockChapter();
        assertThrows(IncorrectFileExtensionException.class, () -> {
            bookService.addChapter(bookId, request, getMockImage());
        });
    }


}
