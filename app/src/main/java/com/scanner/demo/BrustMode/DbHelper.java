/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scanner.demo.BrustMode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    AppCompatActivity a;
    public DbHelper(@Nullable AppCompatActivity context) {
        super(context, "image", null,1);
        a=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table images(image blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
    public boolean insert(byte[] mbyte){
        ContentValues c=new ContentValues();
        c.put("image",mbyte);
        return getWritableDatabase().insert("images",null,c)>0;
    }
    public List<byte[]> getAll(){
        List<byte[]> list=new ArrayList<>();
        Cursor c=getReadableDatabase().rawQuery("select image from images",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            list.add(c.getBlob(0));
            c.moveToNext();
        }
        return list;
    }
    public void remove(){
        getWritableDatabase().execSQL("delete from images");

    }
}
