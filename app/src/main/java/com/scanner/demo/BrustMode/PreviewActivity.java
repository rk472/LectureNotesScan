
package com.scanner.demo.BrustMode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scanner.demo.R;

import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        List<byte[]> list=new DbHelper(this).getAll();
        //Toast.makeText(this, ""+list.size(), Toast.LENGTH_SHORT).show();
        PreviewAdapter p=new PreviewAdapter(list,this);
        RecyclerView r=(RecyclerView)findViewById(R.id.preview_list);
        r.setLayoutManager(new GridLayoutManager(this,3));
        r.setHasFixedSize(true);
        r.setAdapter(p);
    }

}
