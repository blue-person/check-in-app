package com.example.check;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@ExtendWith(MockitoExtension.class)

public class ExampleUnitTest {

    @InjectMocks
    BookDao dao;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void Given_not_exists_When_call_register_book_Then_register_book() {
        assertEquals(false);
    }
}
