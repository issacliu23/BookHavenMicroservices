package com.ncs.nusiss.bookservice.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectImageDimensionsException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.ncs.nusiss.bookservice.MockData.*;
import static com.ncs.nusiss.bookservice.common.CommonMethods.asJsonString;
import static com.ncs.nusiss.bookservice.common.CommonMethods.getMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    //region - Publish Book API
    @Test
    public void whenPublishBookSuccessShouldReturnBookIdAnd200OK() throws Exception {
        String id = "test_id";
        Book request = getMockBook();
        Book response = getMockBook();
        response.setBookId(id);
        when(bookService.createBook(any(),any())).thenReturn(response);

        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(id));
    }
    @Test
    public void whenPublishBookReturnNullShouldReturn500ServerError() throws Exception {
        Book request = getMockBook();
        when(bookService.createBook(any(),any())).thenReturn(null);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void whenPublishBookWithMissingFieldsShouldReturn400BadRequest() throws Exception {
        Book request = getMockBook();
        request.setBookTitle(null);
        request.setSummary(null);
        when(bookService.createBook(any(),any())).thenReturn(null);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPublishBookAndServiceReturnIncorrectFileExtensionExceptionShouldReturn400BadRequest() throws Exception {
        Book request = getMockBook();
        when(bookService.createBook(any(),any())).thenThrow(IncorrectFileExtensionException.class);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPublishBookAndServiceReturnIncorrectImageDimensionsExceptionShouldReturn400BadRequest() throws Exception {
        Book request = getMockBook();
        when(bookService.createBook(any(),any())).thenThrow(IncorrectImageDimensionsException.class);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPublishBookAndServiceReturnSizeLimitExceededExceptionShouldReturn400BadRequest() throws Exception {
        Book request = getMockBook();
        when(bookService.createBook(any(),any())).thenThrow(SizeLimitExceededException.class);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }
    //endregion
    //region - Upload Chapter API
    @Test
    public void whenUploadChapterSuccessfulShouldReturnChapterIdAnd200OK() throws Exception {
        String mockBookId = "test_bookId";
        String mockChapterId = "test_chapterId";

        Book mockBook = getMockBook();
        MockMultipartFile mockChapterFile = getTestPdfFile("chapterFile");
        mockBook.setBookId(mockBookId);
        Chapter request = getMockChapter();
        Chapter response = getMockChapter();
        response.setBook(mockBook);
        response.setChapterId(mockChapterId);

        when(bookService.addChapter(mockBookId, request, mockChapterFile)).thenReturn(response);
        mockMvc.perform(multipart("/book/{bookId}/chapter",mockBookId)
                        .file(mockChapterFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("chapterTitle", request.getChapterTitle())
                        .param("chapterNo", request.getChapterNo().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chapterId").value(mockChapterId));

    }
    @Test
    public void whenUploadChapterWithNullValueShouldReturn400BadRequest() throws Exception {
        String mockBookId = "test_bookId";
        MockMultipartFile mockChapterFile = getTestPdfFile("chapterFile");
        Chapter request = getMockChapter();
        request.setChapterNo(null);
        request.setChapterId(null);


        when(bookService.addChapter(mockBookId, request, mockChapterFile)).thenReturn(null);
        mockMvc.perform(multipart("/book/{bookId}/chapter",mockBookId)
                        .file(mockChapterFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("chapterTitle", request.getChapterTitle())
                        .param("chapterNo", ""))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenUploadChapterServiceReturnNullShouldReturn500ServerError() throws Exception {
        String mockBookId = "test_bookId";
        String mockChapterId = "test_chapterId";

        Book mockBook = getMockBook();
        MockMultipartFile mockChapterFile = getTestPdfFile("chapterFile");
        mockBook.setBookId(mockBookId);
        Chapter request = getMockChapter();
        Chapter response = getMockChapter();
        response.setBook(mockBook);
        response.setChapterId(mockChapterId);

        when(bookService.addChapter(mockBookId, request, mockChapterFile)).thenReturn(null);
        mockMvc.perform(multipart("/book/{bookId}/chapter",mockBookId)
                        .file(mockChapterFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("chapterTitle", request.getChapterTitle())
                        .param("chapterNo", request.getChapterNo().toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void whenUploadChapterServiceThrowSizeLimitExceededExceptionShouldReturn400BadRequest() throws Exception {
        String mockBookId = "test_bookId";
        String mockChapterId = "test_chapterId";

        Book mockBook = getMockBook();
        MockMultipartFile mockChapterFile = getTestPdfFile("chapterFile");
        mockBook.setBookId(mockBookId);
        Chapter request = getMockChapter();
        Chapter response = getMockChapter();
        response.setBook(mockBook);
        response.setChapterId(mockChapterId);

        when(bookService.addChapter(mockBookId, request, mockChapterFile)).thenThrow(SizeLimitExceededException.class);
        mockMvc.perform(multipart("/book/{bookId}/chapter",mockBookId)
                        .file(mockChapterFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("chapterTitle", request.getChapterTitle())
                        .param("chapterNo", request.getChapterNo().toString()))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenUploadChapterServiceThrowBookNotFoundExceptionShouldReturn400BadRequest() throws Exception {
        String mockBookId = "test_bookId";
        String mockChapterId = "test_chapterId";

        Book mockBook = getMockBook();
        MockMultipartFile mockChapterFile = getTestPdfFile("chapterFile");
        mockBook.setBookId(mockBookId);
        Chapter request = getMockChapter();
        Chapter response = getMockChapter();
        response.setBook(mockBook);
        response.setChapterId(mockChapterId);

        when(bookService.addChapter(mockBookId, request, mockChapterFile)).thenThrow(BookNotFoundException.class);
        mockMvc.perform(multipart("/book/{bookId}/chapter",mockBookId)
                        .file(mockChapterFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("chapterTitle", request.getChapterTitle())
                        .param("chapterNo", request.getChapterNo().toString()))
                .andExpect(status().isBadRequest());
    }
    //endregion
    //region - Update Book API
    @Test
    public void whenUpdateBookSuccessShouldReturn200OK() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenReturn(true);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenUpdateBookAndServiceReturnFalseShouldReturn500InternalServerError() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenReturn(false);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void whenUpdateBookWithMissingFieldsShouldReturn400BadRequest() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenReturn(true);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateBookAndServiceThrowBookNotFoundExceptionShouldReturn400BadRequest() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenThrow(BookNotFoundException.class);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenUpdateBookAndServiceThrowSizeLimitExceededExceptionShouldReturn400BadRequest() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenThrow(SizeLimitExceededException.class);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenUpdateBookAndServiceThrowIncorrectFileExtensionExceptionShouldReturn400BadRequest() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenThrow(IncorrectFileExtensionException.class);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void whenUpdateBookAndServiceThrowIncorrectImageDimensionsExceptionShouldReturn400BadRequest() throws Exception {
        String mockBookId = "bookId";
        Book request = getMockBook();
        when(bookService.updateBook(any(), any(), any())).thenThrow(IncorrectImageDimensionsException.class);
        mockMvc.perform(multipart("/book/{bookId}",mockBookId)
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("bookTitle", request.getBookTitle())
                        .param("summary", request.getSummary())
                        .param("pointsRequiredForChapter", request.getPointsRequiredForChapter().toString())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }
    //endregion

    //region - GetAllBooks
    @Test
    public void whenGetAllBooksSuccessShouldReturnListAnd200OK() throws Exception {
        List<BookDTO> responseList = getListOfMockBookDTOs();
        when(bookService.getBooks(null)).thenReturn(responseList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDTO> returnedBooks = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<BookDTO>>(){});
        assertEquals(returnedBooks.size(), responseList.size());
    }
    @Test
    public void whenGetAllBooksWithCriteriaShouldReturnListAnd200OK() throws Exception {
        String bookTitle = "ABC";
        BookCriteria request = new BookCriteria();
        request.setBookTitle(bookTitle);
        List<BookDTO> responseList = getListOfMockBookDTOs();
        getListOfMockBookDTOs().forEach(b -> b.setBookTitle(bookTitle));
        when(bookService.getBooks(request)).thenReturn(responseList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDTO> returnedBooks = getMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<BookDTO>>(){});
        assertEquals(returnedBooks.size(), responseList.size());
    }

    //endregion
}
