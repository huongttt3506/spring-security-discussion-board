package com.example.discussion_board.article.dto;

import com.example.discussion_board.article.entity.Article;
import com.example.discussion_board.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;

    private String title;
    private String content;
    private UserEntity author;

    public static ArticleDto fromEntity(Article entity) {
        return new ArticleDto (
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthor()
        );
    }

}
