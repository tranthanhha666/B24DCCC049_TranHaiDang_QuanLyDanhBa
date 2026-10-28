package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public final class ContactAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<Contact> contacts = new ArrayList<>();

    public ContactAdapter(Context context) { inflater = LayoutInflater.from(context); }

    public void submitContacts(ArrayList<Contact> items) {
        contacts.clear();
        contacts.addAll(items);
        notifyDataSetChanged();
    }

    @Override public int getCount() { return contacts.size(); }
    @Override public Contact getItem(int position) { return contacts.get(position); }
    @Override public long getItemId(int position) { return getItem(position).getId(); }
    @Override public boolean hasStableIds() { return true; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.item_contact, parent, false);
            holder = new Holder(row);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }
        Contact contact = getItem(position);
        holder.avatar.setText(contact.getInitial());
        holder.name.setText(contact.getFullName());
        holder.phone.setText(contact.getPhone());
        holder.group.setText(contact.getGroup());
        holder.star.setVisibility(contact.isFavorite() ? View.VISIBLE : View.INVISIBLE);
        return row;
    }

    private static final class Holder {
        final TextView avatar, name, phone, group, star;

        Holder(View row) {
            avatar = row.findViewById(R.id.contactAvatar);
            name = row.findViewById(R.id.contactName);
            phone = row.findViewById(R.id.contactPhone);
            group = row.findViewById(R.id.contactGroup);
            star = row.findViewById(R.id.contactStar);
        }
    }
}
