package com.example.burgerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ListPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        Button visit_button = (Button) findViewById(R.id.visit_button);
        Button plan_button = (Button) findViewById(R.id.plan_button);


        visit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_VisitPage();
            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    //  resault here
                }

            }
        }

        private void start_VisitPage () {
            Intent intent = new Intent(this, VisitListActivity.class);
            startActivity(intent);

        }
    }
