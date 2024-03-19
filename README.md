# Code Challenge: Hostel App


## About

App created as result of the code challenge sent by Hostel World. The App display a list of possible
hostels, display the info in a List and shows the Details of the Property itself

### Architecture
MVVM + Clean Architecture

### Modules

- App: Entry point application with the definitions necessary to run the app
- Domain: Exposure of common interfaces to the modules and holds the domain Logic and implementation of use cases
- Data: Holds the logic and classes responsible to retrieve and save information in the Application
- Shared: Module used to share common modules between Android Based Libraries
- Shared-Test: Module used to share common test resources between different modules
- Features: Dedicated to hold each screen of the application


### How to Run
- Clone the code
- Open Project with Android Studio (developed and tested using: Android Studio Hedgehog | 2023.1.1 Patch 2)
- Sync the Dependencies 
- Run the application

### How to Run: Unit Tests
- Open the terminal
- Navigate to the source code root folder
- run: `./gradlew testDebugUnitTest`

OBS: The app does not contains UI/Instrumented Test

OBS 2: Although i had work with RxJava, its been a while since i've made the jump from it to kotlin.coroutines.
So i prefer to show what I know with the technology that Im more comfortable with. But i still took 
advantage of very different aspects of reactive programming in the implementation of the App (eg: communication between View and ViewModel)