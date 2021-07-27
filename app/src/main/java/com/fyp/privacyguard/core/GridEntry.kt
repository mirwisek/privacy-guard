package com.fyp.privacyguard.core

import com.fyp.privacyguard.R

sealed class GridEntry(
    open val title: String = "",
    open val background: String = "2196F3",
    open val icon: Int = -1
) {


    data class CheckLogs(
        override val title: String = "Check Logs",
        override val background: String = "#2196F3",
        override val icon: Int = R.drawable.ic_logs
    ) : GridEntry()

    data class ChangeInterval(
        override val title: String = "Change Interval",
        override val background: String = "#00BCD4",
        override val icon: Int = R.drawable.ic_change_interval
    ) : GridEntry()

    data class FingerRecognition(
        override val title: String = "Finger Recognition",
        override val background: String = "#FF5722",
        override val icon: Int = R.drawable.ic_finger_registration
    ) : GridEntry()

    data class FaceRecognition(
        override val title: String = "Face Recognition",
        override val background: String = "#9C27B0",
        override val icon: Int = R.drawable.ic_face_recognition
    ) : GridEntry()

    data class Logout(
        override val title: String = "Logout",
        override val background: String = "#E91E63",
        override val icon: Int = R.drawable.ic_logout
    ) : GridEntry()

    data class Settings(
        override val title: String = "Settings",
        override val background: String = "#018786",
        override val icon: Int = R.drawable.ic_settings
    ) : GridEntry()


}
