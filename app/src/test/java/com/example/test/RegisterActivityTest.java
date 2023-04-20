package com.example.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest{

public RegisterActivityTest(){
}

@BeforeClass
    public static void setUpClass() {
}

@AfterClass
    public static void tearDownClass(){
    }

    @Test
    public void testEmailValidate(){
    System.out.print("Email Validation");
    String email = "Gavinkelly@gmail.com";
    RegisterActivity instance = new RegisterActivity();
    Boolean result = Boolean.valueOf(String.valueOf(instance.add(email)));
    Boolean expResult = true;
    assertEquals(expResult, result);
    }

    @Test
    public void passwordValidate(){
        System.out.print("Password Validation");
        String password = "Password1234";
        RegisterActivity instance = new RegisterActivity();
        Boolean result = Boolean.valueOf(String.valueOf(instance.passwordValidate(password)));
        Boolean expResult = false;
        assertEquals(expResult, result);
    }
}


