package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public final class MainActivity extends BaseActivity {
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_main);
        ListView list = findViewById(R.id.contactList);
        adapter = new ContactAdapter(this);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.emptyState));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, id);
            startActivity(intent);
        });
        findViewById(R.id.addContactButton).setOnClickListener(view ->
                startActivity(new Intent(this, AddContactActivity.class)));
    }

    /** Mọi đường quay về đều cập nhật cùng nguồn ArrayList và thống kê. */
    @Override
    protected void onResume() {
        super.onResume();
        adapter.submitContacts(repository().getContacts());
        ((TextView) findViewById(R.id.totalCount))
                .setText(String.valueOf(repository().getTotalCount()));
        ((TextView) findViewById(R.id.favoriteCount))
                .setText(String.valueOf(repository().getFavoriteCount()));
    }
}
