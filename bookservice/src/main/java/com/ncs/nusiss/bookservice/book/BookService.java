package com.ncs.nusiss.bookservice.book;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.book.chapter.ChapterRepository;
import com.ncs.nusiss.bookservice.book.chapterAccess.ChapterAccess;
import com.ncs.nusiss.bookservice.book.chapterAccess.ChapterAccessService;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectImageDimensionsException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import com.ncs.nusiss.bookservice.securityconfig.JwtUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ncs.nusiss.bookservice.BookServiceConstants.*;

@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterAccessService chapterAccessService;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations gridFsOperations;

    public Book createBook(Book book, MultipartFile coverImageFile) throws SizeLimitExceededException, IncorrectImageDimensionsException, IncorrectFileExtensionException, IllegalArgumentException {
        String authorId = JwtUtils.getUsernameFromJwt();
        if(book != null && coverImageFile != null) {
            try {
                verifyCoverImage(coverImageFile);
                book.setAuthorId(authorId);
                book.setAuthorName(authorId);
                book.setCoverImage(new Binary(BsonBinarySubType.BINARY, coverImageFile.getBytes()));
                book.setCreatedDate(LocalDate.now());
                book.setUpdatedDate(LocalDate.now());
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
                 verifyChapterFile(chapterFile);
                chapter.setCreatedDate(LocalDate.now());
                chapter.setUpdatedDate(LocalDate.now());
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

    public Boolean updateBook(String bookId, Book newBook, MultipartFile coverImage) throws BookNotFoundException, IncorrectFileExtensionException, IncorrectImageDimensionsException, IOException {
        if(newBook == null)
            throw new IllegalArgumentException();

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()) {
            verifyCoverImage(coverImage);
            newBook.setCoverImage(new Binary(BsonBinarySubType.BINARY, coverImage.getBytes()));
            Book book = optionalBook.get();
            book.setBookTitle(newBook.getBookTitle());
            book.setSummary(newBook.getSummary());
            book.setPointsRequiredForChapter(newBook.getPointsRequiredForChapter());
            book.setGenreList(newBook.getGenreList());
            book.setCoverImage(newBook.getCoverImage());
            Book savedBook = bookRepository.save(book);
            return savedBook.equals(newBook);
        }
        else
            throw new BookNotFoundException();
    }

    public List<BookDTO> getBooks(BookCriteria criteria) {
        QBook qBook = new QBook("book");
        BooleanBuilder predicate = new BooleanBuilder();
        if(criteria != null) {
            predicate.or(qBook.genreList.any().in(criteria.getGenreList()));
            if(criteria.getAuthorId() != null)
                predicate.or(qBook.authorId.eq(criteria.getAuthorId()));
            if(criteria.getBookTitle() != null)
                predicate.or(qBook.bookTitle.containsIgnoreCase(criteria.getBookTitle()));
            if(criteria.getAuthorName() != null)
                predicate.or(qBook.authorName.containsIgnoreCase(criteria.getAuthorName()));
        }
        List<Book> books = (List<Book>) bookRepository.findAll(predicate);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    private void verifyCoverImage(MultipartFile coverImageFile) throws IncorrectFileExtensionException, IOException, IncorrectImageDimensionsException {
        if (!COVER_IMAGE_PERMITTED_EXTENSIONS.contains(coverImageFile.getContentType()))
            throw new IncorrectFileExtensionException(coverImageFile.getContentType(), COVER_IMAGE_PERMITTED_EXTENSIONS);

        BufferedImage image = ImageIO.read(coverImageFile.getInputStream());
        if (image.getHeight() != COVER_IMAGE_HEIGHT || image.getWidth() != COVER_IMAGE_WIDTH)
            throw new IncorrectImageDimensionsException(image.getHeight(), image.getWidth(), COVER_IMAGE_HEIGHT, COVER_IMAGE_WIDTH);

        if (coverImageFile.getSize() > COVER_IMAGE_MAX_SIZE_IN_BYTES)
            throw new SizeLimitExceededException("Cover image file size exceeded " + COVER_IMAGE_MAX_SIZE_IN_BYTES, coverImageFile.getSize(), COVER_IMAGE_MAX_SIZE_IN_BYTES);
    }

    private void verifyChapterFile(MultipartFile chapterFile) throws SizeLimitExceededException, IncorrectFileExtensionException {
        if(chapterFile.getSize() > CHAPTER_PDF_MAX_SIZE_IN_BYTES)
            throw new SizeLimitExceededException("Chapter file size exceeded " + CHAPTER_PDF_MAX_SIZE_IN_BYTES, chapterFile.getSize(), CHAPTER_PDF_MAX_SIZE_IN_BYTES);
        if (!CHAPTER_FILE_PERMITTED_EXTENSIONS.contains(chapterFile.getContentType()))
            throw new IncorrectFileExtensionException(chapterFile.getContentType(), CHAPTER_FILE_PERMITTED_EXTENSIONS);
    }


    public BookDTO getBook(String bookId) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            BookDTO bookDto = modelMapper.map(optionalBook.get(), BookDTO.class);
            if(bookDto.getChapterList().size() > 0) {
                bookDto.getChapterList().forEach(c -> {
                    if(c.getChapterNo() <= 3) { // number of free chapters, default isLocked = true
                        c.setIsLocked(false);
                    }
                });
            }
            return bookDto;
        }
        else
            throw new BookNotFoundException();
    }

    public BookDTO getBookForLoginUsers(String bookId, String userId) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            BookDTO bookDto = modelMapper.map(optionalBook.get(), BookDTO.class);
            if(bookDto.getChapterList().size() > 0) {
                bookDto.getChapterList().forEach(c -> {
                    if(c.getChapterNo() <= 3) { // number of free chapters, default isLocked = true
                        c.setIsLocked(false);
                    }
                });
            }

            List<ChapterAccess> userChapterAccessList = chapterAccessService.getChapterAccessByBookId(bookId, userId);
            if(userChapterAccessList != null && userChapterAccessList.size() > 0) {
                bookDto.getChapterList().forEach(c -> {
                    if(userChapterAccessList.stream().map(ChapterAccess::getChapterId).collect(Collectors.toList()).contains(c.getChapterId())) {
                        c.setIsLocked(false);
                    }
                });
            }
            return bookDto;
        }
        else
            throw new BookNotFoundException();
    }
}
