package com.example.loginpack;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginPack {
    ConstraintLayout mainLayout;
    Button btnLogin;
    EditText edtEmail;
    EditText edtPassword;
    View loginPage;
    View successDialog;
    View failureDialog;
    Resources resources;
    Activity activity;
    JSONObject json;

    public interface LogCallback{
        void success(JSONObject json);
        void failed();
    }

    LoginPack(final String api, int parentLayout, int loginLayout, final Activity activity,
              final LogCallback listener, String packageName, String mainLayoutId,
              String emailFieldId, String passwordFieldId, String submitButtonId)
    {
        activity.setContentView(parentLayout);
        this.activity = activity ;
        PackageManager manager = activity.getPackageManager();

        try {
            resources = manager.getResourcesForApplication(packageName);
            int resLayout = resources.getIdentifier(mainLayoutId, "id", packageName);
            mainLayout = activity.findViewById(resLayout);
            loginPage=activity.getLayoutInflater().inflate(loginLayout,null);
            loginPage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mainLayout.addView(loginPage);
            int resEdtMail = resources.getIdentifier(emailFieldId,"id",packageName);
            int resEdtPassword = resources.getIdentifier(passwordFieldId,"id",packageName);
            int resBtnSubmit = resources.getIdentifier(submitButtonId,"id",packageName);

            edtEmail=activity.findViewById(resEdtMail);
            edtPassword=activity.findViewById(resEdtPassword);
            btnLogin=activity.findViewById(resBtnSubmit);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    edtEmail.setError("Please enter your email Id");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    edtPassword.setError("Please enter password");
                    return;
                }

                if(!isEmailValid(edtEmail.getText().toString().trim()))
                    Toast.makeText(activity, "Invalid email Id", Toast.LENGTH_SHORT).show();

                else
                {
                    loginApi(api,email,password);
                }


            }

            private void loginApi(String api,String email,String password) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(api)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APIService service = retrofit.create(APIService.class);

                Call<ResponseBody> call = service.userLogin(email, password);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("RESPONSE", ""+response);

                        if(response.code()==200)
                        {
                            Log.e("RESPONSE BODY",new Gson().toJson(response.body()));
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                listener.success(json);
                                Log.e("RESPONSE",new Gson().toJson(json));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            listener.failed();
                            Log.e("RESPONSE BODY",new Gson().toJson(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            private boolean isEmailValid(String trim) {

                boolean isValid = false;

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                CharSequence inputStr = trim;

                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);
                if (matcher.matches()) {
                    isValid = true;
                }
                return isValid;
            }
        });

    }

    LoginPack(final String api, int parentLayout, int loginLayout, final Activity activity,
              final LogCallback listener, String packageName, String mainLayoutId,
              String emailFieldId, String passwordFieldId, String submitButtonId,
              Boolean isSuccessDialogue,int successLayout, int failureLayout)
    {

        activity.setContentView(parentLayout);
        this.activity = activity ;
        PackageManager manager = activity.getPackageManager();

        try {
            resources = manager.getResourcesForApplication(packageName);
            int resLayout = resources.getIdentifier(mainLayoutId, "id", packageName);
            mainLayout = activity.findViewById(resLayout);
            loginPage=activity.getLayoutInflater().inflate(loginLayout,null);
            successDialog = activity.getLayoutInflater().inflate(successLayout,null);
            successDialog.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            failureDialog = activity.getLayoutInflater().inflate(failureLayout,null);

            loginPage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mainLayout.addView(loginPage);
            int resEdtMail = resources.getIdentifier(emailFieldId,"id",packageName);
            int resEdtPassword = resources.getIdentifier(passwordFieldId,"id",packageName);
            int resBtnSubmit = resources.getIdentifier(submitButtonId,"id",packageName);

            edtEmail=activity.findViewById(resEdtMail);
            edtPassword=activity.findViewById(resEdtPassword);
            btnLogin=activity.findViewById(resBtnSubmit);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    edtEmail.setError("Please enter your email Id");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    edtPassword.setError("Please enter password");
                    return;
                }

                if(!isEmailValid(edtEmail.getText().toString().trim()))
                    Toast.makeText(activity, "Invalid email Id", Toast.LENGTH_SHORT).show();

                else
                {
                    loginApi(api,email,password,successDialog,failureDialog);
                }
            }

            private void loginApi(String api, String email, String password, final View successDialog, final View failureDialog) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(api)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APIService service = retrofit.create(APIService.class);

                Call<ResponseBody> call = service.userLogin(email, password);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e("RESPONSE", ""+response);

                        if(response.code()==200)
                        {
                            Log.e("RESPONSE BODY",new Gson().toJson(response.body()));
                            try {
                                json = new JSONObject(response.body().string());
                                Log.e("RESPONSE",new Gson().toJson(json));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            showDialog(json,successDialog,true);


                        }
                        else
                        {
                            listener.failed();
                            showDialog(json,failureDialog,false);
                        }
                    }

                    private void showDialog(final JSONObject json, View successDialog, final Boolean login) {
                        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(successDialog);
                        dialog.show();

                        new CountDownTimer(2000, 1000) {
                            public void onFinish() {
                                dialog.dismiss();
                                if(login) {
                                    listener.success(json);
                                }
                                else {
                                    listener.failed();
                                }
                            }
                            public void onTick(long millisUntilFinished) {
                            }
                        }.start();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            private boolean isEmailValid(String trim) {

                boolean isValid = false;

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                CharSequence inputStr = trim;

                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);
                if (matcher.matches()) {
                    isValid = true;
                }
                return isValid;
            }
        });


    }



    public static class Builder{
        static String api;
        int mainLayout;
        int loginLayout;
        int successLayout;
        int failureLayout;
        Boolean isSucessDialog = false;
        Activity delegate;
        LogCallback listener;
        String packageName;
        String emailFieldId;
        String passwordFieldId;
        String mainLayoutId;
        String submitButtonId;

        public Builder()
        {

        }

        public Builder callback(LogCallback listener){
            this.listener = listener;
            return this;
        }

        public Builder api(String api){
            this.api = api;
            return this;
        }

        public Builder emailFieldId(String Id){
            this.emailFieldId = Id;
            return this;
        }
        public Builder submitButtonId(String Id){
            this.submitButtonId = Id;
            return this;
        }

        public Builder passwordFieldId(String Id){
            this.passwordFieldId = Id;
            return this;
        }
        public Builder mainLayoutId(String Id){
            this.mainLayoutId = Id;
            return this;
        }

        public Builder delegate(Activity delegate){
            this.delegate = delegate;
            return this;
        }

        public Builder mainLayout(int layout) {
            this.mainLayout = layout;
            return this;
        }

        public Builder successLayout(int layout) {
            this.successLayout = layout;
            return this;
        }

        public Builder failureLayout(int layout) {
            this.failureLayout = layout;
            return this;
        }

        public Builder isDialogue(Boolean isDialog)
        {
            this.isSucessDialog = isDialog;
            return this;

        }

        public Builder loginLayout(int login)
        {
            this.loginLayout=login;
            return this;
        }


        public Builder packageName(String packageName)
        {
            this.packageName = packageName;
            return this;
        }

        public LoginPack build(){

            if(!isSucessDialog) {
                return new LoginPack(api, mainLayout, loginLayout, delegate,
                        listener, packageName, mainLayoutId, emailFieldId,
                        passwordFieldId, submitButtonId);
            }
            else
            {
                return new LoginPack(api, mainLayout, loginLayout, delegate,
                        listener, packageName, mainLayoutId, emailFieldId,
                        passwordFieldId, submitButtonId,isSucessDialog,successLayout,failureLayout);
            }
        }

    }


}
