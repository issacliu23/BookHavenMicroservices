package com.ncs.nusiss.bookservice.book.chapter;

import com.ncs.nusiss.bookservice.book.Book;
import com.ncs.nusiss.bookservice.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class ChapterController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    @PostMapping("book/{bookId}/chapter")
    public void addChapter(@PathVariable(value = "bookId") String bookId, @RequestBody Chapter chapter) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.getChapters().add(chapter);
            chapter.setBookId(book.getBookId());
            chapterRepository.save(chapter);
            bookRepository.save(book);
        }
    }
}
