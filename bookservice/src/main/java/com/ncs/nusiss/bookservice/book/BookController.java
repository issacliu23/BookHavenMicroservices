package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Book> publishBook(@Valid @ModelAttribute Book book, @RequestParam("image") MultipartFile file) throws IOException {
        String imageContentType = file.getContentType();
        if(imageContentType != null && (imageContentType.startsWith("image/jpg") || imageContentType.startsWith("image/jpeg") || imageContentType.startsWith("image/png"))) {
            BufferedImage image= ImageIO.read(file.getInputStream());
            if (image!=null) {//If image=null means that the upload is not an image format
                System.out.println (image.getWidth());//Get the image width,Unit px
                System.out.println (image.getHeight());//Get the image height,Unit px
            }
            book.setCoverImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            Book createdBook = bookService.createBook(book);
            if (createdBook != null)
                return new ResponseEntity<>(createdBook, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }



    // createBook
    // uploadChapters
    // updateBook
    // getAllBooks
    // getBook
    //
}
