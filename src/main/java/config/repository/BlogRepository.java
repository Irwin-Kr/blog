package config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import config.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long>{

}
