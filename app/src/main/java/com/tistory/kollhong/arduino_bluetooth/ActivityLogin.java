/*
 * Copyright (c) 2019. Team 김우준. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tistory.kollhong.arduino_bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import static com.tistory.kollhong.arduino_bluetooth.ActivityRegister.isMyPage;

public class ActivityLogin extends AppCompatActivity {
    private mAccounts mAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAcc = new mAccounts(this, false);
        final TextView id = findViewById(R.id.idText);
        final TextView pw = findViewById(R.id.passwordText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(
                v -> {
                    /*
                     * Copyright (c) 2019. KollHong. All Rights Reserved.
                     */
                    String session = mAcc.Login(id.getText().toString(), pw.getText().toString());
                    if (session.equals("false")) {
                        Snackbar.make(findViewById(R.id.LoginView), R.string.login_failed, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(v.getContext(), ActivityMain.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                        finish();
                    }
                });


        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(v.getContext(), ActivityRegister.class);
                    Bundle bd = new Bundle();
                    bd.putBoolean(isMyPage, false);
                    intent.putExtra("bundle", bd);
                    startActivity(intent);
                });

    }

}
