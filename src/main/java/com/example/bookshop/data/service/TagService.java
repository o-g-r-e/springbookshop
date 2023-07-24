package com.example.bookshop.data.service;

import com.example.bookshop.data.CountedTag;
import com.example.bookshop.data.entity.Tag;
import com.example.bookshop.data.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {

    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<CountedTag> getTags() {
        Pageable pageable = PageRequest.of(0, 100);
        return tagRepository.getSortedTags(pageable);
    }

    public List<Tag> getTagsByBookId(Integer bookId) {
        return tagRepository.getTagsByBookId(bookId);
    }

    public List<CountedTag> randomSelectTags(List<CountedTag> tags) {
        List<CountedTag> result = new ArrayList<>(tags.subList(0, tags.size()));

        if(result.size() <= 75) {
            Collections.shuffle(result);
            return result;
        }

        Random random = new Random();
        while (result.size() > 50) {
            int index = random.nextInt(result.size() - 5) + 5;
            result.remove(tags.get(index));
        }
        Collections.shuffle(result);
        return result;
    }
}
