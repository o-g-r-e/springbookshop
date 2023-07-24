package com.example.bookshop.data.service;

import com.example.bookshop.data.entity.Genre;
import com.example.bookshop.data.GenreCategory;
import com.example.bookshop.data.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres();
    }

    public List<GenreCategory> getSortedGenres() {
        List<Genre> genres = genreRepository.getGenres();
        List<GenreCategory> result = new ArrayList<>();
        List<Genre> rootGenres = genres.stream().filter(genreItem->genreItem.getParentId()==null).collect(Collectors.toList());
        for (Genre rootGenre : rootGenres) {
            List<Genre> subGenres = genres.stream().filter(genreItem->genreItem.getParentId()==rootGenre.getId()).collect(Collectors.toList());
            result.add(new GenreCategory(rootGenre, subGenres));
        }
        return result;
    }
}
