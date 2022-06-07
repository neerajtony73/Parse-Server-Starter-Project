/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpActive = true;
  EditText password ;
   TextView loginText;


  @Override
  public void onClick(View view) {
    Button signUpButton = (Button) findViewById(R.id.signUpButton);
    if(view.getId() == R.id.orLogin){
      if(signUpActive){
        signUpActive = false;
        assert signUpButton != null;
        signUpButton.setText("Log In");
        assert loginText != null;
        loginText.setText("or Sign Up");

      }
      else {
        signUpActive = true;
        assert signUpButton != null;
        signUpButton.setText("Sign Up");
        assert loginText != null;
        loginText.setText("or Log In");

      }


    }
    else if(view.getId() == R.id.relativeLayoutView || view.getId() == R.id.logo){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(),UserList.class);
    startActivity(intent);
  }

  public void signUpProcess(View view){

     EditText username = (EditText) findViewById(R.id.editUserName);
    assert username != null;
    assert password != null;
    if(username.getText().toString().matches("") || password.getText().toString().matches("") ) {
        Toast.makeText(this, "A username and password are required",Toast.LENGTH_SHORT).show();
      }
    else {
      if (signUpActive) {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username.getText().toString());
        parseUser.setPassword(password.getText().toString());
        parseUser.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Toast.makeText(MainActivity.this, "Sign Up:Successful", Toast.LENGTH_SHORT).show();
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
      else {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (e == null) {
              Toast.makeText(MainActivity.this, "Log in:Successful", Toast.LENGTH_SHORT).show();
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");
    loginText = (TextView) findViewById(R.id.orLogin);
    loginText.setOnClickListener(this);
    password = (EditText) findViewById(R.id.editPassword);
    password.setOnKeyListener(this);
    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutView);
    ImageView imageView = (ImageView) findViewById(R.id.logo);
    relativeLayout.setOnClickListener(this);
    imageView.setOnClickListener(this);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    if(ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getUsername() != null){
      showUserList();
    }

  }


  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signUpProcess(view);
    }
    return false;
  }
}