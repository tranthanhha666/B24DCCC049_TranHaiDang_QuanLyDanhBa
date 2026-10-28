package com.example.myapplication;

import static org.junit.Assert.*;
import org.junit.Test;

public class ContactRepositoryTest {
    @Test public void seedsExactlyThreeContactsAndOneFavorite() {
        ContactRepository repo = ContactRepository.createDefault();
        assertEquals(3, repo.getTotalCount());
        assertEquals(1, repo.getFavoriteCount());
        assertEquals("Nguyễn Văn An", repo.getContacts().get(0).getFullName());
        assertEquals("0912345678", repo.getContacts().get(1).getPhone());
        assertEquals("Công việc", repo.getContacts().get(2).getGroup());
    }

    @Test public void acceptsOnlyTenDigitsStartingWithZero() {
        assertTrue(ContactValidator.isValidPhone("0901234567"));
        assertTrue(ContactValidator.isValidPhone(" 0901234567 "));
        for (String invalid : new String[]{"", "090123456", "09012345678",
                "1901234567", "+84901234567", "090 123456", "0abcdefghi",
                "０９０１２３４５６７", "090123456\n7"}) {
            assertFalse(invalid, ContactValidator.isValidPhone(invalid));
        }
        assertFalse(ContactValidator.isValidPhone(null));
    }

    @Test public void rejectsMissingName() {
        assertFalse(ContactValidator.isValidName(null));
        assertFalse(ContactValidator.isValidName(" \t\n"));
        assertTrue(ContactValidator.isValidName(" Đăng "));
    }

    @Test public void addPreservesAllFieldsAndUpdatesBothCounters() {
        ContactRepository repo = ContactRepository.createDefault();
        Contact added = repo.add("  Nguyễn Minh Anh ", " 0923456789 ", "Khác",
                "Nữ", true, " Ghi chú thử nghiệm ");
        assertEquals("Nguyễn Minh Anh", added.getFullName());
        assertEquals("0923456789", added.getPhone());
        assertEquals("Khác", added.getGroup());
        assertEquals("Nữ", added.getGender());
        assertEquals("Ghi chú thử nghiệm", added.getNotes());
        assertEquals(4, repo.getTotalCount());
        assertEquals(2, repo.getFavoriteCount());
        assertSame(added, repo.findById(added.getId()));
    }

    @Test public void duplicateCannotMutateListOrCounters() {
        ContactRepository repo = ContactRepository.createDefault();
        assertThrows(IllegalArgumentException.class, () ->
                repo.add("Trùng số", " 0901234567 ", "Khác", "Nam", true, ""));
        assertEquals(3, repo.getTotalCount());
        assertEquals(1, repo.getFavoriteCount());
    }

    @Test public void invalidInputCannotBeAddedThroughRepository() {
        ContactRepository repo = new ContactRepository();
        assertThrows(IllegalArgumentException.class, () ->
                repo.add(" ", "0901234567", "Khác", "Nam", false, ""));
        assertThrows(IllegalArgumentException.class, () ->
                repo.add("An", "123", "Khác", "Nam", false, ""));
        assertEquals(0, repo.getTotalCount());
    }

    @Test public void favoriteChangeAndDeleteUpdateCounters() {
        ContactRepository repo = ContactRepository.createDefault();
        long id = repo.getContacts().get(0).getId();
        assertTrue(repo.setFavorite(id, true));
        assertEquals(2, repo.getFavoriteCount());
        assertTrue(repo.findById(id).isFavorite());
        assertTrue(repo.setFavorite(id, true));
        assertEquals(2, repo.getFavoriteCount());
        assertTrue(repo.delete(id));
        assertEquals(2, repo.getTotalCount());
        assertEquals(1, repo.getFavoriteCount());
        assertNull(repo.findById(id));
    }

    @Test public void invalidIdsDoNotChangeData() {
        ContactRepository repo = ContactRepository.createDefault();
        assertFalse(repo.delete(-1));
        assertFalse(repo.setFavorite(Long.MAX_VALUE, true));
        assertNull(repo.findById(-1));
        assertEquals(3, repo.getTotalCount());
    }

    @Test public void listSnapshotCannotMutateRepository() {
        ContactRepository repo = ContactRepository.createDefault();
        repo.getContacts().clear();
        assertEquals(3, repo.getTotalCount());
    }

    @Test public void idsRemainStableAfterDeleteAndAdd() {
        ContactRepository repo = ContactRepository.createDefault();
        long oldId = repo.getContacts().get(0).getId();
        long remainingId = repo.getContacts().get(1).getId();
        repo.delete(oldId);
        Contact added = repo.add("Mới", "0922222222", "Khác", "Khác", false, null);
        assertTrue(added.getId() > remainingId);
        assertEquals("", added.getNotes());
        assertEquals("Trần Thị Bình", repo.findById(remainingId).getFullName());
    }

    @Test public void avatarUsesFirstLetterOfGivenName() {
        Contact contact = new Contact(1, "Nguyễn Văn An", "0901234567",
                "Bạn bè", "Nam", false, "");
        assertEquals("A", contact.getInitial());
        assertEquals("Đ", new Contact(2, "Đăng", "", "", "", false, "").getInitial());
    }

    @Test public void loginRequiresExactDemoPassword() {
        assertTrue(LoginValidator.matches("admin", "123456"));
        assertTrue(LoginValidator.matches(" admin ", "123456"));
        assertFalse(LoginValidator.matches("Admin", "123456"));
        assertFalse(LoginValidator.matches("admin", "123456 "));
        assertFalse(LoginValidator.matches("", ""));
        assertFalse(LoginValidator.matches(null, null));
    }
}
