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

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;

public class ActivityRegister extends AppCompatActivity {
    static final String isMyPage = "isMypage";
    private ArrayAdapter adapter;
    private Spinner spinner;
    RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = findViewById(R.id.ageSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.weightSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.weight, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = findViewById(R.id.heightSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.height, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn = findViewById(R.id.registerButton);
        btn.setOnClickListener(
                v -> {

                    /*
                     * Copyright (c) 2019. KollHong.
                     *
                     */

                    mAccounts mAcc = new mAccounts(this, true);

                    EditText nameText = findViewById(R.id.nameText);
                    String name = nameText.getText().toString();

                    EditText idText = findViewById(R.id.idText);
                    String id = idText.getText().toString();

                    EditText passwordText = findViewById(R.id.passwordText);
                    String pw = passwordText.getText().toString();

                    EditText emailText = findViewById(R.id.emailText);
                    String email = emailText.getText().toString();

                    RadioGroup rd = findViewById(R.id.genderGroup);
                    RadioButton rb = findViewById(rd.getCheckedRadioButtonId());
                    int gender;
                    if (rb.getId() == R.id.genderWoman)
                        gender = 1;
                    else gender = 0;


                    Spinner ageSpinner = findViewById(R.id.ageSpinner);
                    String[] array = getResources().getStringArray(R.array.age);
                    String string = array[ageSpinner.getSelectedItemPosition()];
                    int age = Integer.parseInt(string.substring(0, 1));

                    Spinner weightSpinner = findViewById(R.id.weightSpinner);
                    array = getResources().getStringArray(R.array.weight);
                    string = array[weightSpinner.getSelectedItemPosition()];
                    float weight = Float.parseFloat(string.split("kg")[0]);


                    Spinner heightSpinner = findViewById(R.id.heightSpinner);
                    array = getResources().getStringArray(R.array.height);
                    string = array[heightSpinner.getSelectedItemPosition()];
                    float height = Float.parseFloat(string.split("cm")[0]);

                    switch (mAcc.Join(id, pw, name, age, weight, height, email, gender)) {
                        //3 -> fail, 1 -> id redundant, 2-> pw condition not satisfied, 0->success
                        case 0:
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case 1:
                            Toast.makeText(this, "이미 가입 된 ID입니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(this, "PW 조건을 확인해주세요", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(this, "이유를 알 수 없는 실패입니다.", Toast.LENGTH_SHORT).show();
                            break;


                    }
                }
        );

        Bundle bd = getIntent().getBundleExtra("bundle");
        if (bd.getBoolean(isMyPage)) {

            /*
             * Copyright (c) 2019. KollHong.
             *
             */

            String session = bd.getString("session");

            mAccounts accounts = new mAccounts(getApplicationContext(), true);
            ContentValues accinfo = accounts.getAccountInfo();

            int age = accinfo.getAsInteger(mDbMan.userTableVar[3]);
            float weight = accinfo.getAsFloat(mDbMan.userTableVar[4]);
            float height = accinfo.getAsFloat(mDbMan.userTableVar[5]);


            TextView title = findViewById(R.id.register_title);
            title.setText(R.string.Mypage);

            EditText nameText = findViewById(R.id.nameText);
            nameText.setText(accinfo.getAsString(mDbMan.userTableVar[2]));

            EditText idText = findViewById(R.id.idText);
            idText.setText(session);

            EditText emailText = findViewById(R.id.emailText);
            emailText.setText(accinfo.getAsString(mDbMan.userTableVar[6]));

            RadioGroup rd = findViewById(R.id.genderGroup);
            if (accinfo.getAsInteger(mDbMan.userTableVar[7]) == 0) rd.check(R.id.genderMan);
            else if (accinfo.getAsInteger(mDbMan.userTableVar[7]) == 1) rd.check(R.id.genderWoman);


            Spinner ageSpinner = findViewById(R.id.ageSpinner);
            String[] array = getResources().getStringArray(R.array.age);
            int position = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i].contains(String.valueOf(age))) {
                    position = i;
                    break;
                }
            }
            ageSpinner.setSelection(position);

            Spinner weightSpinner = findViewById(R.id.weightSpinner);
            array = getResources().getStringArray(R.array.weight);
            position = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i].contains(String.valueOf(weight))) {
                    position = i;
                    break;
                }

            }
            weightSpinner.setSelection(position);


            Spinner heightSpinner = findViewById(R.id.heightSpinner);
            array = getResources().getStringArray(R.array.height);
            position = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i].contains(String.valueOf(height))) {
                    position = i;
                    break;
                }

            }
            heightSpinner.setSelection(position);


        }
    }

}