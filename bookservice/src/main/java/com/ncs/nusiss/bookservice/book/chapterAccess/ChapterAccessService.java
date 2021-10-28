package com.ncs.nusiss.bookservice.book.chapterAccess;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.exceptions.ChapterAccessExistsException;
import com.ncs.nusiss.bookservice.exceptions.ChapterAccessNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterAccessService {

    @Autowired
    private ChapterAccessRepository chapterAccessRepository;

    public List<ChapterAccess> getChapterAccessByBookId(String bookId, String userId) throws IllegalStateException {
        Optional<List<ChapterAccess>> optionalUserChapterList = chapterAccessRepository.findByBookIdAndUserId(bookId, userId);
        return optionalUserChapterList.orElseGet(ArrayList::new);
    }

    public ChapterAccess getChapterAccess(String chapterId, String userId) throws IllegalStateException, ChapterAccessNotFoundException {
        ChapterAccess chapterAccess;
        Optional<ChapterAccess> chapterAccessOptional = chapterAccessRepository.findByChapterIdAndUserId(chapterId, userId);
        if (chapterAccessOptional.isPresent()) {
            chapterAccess = chapterAccessOptional.get();
        } else
            throw new ChapterAccessNotFoundException();
        return chapterAccess;
    }

    public ChapterAccess addChapterAccess(Chapter chapter, String userId) throws ChapterAccessExistsException {
        ChapterAccess chapterAccess = new ChapterAccess();
        ChapterAccess savedChapterAccess = null;
        Optional<ChapterAccess> chapterAccessOptional = chapterAccessRepository.findByChapterIdAndUserId(chapter.getChapterId(), userId);
        if (!chapterAccessOptional.isPresent()) {
            chapterAccess.setChapterId(chapter.getChapterId());
            chapterAccess.setBookId(chapter.getBook().getBookId());
            chapterAccess.setUserId(userId);
            savedChapterAccess = chapterAccessRepository.save(chapterAccess);
        } else
            throw new ChapterAccessExistsException();
        return savedChapterAccess;
    }
}
