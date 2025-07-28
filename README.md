# NoteLock

NoteLock is an Android application designed to securely store your sensitive notes, account details, and passwords. It leverages biometric authentication (fingerprint) and encryption to ensure your private information is protected at all times. 

## Features

- **Biometric Authentication**: Only authorized users can access notes using fingerprint or device credentials.
- **AES Encryption**: All notes, account details, and passwords are encrypted before saving.
- **Secure Cloud Storage**: Notes are stored securely in Firebase Firestore.
- **Add, Edit, and Delete Notes**: Manage your notes with intuitive controls.
- **Error Handling**: Provides feedback for missing fields and authentication errors.
- **Timestamping**: Each note is saved along with the creation timestamp.

## Technologies Used

- **Java** (Android)
- **AndroidX Biometric**
- **Firebase Firestore**
- **AES Encryption**

## Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Naga-Aditya-Anand/NoteLock.git
   ```

2. **Open in Android Studio**
   - Import the project into Android Studio.
   - Sync Gradle and ensure all dependencies are installed.

3. **Configure Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
   - Download `google-services.json` from your project settings.
   - Place `google-services.json` in `app/` directory.

4. **Run the App**
   - Connect your Android device or use an emulator with fingerprint support.
   - Build and run the app.

## Usage

- On app launch, authenticate using your fingerprint or device credentials.
- Add new notes by entering a title, account, and password.
- Notes are encrypted and saved to Firebase.
- Edit or delete existing notes as needed.

## Screenshots

*(Add relevant screenshots here if available)*

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Author

- [Naga-Aditya-Anand](https://github.com/Naga-Aditya-Anand)

---

**Note:** This app is private and intended for personal use or demonstration. For production usage, ensure proper security review and Firebase rules configuration.
