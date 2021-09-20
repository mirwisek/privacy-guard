package com.fyp.privacyguard.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.fyp.privacyguard.R
import com.fyp.privacyguard.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PhoneLogService: LifecycleService() {

    companion object {
        private const val NOTIFICATION_ID = 1232
        const val EXTRA_SHOULD_STOP_SERVICE = "should.stop.service"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val TAG = "PhoneLogService"

        const val ACTION_PASSWORD_FAILED = "phone.log.password.failed"
        const val ACTION_PASSWORD_SUCCEEDED = "phone.log.password.succeeded"
    }

    private lateinit var notificationManager: NotificationManager

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService


    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(NOTIFICATION_ID, generateNotification())
        startForeground(NOTIFICATION_ID, generateNotification())

        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        intent?.let { mIntent ->
            when(mIntent.action) {
                ACTION_PASSWORD_FAILED -> {
                    startCamera()
                    takePhoto()
                }
                ACTION_PASSWORD_SUCCEEDED -> {
                    startCamera()
                    takePhoto()
                }
            }
        }

        val shouldStopService =
            intent?.getBooleanExtra(EXTRA_SHOULD_STOP_SERVICE, false) ?: false

        if(shouldStopService) {
            toast("Stopping service")
            stopForeground(true)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    toast("Image capture failed")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    toast(msg)
                    Log.d(TAG, msg)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

//            // Preview
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(viewFinder.surfaceProvider)
//                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

}