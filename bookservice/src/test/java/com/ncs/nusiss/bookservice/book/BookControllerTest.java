package com.ncs.nusiss.bookservice.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.ncs.nusiss.bookservice.MockData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    // whenPublishBookSuccessShouldReturn200OK
    @Test
    public void whenPublishBookSuccessShouldReturnBookIdAnd200OK() throws Exception {
        String id = "test_id";
        Book request = getMockBook();
        Book response = getMockBook();
        response.setBookId(id);
        when(bookService.createBook(any())).thenReturn(response);

        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", request.getTitle())
                        .param("summary", request.getSummary())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(id));

    }
    @Test
    public void whenPublishBookReturnNullShouldReturn500ServerError() throws Exception {
        Book request = getMockBook();
        when(bookService.createBook(any())).thenReturn(null);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", request.getTitle())
                        .param("summary", request.getSummary())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void whenPublishBookWithMissingFieldsShouldReturn400BadRequest() throws Exception {
        Book request = getMockBook();
        request.setTitle(null);
        request.setSummary(null);
        when(bookService.createBook(any())).thenReturn(null);
        mockMvc.perform(multipart("/book")
                        .file(getMockImage())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", request.getTitle())
                        .param("summary", request.getSummary())
                        .param("genreList", request.getGenreList().stream().map(Genre::name).toArray(String[]::new)))
                .andExpect(status().isBadRequest());
    }
}
