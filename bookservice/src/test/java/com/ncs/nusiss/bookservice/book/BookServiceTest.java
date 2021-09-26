package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.book.chapter.ChapterRepository;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectImageDimensionsException;
import com.querydsl.core.types.Predicate;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.ncs.nusiss.bookservice.BookServiceConstants.CHAPTER_FILE_NAME;
import static com.ncs.nusiss.bookservice.BookServiceConstants.COVER_IMAGE_FILE_NAME;
import static com.ncs.nusiss.bookservice.MockData.*;
import static com.ncs.nusiss.bookservice.MockData.getMockImage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private ChapterRepository chapterRepository;

    //region - Create Book Method
    @Test
    public void whenCreateBookShouldReturnBookWithId() throws Exception {
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
    public void whenCreateBookWithInvalidImageShouldThrowExceptions() {
        String id = "test_id";
        Book request = getMockBook();
        Book mockResponse = getMockBook();
        mockResponse.setBookId(id);
        when(bookRepository.insert(any(Book.class))).thenReturn(mockResponse);
        assertThrows(IncorrectFileExtensionException.class, () -> {
            bookService.createBook(request, getTestPdfFile(COVER_IMAGE_FILE_NAME));
        });
        assertThrows(IncorrectImageDimensionsException.class, () -> {
            bookService.createBook(request, getIncorrectHeightAndWidthImage());
        });
        assertThrows(SizeLimitExceededException.class, () -> {
            bookService.createBook(request, getExceedLimitSizeImage());
        });
    }
    //endregion
    //region - Add Chapter Method
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
    //endregion
    //region - Update Book Method
    @Test
    public void whenUpdateBookSuccessfulShouldReturnTrue() throws Exception {
        String bookId = "bookId";
        Binary coverImage = new Binary(BsonBinarySubType.BINARY, getMockImage().getBytes());
        Book request = getMockBook();
        request.setBookId(bookId);
        request.setCoverImage(coverImage);

        Book mockResponse = getMockBook();
        mockResponse.setBookId(bookId);
        mockResponse.setCoverImage(coverImage);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockResponse));
        when(bookRepository.save(any(Book.class))).thenReturn(mockResponse);
        Boolean isUpdated = bookService.updateBook(bookId, request, getMockImage());
        verify(bookRepository,times(1)).save(any());
        assertTrue(isUpdated);
        assertEquals(request.getBookTitle(), mockResponse.getBookTitle());
        assertEquals(request.getSummary(), mockResponse.getSummary());
        assertEquals(request.getAuthorId(), mockResponse.getAuthorId());
        assertTrue(request.getGenreList().equals(mockResponse.getGenreList()));
    }
    @Test
    public void whenUpdateBookWithNullShouldThrowIllegalArgumentException() throws Exception{
        String bookId = "bookId";
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(bookId, null, getMockImage());
        });
    }
    @Test
    public void whenUpdateBookWithInvalidImageShouldThrowExceptions() throws IOException {
        String bookId = "bookId";
        Book request = getMockBook();

        Book mockResponse = getMockBook();
        mockResponse.setBookId(bookId);
        mockResponse.setCoverImage(new Binary(BsonBinarySubType.BINARY,getMockImage().getBytes()));
        when(bookRepository.findById(any())).thenReturn(Optional.of(mockResponse));
        assertThrows(IncorrectFileExtensionException.class, () -> {
            bookService.updateBook(bookId, request, getTestPdfFile(COVER_IMAGE_FILE_NAME));
        });
        assertThrows(SizeLimitExceededException.class, () -> {
            bookService.updateBook(bookId, request, getExceedLimitSizeImage());
        });
        assertThrows(IncorrectImageDimensionsException.class, () -> {
            bookService.updateBook(bookId, request, getIncorrectHeightAndWidthImage());
        });
    }
    //endregion
    //region - Get All Books Method
    @Test
    public void whenGetBooksWithNoCriteriaShouldReturnList() {
        List<Book> mockResponse = getListOfMockBooks();
        when(bookRepository.findAll(any(Predicate.class))).thenReturn(mockResponse);
        List<BookDTO> books = bookService.getBooks(null);
        assertEquals(mockResponse.size(), books.size());
        for(int i = 0; i< books.size(); i++) {
            assertEquals(mockResponse.get(i).getBookTitle(),books.get(i).getBookTitle());
            assertEquals(mockResponse.get(i).getSummary(),books.get(i).getSummary());
            assertEquals(mockResponse.get(i).getPointsRequiredForChapter(),books.get(i).getPointsRequiredForChapter());
            assertEquals(mockResponse.get(i).getGenreList(),books.get(i).getGenreList());
            assertEquals(mockResponse.get(i).getAuthorName(),books.get(i).getAuthorName());
        }
    }
    @Test
    public void whenGetBooksWithCriteriaShouldReturnList() {
        BookCriteria criteria = new BookCriteria();
        String bookTitle = "ABC";
        criteria.setBookTitle(bookTitle);
        List<Book> mockResponse = getListOfMockBooks();
        mockResponse.forEach(b -> b.setBookTitle(bookTitle));
        when(bookRepository.findAll(any(Predicate.class))).thenReturn(mockResponse);
        List<BookDTO> books = bookService.getBooks(criteria);
        assertEquals(mockResponse.size(), books.size());
        for(int i = 0; i< books.size(); i++) {
            assertEquals(mockResponse.get(i).getBookTitle(),books.get(i).getBookTitle());
            assertEquals(mockResponse.get(i).getSummary(),books.get(i).getSummary());
            assertEquals(mockResponse.get(i).getPointsRequiredForChapter(),books.get(i).getPointsRequiredForChapter());
            assertEquals(mockResponse.get(i).getGenreList(),books.get(i).getGenreList());
            assertEquals(mockResponse.get(i).getAuthorName(),books.get(i).getAuthorName());
        }
    }

}
