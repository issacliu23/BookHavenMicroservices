package com.ncs.nusiss.bookservice.book.chapter;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
}
