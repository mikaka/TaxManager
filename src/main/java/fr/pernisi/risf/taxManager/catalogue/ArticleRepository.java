package fr.pernisi.risf.taxmanager.catalogue;

import fr.pernisi.risf.taxmanager.catalogue.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByTitle(String title);
}
