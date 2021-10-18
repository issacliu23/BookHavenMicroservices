package com.ncs.nusiss.bookservice.book.chapter;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.ncs.nusiss.bookservice.book.Book;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.ChapterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations operations;

    public Chapter getChapterDetail(String chapterId) throws IllegalStateException, ChapterNotFoundException {
        Chapter chapter = new Chapter();
        Optional<Chapter> chapterOptional = chapterRepository.findById(chapterId);
        if (chapterOptional.isPresent()) {
            chapter = chapterOptional.get();
            chapter.setAuthorId(chapter.getBook().getAuthorId());
            chapter.setPointsRequiredForChapter(chapter.getBook().getPointsRequiredForChapter());
        } else
            throw new ChapterNotFoundException();
        return chapter;
    }

    public Chapter getChapterContent(String chapterId) throws IllegalStateException, ChapterNotFoundException, IOException {
        Chapter chapter = new Chapter();
        Optional<Chapter> chapterOptional = chapterRepository.findById(chapterId);
        if (chapterOptional.isPresent()) {
            chapter = chapterOptional.get();
            GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("metadata.chapterId").is(chapter.getChapterId())));
            chapter.setStream(operations.getResource(gridFSFile).getInputStream());
            chapter.setChapterId(gridFSFile.getMetadata().get("chapterId").toString());
        } else
            throw new ChapterNotFoundException();
        return chapter;
    }

}
