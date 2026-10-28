package com.example.myapplication;

import java.util.ArrayList;

/** Nguồn dữ liệu duy nhất, chỉ tồn tại trong bộ nhớ khi tiến trình còn chạy. */
public final class ContactRepository {
    private final ArrayList<Contact> contacts = new ArrayList<>();
    private long nextId = 1;

    public static ContactRepository createDefault() {
        ContactRepository repository = new ContactRepository();
        repository.add("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, "");
        repository.add("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "");
        repository.add("Lê Minh Châu", "0987654321", "Công việc", "Nam", false, "");
        return repository;
    }

    public ArrayList<Contact> getContacts() { return new ArrayList<>(contacts); }

    public Contact findById(long id) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) return contact;
        }
        return null;
    }

    public boolean containsPhone(String phone) {
        if (phone == null) return false;
        for (Contact contact : contacts) {
            if (contact.getPhone().equals(phone.trim())) return true;
        }
        return false;
    }

    public Contact add(String name, String phone, String group, String gender,
                       boolean favorite, String notes) {
        if (!ContactValidator.isValidName(name) || !ContactValidator.isValidPhone(phone)) {
            throw new IllegalArgumentException("Thông tin liên hệ không hợp lệ");
        }
        if (containsPhone(phone)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại trong danh bạ");
        }
        Contact contact = new Contact(nextId++, name.trim(), phone.trim(), group,
                gender, favorite, notes == null ? "" : notes.trim());
        contacts.add(contact);
        return contact;
    }

    public boolean setFavorite(long id, boolean favorite) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == id) {
                contacts.set(i, contacts.get(i).withFavorite(favorite));
                return true;
            }
        }
        return false;
    }

    public boolean delete(long id) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == id) {
                contacts.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getTotalCount() { return contacts.size(); }

    public int getFavoriteCount() {
        int count = 0;
        for (Contact contact : contacts) if (contact.isFavorite()) count++;
        return count;
    }
}
