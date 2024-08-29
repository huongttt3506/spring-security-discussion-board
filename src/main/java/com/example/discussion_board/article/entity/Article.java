package com.example.discussion_board.article.entity;

import com.example.discussion_board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.FieldError;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article_table")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    private String title;
    @Setter
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserEntity author;

}
