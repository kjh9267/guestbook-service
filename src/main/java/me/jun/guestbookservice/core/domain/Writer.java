package me.jun.guestbookservice.core.domain;

import lombok.*;
import me.jun.guestbookservice.core.domain.exception.WriterMismatchException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
@Getter
@Embeddable
public class Writer {

    @Column(
            name = "writerId",
            nullable = false
    )
    private Long value;

    public Writer validate(Long value) {
        if (!this.value.equals(value)) {
            throw WriterMismatchException.of(value.toString());
        }
        return this;
    }
}
