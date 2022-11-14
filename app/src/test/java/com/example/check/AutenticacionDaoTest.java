package com.example.check;

import static org.junit.Assert.*;

import com.example.check.repositorio.dao.AutenticacionDao;

import org.junit.Test;
import org.mockito.Mock;

public class AutenticacionDaoTest {

    @Mock
    AutenticacionDao autenticacionDao;
    @Test
    public void Given_user_null_When_AutenticacionDao_Then_return_false() {
        autenticacionDao = new AutenticacionDao();
        assertEquals(false,autenticacionDao.esValido(null,"admin@gmail.com","admin123","admin123"));
    }
    @Test
    public void Given_user_void_When_AutenticacionDao_Then_return_false() {
        autenticacionDao = new AutenticacionDao();
        assertEquals(false, autenticacionDao.esValido("","admin@gmail.com","admin123","admin123"));
    }


}