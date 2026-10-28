package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public final class AddContactActivity extends BaseActivity {
    private EditText nameInput, phoneInput, notesInput;
    private Spinner groupSpinner;
    private RadioGroup genderGroup;
    private CheckBox favoriteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_add_contact);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        notesInput = findViewById(R.id.notesInput);
        clearErrorWhenEdited(nameInput);
        clearErrorWhenEdited(phoneInput);
        groupSpinner = findViewById(R.id.groupSpinner);
        genderGroup = findViewById(R.id.genderGroup);
        favoriteInput = findViewById(R.id.favoriteInput);
        ArrayAdapter<CharSequence> groups = ArrayAdapter.createFromResource(this,
                R.array.contact_groups, android.R.layout.simple_spinner_item);
        groups.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groups);
        findViewById(R.id.backButton).setOnClickListener(view -> finish());
        findViewById(R.id.saveButton).setOnClickListener(view -> saveContact());
    }

    private void saveContact() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        nameInput.setError(null);
        phoneInput.setError(null);
        boolean valid = true;
        if (!ContactValidator.isValidName(name)) {
            nameInput.setError(getString(R.string.required_name));
            valid = false;
        }
        if (!ContactValidator.isValidPhone(phone)) {
            phoneInput.setError(getString(R.string.invalid_phone));
            valid = false;
        } else if (repository().containsPhone(phone)) {
            phoneInput.setError(getString(R.string.duplicate_phone));
            valid = false;
        }
        if (!valid) {
            if (nameInput.getError() != null) nameInput.requestFocus();
            else phoneInput.requestFocus();
            return;
        }
        int checkedId = genderGroup.getCheckedRadioButtonId();
        String gender = getString(checkedId == R.id.genderFemale ? R.string.female
                : checkedId == R.id.genderOther ? R.string.other : R.string.male);
        repository().add(name, phone, groupSpinner.getSelectedItem().toString(),
                gender, favoriteInput.isChecked(), notesInput.getText().toString());
        Toast.makeText(this, R.string.contact_saved, Toast.LENGTH_SHORT).show();
        finish();
    }
}
