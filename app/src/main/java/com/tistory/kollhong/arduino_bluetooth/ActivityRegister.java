/*
 * Copyright (c) 2019. KollHong. All Rights Reserved.
 * Copyright (c) 2018. KollHong. All Rights Reserved.
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class ActivityRegister extends AppCompatActivity {

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

        final EditText nameText = findViewById(R.id.nameText);
        final EditText idText = findViewById(R.id.idText);
        final EditText passwordText = findViewById(R.id.passwordText);
        final EditText emailText = findViewById(R.id.emailText);

    }

    public void OnRegister(View v) {

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

}