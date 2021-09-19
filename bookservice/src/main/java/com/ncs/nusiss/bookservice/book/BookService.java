package com.ncs.nusiss.bookservice.book;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.ncs.nusiss.bookservice.BookServiceConstants;
import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.book.chapter.ChapterRepository;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectImageDimensionsException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ncs.nusiss.bookservice.BookServiceConstants.*;

@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(BookService.class);
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations gridFsOperations;

    public Book createBook(Book book, MultipartFile coverImageFile) throws SizeLimitExceededException, IncorrectImageDimensionsException, IncorrectFileExtensionException, IllegalArgumentException {
        if(book != null && coverImageFile != null) {
            try {
                if (!COVER_IMAGE_PERMITTED_EXTENSIONS.contains(coverImageFile.getContentType()))
                    throw new IncorrectFileExtensionException(coverImageFile.getContentType(), COVER_IMAGE_PERMITTED_EXTENSIONS);

                BufferedImage image = ImageIO.read(coverImageFile.getInputStream());
                if (image.getHeight() != COVER_IMAGE_HEIGHT || image.getWidth() != COVER_IMAGE_WIDTH)
                    throw new IncorrectImageDimensionsException(image.getHeight(), image.getWidth(), COVER_IMAGE_HEIGHT, COVER_IMAGE_WIDTH);

                if (coverImageFile.getSize() > COVER_IMAGE_MAX_SIZE_IN_BYTES)
                    throw new SizeLimitExceededException("Cover image file size exceeded " + COVER_IMAGE_MAX_SIZE_IN_BYTES, coverImageFile.getSize(), COVER_IMAGE_MAX_SIZE_IN_BYTES);

                book.setCoverImage(new Binary(BsonBinarySubType.BINARY, coverImageFile.getBytes()));
                return bookRepository.insert(book);
            }
            catch (IOException e) {
                if(e instanceof SizeLimitExceededException) {
                    throw new SizeLimitExceededException("Cover image file size exceeded " + COVER_IMAGE_MAX_SIZE_IN_BYTES, coverImageFile.getSize(), COVER_IMAGE_MAX_SIZE_IN_BYTES);
                }
                logger.error(e.getMessage());
                return null;
            }
        }
        else
            throw new IllegalArgumentException();
    }

//    @Transactional
//    for transaction to work need to follow this https://stackoverflow.com/questions/51461952/mongodb-v4-0-transaction-mongoerror-transaction-numbers-are-only-allowed-on-a
    public Chapter addChapter(String bookId, Chapter chapter, MultipartFile chapterFile) throws SizeLimitExceededException, BookNotFoundException, IncorrectFileExtensionException, IllegalArgumentException {
        if(bookId!= null && chapter != null && chapterFile != null) {
            try {
                if(chapterFile.getSize() > CHAPTER_PDF_MAX_SIZE_IN_BYTES)
                    throw new SizeLimitExceededException("Chapter file size exceeded " + COVER_IMAGE_MAX_SIZE_IN_BYTES, chapterFile.getSize(), COVER_IMAGE_MAX_SIZE_IN_BYTES);
                if (!CHAPTER_FILE_PERMITTED_EXTENSIONS.contains(chapterFile.getContentType()))
                    throw new IncorrectFileExtensionException(chapterFile.getContentType(), CHAPTER_FILE_PERMITTED_EXTENSIONS);
                Optional<Book> optionalBook = bookRepository.findById(bookId);
                if (optionalBook.isPresent()) {
                    Book book = optionalBook.get();
                    if(book.getChapterList().size() > 0 && book.getChapterList().stream().anyMatch(c-> Objects.equals(c.getChapterNo(), chapter.getChapterNo()))) {
                        throw new IllegalArgumentException("Chapter number already exist in this book");
                    }
                    book.addChapter(chapter);
                    Chapter savedChapter = chapterRepository.insert(chapter);
                    bookRepository.save(book);
                    DBObject metaData = new BasicDBObject();
                    metaData.put("type", "pdf");
                    metaData.put("chapterId", savedChapter.getChapterId());
                    gridFsTemplate.store(chapterFile.getInputStream(), chapterFile.getName(), chapterFile.getContentType(), metaData);
                    return savedChapter;
                } else
                    throw new BookNotFoundException();
            } catch (IOException e) {
                if (e instanceof SizeLimitExceededException) {
                    throw new SizeLimitExceededException("Chapter file size exceeded " + COVER_IMAGE_MAX_SIZE_IN_BYTES, chapterFile.getSize(), COVER_IMAGE_MAX_SIZE_IN_BYTES);
                }
                logger.error(e.getMessage());
                return null;
            }
        }
        else
            throw new IllegalArgumentException();

    }
}
