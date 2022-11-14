package com.example.check;

import static org.junit.Assert.assertEquals;

import com.example.check.repositorio.dao.AutenticacionDao;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @InjectMocks
    AutenticacionDao autenticacionDao;
    @Test
    public void Given_user_null_When_AutenticacionDao_Then_return_false() {
        autenticacionDao = new AutenticacionDao();
        assertEquals(false,autenticacionDao.esValido(null,"admin@gmail.com","admin123","admin123"));
    }
    @Test
<<<<<<< Updated upstream
    void Given_not_exists_When_call_register_book_Then_register_book() {
        assertEquals(false);
    }
}
=======
    public void Given_user_void_When_AutenticacionDao_Then_return_false() {
        autenticacionDao = new AutenticacionDao();
        assertEquals(false, autenticacionDao.esValido("","admin@gmail.com","admin123","admin123"));
    }

}
>>>>>>> Stashed changes
