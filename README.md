# Sweet Code - Android App Project (In Progress)
> _This README is written with the help of ChatGPT_

<p align="justify">
<img src="https://github.com/ashwin-madhavan/sweet-code/blob/main/screenshots/Sugar%20-%20App%20Icon.png?raw=true" width="190px" height=auto align="right" alt="Computador"/>

When preparing for a CS interview, it's important to set aside focused time to really understand the different topics that might come up. This app helps you do just that! You can establish hourly goals for each interview topic, log each timed study session, and track your progress towards achieving those goals.

<strong>Key Features of this Android App:</strong>
  <ul>
    <li>
      Goal Setting/Gamification: Allows users to define specific target study hour goals for each interview question category.
    </li>
    <li>
      AI-Powered Goal Setting: Utilizes OpenAI's API to recommend optimal time allocation for interview preparation based on contextual input.
    </li>
  </ul>
  
Overall, SweetCode is an open-source Android application built using Jetpack Compose and Kotlin programming language. The app is designed to be easy to use and gamified, allowing users to stay engaged and track their progress on their journey to be interview ready.
</p>

## Screenshots

<p>
<img src="https://github.com/ashwin-madhavan/sweet-code/blob/main/screenshots/Home%20Page.png?raw=true" height="550px"/>
<img src="https://github.com/ashwin-madhavan/sweet-code/blob/main/screenshots/Adjust%20Goal%20Hours%20Dialog.png?raw=true" height="550px"/>  
<img src="https://github.com/ashwin-madhavan/sweet-code/blob/main/screenshots/Log%20Entry%20Page.jpg?raw=true" height="550px"/>
<img src="https://github.com/ashwin-madhavan/sweet-code/blob/main/screenshots/Add%20Log%20Entry%20Dialog.png?raw=true" height="550px"/>
</p>


## Requirements
- Android Studio Arctic Fox or later
- OpenAI API Key


## Getting Started

1. Clone the repository.
2. Obtain an OpenAI API Key from the OpenAI website.
4. In the root folder local.properties file, add the following line and replace <your-api-key> with your actual API key:
api_key=<your-api-key>
5. Build and run the app on an emulator or physical device.

## Directory Structure

```terminal
.
├── app
│   ├── build.gradle
│   ├── proguard-rules.pro
│   ├── src
│   │   ├── androidTest
│   │   ├── main
│   │   │   ├── java/com/ashwinmadhavan/codecadence
│   │   │   │   ├── data
│   │   │   │   ├── screen
│   │   │   │   ├── ui
│   │   │   │   ├── constants

│   │   │   │   └── MainActivity.kt
│   │   │   │   └── Navigation.kt
│   │   │   ├── res
│   │   │   └── AndroidManifest.xml
│   │   ├── test
│   │   └── ...
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

- `constants`: This directory contains constant values used throughout the application.
- `data`: This directory contains the data layer of the application, including repositories and data sources.
- `di`: This directory contains the Dependency Injection setup for the application.
- `screen`: This directory contains ui screen components and viewmodels
- `models`: This directory contains the data models used throughout the application.
- `ui`: This directory contains UI files (Color.kt, Theme.kt, Type.kt)
- `MainActivity.kt`: This file contains the implementation of the main activity for the application.
- `Navigation.kt`: This file contains the implementation of the navigation between Activities.

## Features

- [x] View progress bar for each topic
- [x] Edit/set progress bar goal for each topic
- [x] Generate goal suggestions with ChatGPT
  - [x] text-davinci-00303, text-curie-001, text-babbage-001, text-ada-001
- [x] Enter logs of hours spent study by topic
- [x] View logs entries by latest submission to earliest
- [ ] Clean up UI
- [ ] Finish adding unit and integration tests

## Acknowledgments
- Jetpack Compose
- OkHttp Client
- Retrofit
- Room
- [OpenAI GPT-3 API](https://beta.openai.com/docs/api-reference/introduction)
