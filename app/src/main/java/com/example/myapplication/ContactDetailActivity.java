package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public final class ContactDetailActivity extends BaseActivity {
    public static final String EXTRA_CONTACT_ID = "com.example.myapplication.CONTACT_ID";
    private long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactId = getIntent().getLongExtra(EXTRA_CONTACT_ID, -1L);
        Contact contact = repository().findById(contactId);
        if (contact == null) {
            Toast.makeText(this, R.string.contact_missing, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        showScreen(R.layout.activity_contact_detail);
        setText(R.id.detailAvatar, contact.getInitial());
        setText(R.id.detailName, contact.getFullName());
        setText(R.id.detailPhone, contact.getPhone());
        setText(R.id.detailGroup, contact.getGroup());
        setText(R.id.detailGender, contact.getGender());
        setText(R.id.detailNotes, contact.getNotes().isEmpty()
                ? getString(R.string.no_notes) : contact.getNotes());
        CheckBox favorite = findViewById(R.id.detailFavorite);
        favorite.setChecked(contact.isFavorite());
        favorite.setOnCheckedChangeListener((button, checked) ->
                repository().setFavorite(contactId, checked));
        findViewById(R.id.backButton).setOnClickListener(view -> finish());
        findViewById(R.id.callButton).setOnClickListener(view -> dialContact());
        findViewById(R.id.deleteButton).setOnClickListener(view -> confirmDelete());
    }

    private void setText(int id, String value) {
        ((TextView) findViewById(id)).setText(value);
    }

    private void dialContact() {
        Contact contact = repository().findById(contactId);
        if (contact == null) {
            finish();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", contact.getPhone(), null));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, R.string.no_dialer, Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        Contact contact = repository().findById(contactId);
        if (contact == null) {
            finish();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_contact)
                .setMessage(getString(R.string.confirm_delete, contact.getFullName()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    repository().delete(contactId);
                    Toast.makeText(this, R.string.contact_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }
}
