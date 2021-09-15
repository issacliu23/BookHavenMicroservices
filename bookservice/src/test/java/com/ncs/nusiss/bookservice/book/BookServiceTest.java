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
            bookService.createBook(request, getTestPdfFile("image"));
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
    public void whenAddChapterSuccessShouldReturnChapterWithId() throws BookNotFoundException, IOException {
        String bookId = "bookId", chapterId ="chapterId";
        Book mockBook = getMockBook();
        mockBook.setBookId(bookId);
        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));

        Chapter request = getMockChapter();
        mockBook.addChapter(request);
        Chapter response = getMockChapter();

        response.setChapterId(chapterId);
        when(chapterRepository.insert(any(Chapter.class))).thenReturn(response);
        when(bookRepository.save(any())).thenReturn(mockBook);

        Chapter returnedChapter = bookService.addChapter(bookId, request, getTestPdfFile("chapterFile"));
        assertEquals(request.getChapterNo(), returnedChapter.getChapterNo());
        assertEquals(request.getChapterTitle(), returnedChapter.getChapterTitle());
        assertEquals(chapterId, returnedChapter.getChapterId());

    }
    // whenAddChapterAndBookNotFoundShouldThrowBookNotFoundException
    // whenAddChapterAndPdfFileExceedSizeLimitShouldThrowSizeLimitException
    // whenAddChapterWithNullValuesShouldThrowIllegalArgumentException


}
