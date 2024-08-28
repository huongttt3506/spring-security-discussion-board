package com.example.discussion_board.article;

import com.example.discussion_board.article.entity.Article;
import com.example.discussion_board.article.repo.ArticleRepository;
import com.example.discussion_board.user.entity.UserEntity;
import com.example.discussion_board.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    public ArticleService(
            ArticleRepository articleRepository,
            UserRepository userRepository
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void create(
            String title,
            String content,
            String username
    ) {
        Optional<UserEntity> user =
                userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        Article newArticle = new Article();
        newArticle.setTitle(title);
        newArticle.setContent(content);
        newArticle.setAuthor(user.get());
        articleRepository.save(newArticle);
    }

    public List<Article> readAll() {
        return articleRepository.findAll();
    }

}
