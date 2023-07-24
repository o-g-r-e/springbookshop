package com.example.bookshop.data.repository;

import com.example.bookshop.data.CountedTag;
import com.example.bookshop.data.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("SELECT new com.example.bookshop.data.CountedTag(t.tagValue, COUNT(bt.bookId)) FROM Tag t " +
            "JOIN Book2Tag bt ON t.id = bt.tagId " +
            "GROUP BY t.tagValue " +
            "ORDER BY COUNT(bt.bookId) DESC")
    List<CountedTag> getSortedTags(Pageable pageable);

    @Query(value = """
    SELECT tags.id, tags.tag_value FROM book2tag
    JOIN tags
    ON tags.id = tag_id
    WHERE book_id = ?1
    """, nativeQuery = true)
    List<Tag> getTagsByBookId(Integer bookId);
}
