# MAD204 – Assignment 3  
## Notes & Media Manager App

**Student Name:** Ashish Prajapati  
**Student ID:** A00194842  

---

## Overview

This Android app lets the user create notes, mark favorites, attach images, and manage simple backups.  
It is built with Kotlin, min SDK 30, and uses Room, SharedPreferences, internal storage, a background service, and GSON JSON.

---

## Main Features

- **User preferences**
  - Save username and dark/light theme using SharedPreferences.
  - Reload saved username and theme every time the app starts.

- **Notes with Room (SQLite)**
  - Note entity: `id`, `title`, `content`, optional `mediaUri`.
  - Full CRUD (create, read, update, delete) with Room DAO.
  - Notes shown in a RecyclerView list.

- **Media picker**
  - Attach an image to a note using `ActivityResultContracts.GetContent`.
  - Show the selected image inside the note row.

- **Favorites**
  - Separate `Favorite` Room table for favorite notes.
  - Favorite checkbox on each note.
  - Favorites list displayed in another RecyclerView.

- **JSON export / import**
  - Export all notes to JSON using GSON helper.
  - JSON string logged to Logcat and can be used to restore notes.

- **File storage backup**
  - Save all note titles into `backup.txt` in internal storage.
  - Load the file and show its content in the main screen.

- **Reminder service + notification**
  - Background Service waits a few seconds.
  - Sends a notification: “Don’t forget your notes!”.
  - Tapping the notification opens the app.

---

## How to Run

1. Open the project in Android Studio (Android Gradle Plugin 8+, Kotlin).  
2. Sync Gradle and build the project.  
3. Run on an emulator or device with API 30 or higher.  
4. Use the main screen to:
   - Enter username, choose theme, tap **Save preferences**.  
   - Tap **Add note** to create notes; use **Edit**, **Delete**, **Media**, and **Favorite** on each row.  
   - Tap **Export JSON** to log all notes as JSON.  
   - Tap **Import JSON** to restore from the last saved JSON (simple implementation).  
   - Tap **Save backup.txt** and **Load backup.txt** to test file storage.  
   - Tap **Start reminder** and wait a few seconds to see the notification.

