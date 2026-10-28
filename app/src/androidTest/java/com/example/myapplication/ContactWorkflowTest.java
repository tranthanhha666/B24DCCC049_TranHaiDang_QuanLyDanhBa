package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ContactWorkflowTest {
    @Rule public ActivityScenarioRule<LoginActivity> loginRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before public void resetContacts() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            ContactRepository repo = ((ContactsApplication)
                    InstrumentationRegistry.getInstrumentation().getTargetContext()
                            .getApplicationContext()).getRepository();
            for (Contact contact : repo.getContacts()) repo.delete(contact.getId());
            for (Contact contact : ContactRepository.createDefault().getContacts()) {
                repo.add(contact.getFullName(), contact.getPhone(), contact.getGroup(),
                        contact.getGender(), contact.isFavorite(), contact.getNotes());
            }
        });
    }

    @Test public void completeWorkflowAndCaptureReportScreenshots() throws Exception {
        screenshot("01_login");
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.usernameInput)).check(matches(hasErrorText(
                "Vui lòng nhập tên đăng nhập")));
        onView(withId(R.id.passwordInput)).check(matches(hasErrorText(
                "Vui lòng nhập mật khẩu")));
        onView(withId(R.id.usernameInput)).perform(replaceText("admin"));
        onView(withId(R.id.passwordInput)).perform(replaceText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.loginError)).check(matches(withText(
                "Sai tên đăng nhập hoặc mật khẩu")));
        screenshot("02_login_error");
        onView(withId(R.id.passwordInput)).perform(replaceText("123456"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.totalCount)).check(matches(withText("3")));
        onView(withId(R.id.favoriteCount)).check(matches(withText("1")));
        screenshot("03_contacts");
        onView(withId(R.id.addContactButton)).perform(click());
        onView(withId(R.id.genderMale)).check(matches(isChecked()));
        screenshot("04_add_top");
        onView(withId(R.id.saveButton)).perform(scrollTo(), click());
        androidx.test.espresso.Espresso.closeSoftKeyboard();
        onView(withId(R.id.nameInput)).check(matches(hasErrorText("Vui lòng nhập họ và tên")));
        onView(withId(R.id.phoneInput)).check(matches(hasErrorText(
                "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")));
        screenshot("05_add_error");
        onView(withId(R.id.nameInput)).perform(scrollTo(), replaceText("Phạm Minh Anh"));
        onView(withId(R.id.phoneInput)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(scrollTo(), click());
        androidx.test.espresso.Espresso.closeSoftKeyboard();
        onView(withId(R.id.phoneInput)).check(matches(hasErrorText(
                "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")));
        onView(withId(R.id.phoneInput)).perform(scrollTo(), replaceText("0901234567"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(scrollTo(), click());
        androidx.test.espresso.Espresso.closeSoftKeyboard();
        onView(withId(R.id.phoneInput)).check(matches(hasErrorText(
                "Số điện thoại đã tồn tại trong danh bạ")));
        screenshot("06_duplicate_phone");
        onView(withId(R.id.phoneInput)).perform(scrollTo(), replaceText("0923456789"), closeSoftKeyboard());
        onView(withId(R.id.groupSpinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("Công việc"))).perform(click());
        onView(withId(R.id.genderFemale)).perform(scrollTo(), click());
        onView(withId(R.id.favoriteInput)).perform(scrollTo(), click());
        onView(withId(R.id.notesInput)).perform(scrollTo(), replaceText("Bạn cùng lớp"), closeSoftKeyboard());
        onView(withId(R.id.backButton)).perform(scrollTo());
        onView(withId(R.id.phoneInput)).check(matches(hasErrorText(nullValue(String.class))));
        screenshot("07_add_filled_top");
        onView(withId(R.id.saveButton)).perform(scrollTo());
        screenshot("08_add_filled_bottom");
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.totalCount)).check(matches(withText("4")));
        onView(withId(R.id.favoriteCount)).check(matches(withText("2")));
        onView(withText("Phạm Minh Anh")).check(matches(isDisplayed()));
        screenshot("09_after_add");
        onData(anything()).inAdapterView(withId(R.id.contactList)).atPosition(3).perform(click());
        onView(withId(R.id.detailName)).check(matches(withText("Phạm Minh Anh")));
        onView(withId(R.id.detailGroup)).check(matches(withText("Công việc")));
        onView(withId(R.id.detailGender)).check(matches(withText("Nữ")));
        onView(withId(R.id.detailNotes)).check(matches(withText("Bạn cùng lớp")));
        onView(withId(R.id.detailFavorite)).check(matches(isChecked()));
        onView(withId(R.id.backButton)).perform(scrollTo());
        screenshot("10_detail");
        onView(withId(R.id.detailFavorite)).perform(scrollTo(), click());
        onView(withId(R.id.backButton)).perform(scrollTo(), click());
        onView(withId(R.id.favoriteCount)).check(matches(withText("1")));
        screenshot("11_after_favorite");
        onData(anything()).inAdapterView(withId(R.id.contactList)).atPosition(3).perform(click());

        onView(withId(R.id.deleteButton)).perform(scrollTo(), click());
        screenshot("13_delete_confirmation");
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.detailName)).check(matches(withText("Phạm Minh Anh")));
        onView(withId(R.id.deleteButton)).perform(scrollTo(), click());
        onView(withText(R.string.delete)).perform(click());
        onView(withId(R.id.totalCount)).check(matches(withText("3")));
        onView(withId(R.id.favoriteCount)).check(matches(withText("1")));
        screenshot("14_after_delete");

        // Mở ứng dụng ngoài ở cuối luồng để không phụ thuộc back stack của Dialer.
        onData(anything()).inAdapterView(withId(R.id.contactList)).atPosition(0).perform(click());
        Intents.init();
        try {
            onView(withId(R.id.callButton)).perform(scrollTo(), click());
            intended(allOf(hasAction(Intent.ACTION_DIAL), hasData("tel:0901234567")));
            InstrumentationRegistry.getInstrumentation().getUiAutomation().waitForIdle(800, 5000);
            screenshot("12_dialer");
        } finally {
            Intents.release();
        }
    }

    @Test public void formRetainsInputAcrossActivityRecreation() {
        try (ActivityScenario<AddContactActivity> scenario = ActivityScenario.launch(AddContactActivity.class)) {
            onView(withId(R.id.nameInput)).perform(replaceText("Lê Hồng Hà"));
            onView(withId(R.id.phoneInput)).perform(replaceText("0933333333"), closeSoftKeyboard());
            onView(withId(R.id.genderFemale)).perform(scrollTo(), click());
            onView(withId(R.id.favoriteInput)).perform(scrollTo(), click());
            onView(withId(R.id.notesInput)).perform(scrollTo(), replaceText("Ghi chú giữ nguyên"), closeSoftKeyboard());
            scenario.recreate();
            onView(withId(R.id.nameInput)).check(matches(withText("Lê Hồng Hà")));
            onView(withId(R.id.phoneInput)).check(matches(withText("0933333333")));
            onView(withId(R.id.genderFemale)).check(matches(isChecked()));
            onView(withId(R.id.favoriteInput)).check(matches(isChecked()));
            onView(withId(R.id.notesInput)).check(matches(withText("Ghi chú giữ nguyên")));
        }
    }

    @Test public void invalidContactIdFinishesWithoutCrash() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, Long.MAX_VALUE);
        try (ActivityScenario<ContactDetailActivity> scenario = ActivityScenario.launch(intent)) {
            assertEquals(androidx.lifecycle.Lifecycle.State.DESTROYED, scenario.getState());
        }
    }

    @Test public void emptyListDisplaysHelpfulMessage() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            ContactRepository repo = ((ContactsApplication)
                    InstrumentationRegistry.getInstrumentation().getTargetContext()
                            .getApplicationContext()).getRepository();
            for (Contact contact : repo.getContacts()) repo.delete(contact.getId());
        });
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.totalCount)).check(matches(withText("0")));
            onView(withId(R.id.favoriteCount)).check(matches(withText("0")));
            onView(withId(R.id.emptyState)).check(matches(isDisplayed()));
            onView(withText(R.string.empty_contacts)).check(matches(isDisplayed()));
        }
    }

    private void screenshot(String name) throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();
        // Đợi frame cuối và Toast kết thúc để ảnh báo cáo không che nội dung.
        instrumentation.getUiAutomation().waitForIdle(800, 5000);
        Bitmap bitmap = instrumentation.getUiAutomation().takeScreenshot();
        assertNotNull("Không lấy được ảnh màn hình", bitmap);
        File directory = new File(instrumentation.getTargetContext().getExternalFilesDir(null), "screenshots");
        assertTrue(directory.isDirectory() || directory.mkdirs());
        try (FileOutputStream output = new FileOutputStream(new File(directory, name + ".png"))) {
            assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output));
        } finally {
            bitmap.recycle();
        }
        // Giữ bằng chứng sau khi Gradle gỡ APK kiểm thử; thao tác shell chỉ nằm trong test.
        String archive = "/sdcard/Download/quanlydanhba-evidence";
        String source = new File(directory, name + ".png").getAbsolutePath();
        for (String command : new String[]{"mkdir -p " + archive, "cp " + source + " " + archive + "/"}) {
            try (ParcelFileDescriptor descriptor = instrumentation.getUiAutomation().executeShellCommand(command);
                 java.io.InputStream input = new ParcelFileDescriptor.AutoCloseInputStream(descriptor)) {
                while (input.read() != -1) { /* Đợi lệnh hoàn thành. */ }
            }
        }
    }
}
