# SurveyForm

A dynamic survey form Android app built with Jetpack Compose, following Clean Architecture and modern Android best practices.

## Features
- Fetches and renders surveys form dynamically from a remote API
- Supports multiple question types: multiple choice, dropdown, number input, text input, checkbox, camera
- Navigation between questions, including skip logic
- Input validation for each question type
- Collects and stores user answers locally using Room database
- Displays all survey submissions in a survey submissions result screen
- Robust error handling and loading states
- Snackbar feedback for submission success/error
- Clean, modular codebase with dependency injection

## Architecture
- **MVVM (Model-View-ViewModel)** with unidirectional data flow
- **Clean Architecture**: domain, data, and presentation layers
- **State management**: UI state and submit state are managed with Kotlin Flows and Compose state
- **Room** for local persistence of survey submissions
- **Hilt** for dependency injection
- **Kotlin Serialization** for JSON handling


## Technology & Libraries Used
- **Kotlin**
- **Jetpack Compose** (UI)
- **Room** (local database)
- **Hilt** (dependency injection)
- **Kotlinx Serialization** (JSON)
- **Retrofit** (network/API)
- **OkHttp Logging Interceptor** (network request/response logging)
- **Coil** (image loading in Compose)
- **Coroutines & Flow** (async, state management)
- **FileProvider** (for camera image URIs)

## Image Handling
- For Camera type , the app captures a photo and saves it to the app's cache directory.
- **Only the file path (URI as string) is stored in the database**, not the image data itself.
- **Coil** is used to efficiently load and display images from file paths in the Compose UI.

## Setup & Running
1. **Clone the repository**
2. Open in Android Studio (Arctic Fox or newer recommended)
3. Sync Gradle and build the project
4. Run on an emulator or real device

## Download

The final debug APK file is available on the [GitHub Releases](../../releases) page of this repository.  
You can download and install it directly on your Android device for testing.

---

**Author:**
- Md Zihad