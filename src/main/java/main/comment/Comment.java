package main.comment;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public final class Comment {
    private final String author;
    private final String content;
    private final LocalDate createdAt;

    public Comment(final String author,
                   final String content,
                   final LocalDate createdAt) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }
}
