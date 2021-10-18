package com.ncs.nusiss.bookservice.book.chapterAccess;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChapterAccessRepository extends MongoRepository<ChapterAccess, String> {

    Optional<ChapterAccess> findByChapterIdAndUserId(String chapterId, String userId);
}
