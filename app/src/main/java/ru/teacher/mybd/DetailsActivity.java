package ru.teacher.mybd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import static ru.teacher.mybd.MainActivity.CONTACT_FIRST_NAME_KEY;
import static ru.teacher.mybd.MainActivity.CONTACT_ID_KEY;
import static ru.teacher.mybd.MainActivity.CONTACT_PHONE_NUMBER_KEY;
import static ru.teacher.mybd.MainActivity.CONTACT_SECOND_NAME_KEY;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private long contactId;

    private TextInputLayout phoneLayout;
    private TextInputLayout firstNameLayout;
    private TextInputLayout secondNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Button applyButton = findViewById(R.id.Apply);
        Button cancelButton = findViewById(R.id.Cancel);
        phoneLayout = findViewById(R.id.PhoneLayout);
        firstNameLayout = findViewById(R.id.FirstNameLayout);
        secondNameLayout = findViewById(R.id.SecondNameLayout);

        Intent intent = getIntent();
        if (intent != null) {
            contactId = intent.getLongExtra(CONTACT_ID_KEY, -666);
            String firstName = intent.getStringExtra(CONTACT_FIRST_NAME_KEY);
            String secondName = intent.getStringExtra(CONTACT_SECOND_NAME_KEY);
            String phoneNumber = intent.getStringExtra(CONTACT_PHONE_NUMBER_KEY);

            phoneLayout.getEditText().setText(phoneNumber);
            firstNameLayout.getEditText().setText(firstName);
            secondNameLayout.getEditText().setText(secondName);

            setIntent(null);
        }

        applyButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Apply) {
            String phoneNumber = phoneLayout.getEditText().getText().toString().trim();
            String firstName = firstNameLayout.getEditText().getText().toString().trim();
            String secondName = secondNameLayout.getEditText().getText().toString().trim();

            if (phoneNumber.length() != 0 &&
                    firstName.length() != 0 &&
                    secondName.length() != 0) {
                Intent intent = new Intent();
                intent.putExtra(CONTACT_ID_KEY, contactId);
                intent.putExtra(CONTACT_FIRST_NAME_KEY, firstName);
                intent.putExtra(CONTACT_SECOND_NAME_KEY, secondName);
                intent.putExtra(CONTACT_PHONE_NUMBER_KEY, phoneNumber);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        else if (v.getId() == R.id.Cancel) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
