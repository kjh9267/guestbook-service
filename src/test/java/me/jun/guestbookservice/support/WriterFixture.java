package me.jun.guestbookservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.guestbookservice.core.domain.Writer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class WriterFixture {

    public static final Long WRITER_ID = 1L;

    public static Writer writer() {
        return Writer.builder()
                .value(WRITER_ID)
                .build();
    }
}