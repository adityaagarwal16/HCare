package com.hcare.homeopathy.hcare.Disease;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.Checkout.CheckoutActivity;
import com.hcare.homeopathy.hcare.DiseaseInfo;
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

import static com.hcare.homeopathy.hcare.Constants.DISEASE_OBJECT;

public class DiseaseActivity extends BaseActivity {

    DiseaseInfo disease;
    private EditText mChatMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        disease = new DiseaseInfo((Diseases)
                getIntent().getSerializableExtra("request_type1"));

        setToolbar();
        setContent();
        mChatMessageView = findViewById(R.id.diseases);
    }

    public void continueButton(View view) {
        if(mChatMessageView.getText().toString().equals(""))
            Toast.makeText(this, "Please enter your health Issue",
                    Toast.LENGTH_SHORT).show();
        else {
            try {
                Intent regIntent = new Intent(DiseaseActivity.this,
                        CheckoutActivity.class);
                regIntent.putExtra(DISEASE_OBJECT,
                        getIntent().getSerializableExtra("request_type1"));
                regIntent.putExtra("details1", mChatMessageView.getText().toString());
                startActivity(regIntent);
            }
            catch (Exception ignored) { }
        }
    }

    private void setContent() {
        ((ImageView) findViewById(R.id.diseaseImage)).setImageResource(disease.getDrawable());
        ((TextView) findViewById(R.id.aboutDisease)).setText(disease.getInfo());
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ((TextView) findViewById(R.id.title)).setText(disease.getDiseaseName());
        findViewById(R.id.howItWorks).setOnClickListener(v -> showHowToUseDialog());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHowToUseDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_how_it_works);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        dialog.show();
    }


}

