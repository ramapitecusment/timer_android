# Timer

**Dependencies:**
```
dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'com.android.support:appcompat-v7:30.0.0'
    implementation 'com.android.support:preference-v7:30.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.1'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
}
```

menu.xml is stored in **menu** derectory.

All images are store in **drawable**.

All timer sounds are stored in **raw**.

Project uses **fragments** that helps to create settings activity.

All changes in SettingsActiivty are listened and impleneted in MainMenu with closing the Application.

PreferenceScreen stores CheckBoxPreference, ListPreference, EditTextPreference.

![alt text](https://raw.githubusercontent.com/ramapitecusment/timer_android/master/images/1.PNG)
![alt text](https://raw.githubusercontent.com/ramapitecusment/timer_android/master/images/2.PNG)
![alt text](https://raw.githubusercontent.com/ramapitecusment/timer_android/master/images/3.PNG)
![alt text](https://raw.githubusercontent.com/ramapitecusment/timer_android/master/images/4.PNG)