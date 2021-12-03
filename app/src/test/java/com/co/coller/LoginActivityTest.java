package com.co.coller;

import android.util.Log;

import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    private String email = "admin@admin.com";
    private String password = "admin";
    private String message = null;
    private com.co.coller.api.api api;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Before
    public void beforeTest(){
        MockitoAnnotations.initMocks(this);
        api = apiClient.getClient().create(api.class);
    }

    @Test
    public void login() throws InterruptedException{
        api.postLogin(email, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                message = response.body().toString();
                latch.countDown();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", t.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertNotNull(message);
        System.out.println(message);
    }
}