package com.example.zap_picker

import MediaPickerHandler
import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val MEDIA_PICKER_CHANNEL = "MediaPicker"

    private val mediaPickerHandler: MediaPickerHandler by lazy {
        MediaPickerHandler(this, MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, MEDIA_PICKER_CHANNEL))
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, MEDIA_PICKER_CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                when (call.method) {
                    "pickMedia" -> {

                        val type = call.argument<String>("mediaType")
                        val pickerType = call.argument<String>("pickerType")
                        val optionsType = call.argument<Map<String, Any>>("options")
                        if (type != null && pickerType != null) {
                            PermissionHandler(this,type!!).requestFileAccess()
                            mediaPickerHandler.pickMedia(type, pickerType, optionsType, result)
                        } else {
                            result.error("ARGUMENT_ERROR", "Invalid arguments for pickMedia", null)
                        }
                    }

                    else -> {
                        result.error("ARGUMENT_ERROR", "Invalid arguments", null)
                        result.notImplemented()
                    }
                }
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaPickerHandler.handleActivityResult(requestCode, resultCode, data)

    }
}
