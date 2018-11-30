/*
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

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.tistory.kollhong.arduino_bluetooth.ApplicationVO.DATABASE_NAME;
import static com.tistory.kollhong.arduino_bluetooth.ApplicationVO.ROOT_DIR;

public class DbDcreater {
    DbDcreater(Context context) {

        File folder = new File(ROOT_DIR + "/database");
        folder.mkdirs();
        File outfile = new File(ROOT_DIR + "/database/" + DATABASE_NAME);
        if (outfile.length() <= 0) {
            AssetManager assetManager = context.getResources().getAssets();
            try {
                InputStream input = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
                long filesize = input.available();
                byte[] tempdata = new byte[(int) filesize];
                input.read(tempdata);
                input.close();

                outfile.createNewFile();
                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
