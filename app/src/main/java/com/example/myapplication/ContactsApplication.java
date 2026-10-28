package com.example.myapplication;

import android.app.Application;

public final class ContactsApplication extends Application {
    private final ContactRepository repository = ContactRepository.createDefault();

    public ContactRepository getRepository() { return repository; }
}
