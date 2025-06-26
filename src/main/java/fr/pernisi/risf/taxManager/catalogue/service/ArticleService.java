package fr.pernisi.risf.taxmanager.catalogue.service;

import fr.pernisi.risf.taxmanager.catalogue.ArticleRepository;
import fr.pernisi.risf.taxmanager.catalogue.dto.ArticleDto;
import fr.pernisi.risf.taxmanager.catalogue.dto.ArticleRegisterDto;
import fr.pernisi.risf.taxmanager.catalogue.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private ArticleRepository repository;
    private ArticleMapperService articleMapperService;


    public List<ArticleDto> getAllArticles() {
        return this.repository.findAll().stream().map( article -> articleMapperService.entityToDTO(article)).collect(Collectors.toList());
    }

    public Optional<Article> getArticleById(Long id) {
        return repository.findById(id);
    }

    public Article createArticle(ArticleDto article) {
        return repository.save(articleMapperService.DTOtoEntity(article));
    }

    public Article updateArticle(Long id, ArticleDto updatedArticle) {
        return repository.findById(id).map(article -> {
            article.setTitle( updatedArticle.getTitle());
            article.setDescription(updatedArticle.getDescription());
            return repository.save(article);
        }).orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    public void deleteArticle(Long id) {
        repository.deleteById(id);
    }

    public Optional<Article> findByTitle(String title) {
        return repository.findByTitle(title);
    }


}
