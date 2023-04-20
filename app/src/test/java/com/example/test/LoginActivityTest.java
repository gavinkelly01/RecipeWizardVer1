package com.example.test;

import static org.junit.Assert.assertEquals;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.core.app.ApplicationProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    public LoginActivityTest(){
    }

    @BeforeClass
    public static void setUpClass(){
    }

    @AfterClass
    public static void tearDownClass(){
    }

    @Test
    public void testValidateSharedPreferences(){
        System.out.println("Account Saved");
        String email = "ValidateTest@gmail.com";
        String password = "Test";

        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences testSharedPreferences = context.getSharedPreferences("test_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = testSharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();

        LoginActivity instance = new LoginActivity();
        instance.mSharedPreferences = testSharedPreferences;
        Boolean expResult = true;
        Boolean result = instance.validateLoginCredentials(email,password);
        assertEquals(expResult, result);
    }

    @Test
    public void LoginEmailValidate(){
        System.out.print("Email Validation");
        String email = "testing@gmail.com";
        LoginActivity instance = new LoginActivity();
        Boolean result = Boolean.valueOf(String.valueOf(instance.loginEmailValidate(email)));
        Boolean expResult = true;
        assertEquals(expResult, result);
    }

}
