package com.udacity.sandwichclub;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra";
    private static final int DEFAULT_POSITION = -1;

    private TextView umOriginTextView;
    private TextView umAlsoKnownTextView;
    private TextView umIngredientsTextView;
    private TextView umDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        umOriginTextView = findViewById(R.id.uOrigin_tview);
        umAlsoKnownTextView = findViewById(R.id.uAlso_known_tview);
        umIngredientsTextView = findViewById(R.id.uIingredients_tview);
        umDescriptionTextView = findViewById(R.id.uDescription_tview);
        ImageView ingredientsIv = findViewById(R.id.uImage_iview);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich uSandwichs = null;
        try {
            uSandwichs = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (uSandwichs == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(uSandwichs);
        Picasso.with(this)
                .load(uSandwichs.getImage()).placeholder(R.mipmap.loading)
                .into(ingredientsIv);

        setTitle(uSandwichs.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    private void populateUI(Sandwich sandwich) {
        umDescriptionTextView.setText(sandwich.getDescription());


        umOriginTextView.setText(sandwich.getPlaceOfOrigin().isEmpty() ? getString(R.string.not_available) : sandwich.getPlaceOfOrigin());


        if (sandwich.getAlsoKnownAs().size() > 0) {
            umAlsoKnownTextView.setText(TextUtils.join("\n", sandwich.getAlsoKnownAs()));
        } else {

            umAlsoKnownTextView.setText(getString(R.string.not_available));
        }

        umIngredientsTextView.setText(TextUtils.join("\n", sandwich.getIngredients()));

    }
}