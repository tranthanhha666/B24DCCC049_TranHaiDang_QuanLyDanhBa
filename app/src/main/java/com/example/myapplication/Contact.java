package com.example.myapplication;

/** Một liên hệ. Dữ liệu bất biến; thay đổi yêu thích thông qua repository. */
public final class Contact {
    private final long id;
    private final String fullName;
    private final String phone;
    private final String group;
    private final String gender;
    private final boolean favorite;
    private final String notes;

    public Contact(long id, String fullName, String phone, String group,
                   String gender, boolean favorite, String notes) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.notes = notes;
    }

    public long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getGroup() { return group; }
    public String getGender() { return gender; }
    public boolean isFavorite() { return favorite; }
    public String getNotes() { return notes; }

    public String getInitial() {
        String name = fullName.trim();
        int start = name.lastIndexOf(' ') + 1;
        return name.substring(start, name.offsetByCodePoints(start, 1))
                .toUpperCase(java.util.Locale.forLanguageTag("vi"));
    }

    public Contact withFavorite(boolean value) {
        return new Contact(id, fullName, phone, group, gender, value, notes);
    }
}
