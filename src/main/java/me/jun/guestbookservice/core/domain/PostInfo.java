package me.jun.guestbookservice.core.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Builder(toBuilder = true)
@Getter
@Embeddable
public class PostInfo {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    public PostInfo updateTitle(String newTitle) {
        this.title = newTitle;
        return this;
    }

    public PostInfo updateContent(String newContent) {
        this.content = newContent;
        return this;
    }
}
