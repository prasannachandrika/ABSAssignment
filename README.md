User List App
This Android application displays a list of users by fetching data from an API. Each user is presented with their profile picture, name, and address (country, state, city). Tapping on a user item navigates to a detailed screen that shows additional information about the selected user, including their email, phone number, age, and gender.

Features:

Fetches user data from a remote API.
Displays a list of users with their profile pictures and basic details.
Navigates to a details screen showing comprehensive user information.
Implements Clean Architecture for better separation of concerns and testability.
Utilizes Hilt for dependency injection to manage application dependencies.
Loads images efficiently using Coil.
Includes unit tests to ensure functionality and correctness.

----------------------
Architecture:

This project follows the Clean Architecture principles, which organizes the codebase into distinct layers:

Presentation Layer: Handles the UI components, including activities and fragments. This layer interacts with the ViewModel to observe data and respond to user actions.

Domain Layer: Contains the business logic and use cases. This layer is independent of any framework and holds the core functionality of the application.

Data Layer: Responsible for data management, including remote data fetching using Retrofit and local data persistence if needed. This layer interacts with the API and provides data to the domain layer.

--------------------
Technologies Used:

Android SDK: For building Android applications.
Kotlin: The programming language used in this project.
Retrofit: For making network calls to fetch user data from the API.
Coil: For efficient image loading and caching.
Dagger Hilt: For dependency injection, simplifying the management of dependencies.
JUnit4: For unit testing.
Prerequisites
Before you begin, ensure you have the following installed:

Android Studio
Java JDK
Android SDK (included with Android Studio)
Gradle (included with Android Studio)

-------------------------------------------
Installation
Follow these steps to install and run the project:

Clone the repository:

bash
Copy code
git clone https://github.com/prasannachandrika/ABSAssignment.git
Open Android Studio:

Select File > New > Import Project.
Provide the path to the cloned repository.
Build the Project:

Sync the Gradle files and build the project to download the necessary dependencies.
Run the Application:

---------------------------------
Usage:
Upon launching the app, the main screen will display a list of users fetched from the API.
Each item shows the user's profile picture, name, and address.
Tap on any user to navigate to the details screen.
The details screen displays the user's complete profile, including email, phone number, age, and gender.

---------------------------------
## Screenshots
![screen_one](https://github.com/user-attachments/assets/8e45c6af-fda3-45c6-b877-57724624af6b)
![sreen_two](https://github.com/user-attachments/assets/30b2cacf-eb42-4460-a110-ecb34450b583)

