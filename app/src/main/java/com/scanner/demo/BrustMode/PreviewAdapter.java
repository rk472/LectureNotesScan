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

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scanner.demo.R;

import java.util.List;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewViewHolder> {
    List<byte[]> list;
    AppCompatActivity a;

    public PreviewAdapter(List<byte[]> list, AppCompatActivity a) {
        this.list = list;
        this.a = a;
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PreviewViewHolder(LayoutInflater.from(a).inflate(R.layout.preview_row,parent,false));
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        holder.setImage(list.get(position));
        //Toast.makeText(a, "position "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
