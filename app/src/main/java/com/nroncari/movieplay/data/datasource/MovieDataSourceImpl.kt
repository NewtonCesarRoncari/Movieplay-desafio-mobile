package com.nroncari.movieplay.data.datasource

import com.nroncari.movieplay.data.mapper.MovieDetailToDomainMapper
import com.nroncari.movieplay.data.mapper.MovieToDomainMapper
import com.nroncari.movieplay.data.service.MovieService
import com.nroncari.movieplay.domain.model.MovieDetailDomain
import com.nroncari.movieplay.domain.model.MovieListItemDomain

class MovieDataSourceImpl(
    private val service: MovieService
) : MovieDataSource {

    private val mapper = MovieToDomainMapper()
    private val movieDetailMapper = MovieDetailToDomainMapper()

    override suspend fun getMoviesByGenre(page: Int, genre: Int): List<MovieListItemDomain> {
        return service.getMoviesByGenre(page = page, genre = genre).results.map { movieResponse ->
            mapper.map(movieResponse)
        }
    }

    override suspend fun getMovieDetailBy(movieId: Long): MovieDetailDomain {
        return movieDetailMapper.map(service.getMovieDetailBy(movieId = movieId))
    }

    override suspend fun getMovieRecommendationsBy(movieId: Long): List<MovieListItemDomain> {
        return service.getMovieRecommendationsBy(movieId).results.map { movies ->
            mapper.map(movies)
        }
    }
}