import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.gallery_picker_plugin.MainActivity
import io.flutter.plugin.common.MethodChannel
import java.util.*

class MediaPickerHandler(private val activity: MainActivity, private val channel: MethodChannel) {

    private val REQUEST_CODE_PICK_IMAGE = 456
    private val REQUEST_CODE_PICK_VIDEO = 789
    private val REQUEST_CODE_PICK_FILE = 101
    private var mediaType: MediaType? = null
    private var pickerType: PickerType? = null
    private var callback: MethodChannel.Result? = null

    // Optional parameters
    private var maxWidth: Int? = null
    private var maxHeight: Int? = null
    private var imageQuality: Int? = null
    private var maxDuration: Int? = null
    private var maxSize: Long? = null
    private var maxItemCount: Int? = null
    private var fileTypes: List<String>? = null // New option for AnyType

    enum class MediaType {
        PDF, Image, AnyType, Video, OtherType
    }

    enum class PickerType {
        Gallery, File
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    fun pickMedia(
        type: String,
        pickerType: String,
        options: Map<String, Any>?,
        result: MethodChannel.Result
    ) {
        this.pickerType = PickerType.valueOf(pickerType)
        this.mediaType = MediaType.valueOf(type)
        callback = result
        val myActive:Activity = activity as Activity
        // Handle optional parameters
        options?.let {
            fileTypes = it["fileTypes"] as? List<String> // Retrieve fileTypes from options
        }

        when (this.mediaType) {
            MediaType.Image -> pickImage(myActive,false)
            MediaType.Video -> pickImage(myActive,true)

            else -> pickFile(myActive)
        }
    }


    // done
    fun pickImage(activity: Activity,isVideo: Boolean) {
        val selectedFiles = mutableListOf<String>()
        if (isVideo){
            // video
            if (this.pickerType == PickerType.Gallery) {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                if (gallery.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(gallery, REQUEST_CODE_PICK_VIDEO)
                }
            } else {
                val pickFile = Intent(Intent.ACTION_GET_CONTENT)
                pickFile.type = "video/*"
                if (pickFile.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(pickFile, REQUEST_CODE_PICK_VIDEO)
                }

            }

        }else{
            // image
            if (this.pickerType == PickerType.Gallery) {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (gallery.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(gallery, REQUEST_CODE_PICK_IMAGE)
                }
            } else {
                val pickFile = Intent(Intent.ACTION_GET_CONTENT)
                pickFile.type = "image/*"
                if (pickFile.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(pickFile, REQUEST_CODE_PICK_IMAGE)
                }

            }
        }
    }
    //
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun pickFile(activity: Activity){
        val selectedFiles = mutableListOf<String>()
        when(this.mediaType) {
            // pdf
            MediaType.PDF -> {
                val pickFile = Intent(Intent.ACTION_GET_CONTENT)
                pickFile.type = "application/pdf"
                if (pickFile.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(pickFile, REQUEST_CODE_PICK_FILE)
                }
            }
            // end pdf

            // anyType
            MediaType.AnyType -> {
                val pickFile = Intent(Intent.ACTION_GET_CONTENT)
                pickFile.type = "*/*"

                if (pickFile.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(pickFile, REQUEST_CODE_PICK_FILE)
                }
            }
            // end anyType
            MediaType.OtherType ->{
                val pickFile = Intent(Intent.ACTION_GET_CONTENT)
                pickFile.type = "*/*"

                fileTypes?.let { types ->
                    if (types.isNotEmpty()) {
                        // Convert the list of MIME types to an array and add it to the Intent
                        pickFile.putExtra(Intent.EXTRA_MIME_TYPES, types.toTypedArray())
                    }
                }
                if (pickFile.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(pickFile, REQUEST_CODE_PICK_FILE)
                }
            }
            else -> {}
        }
    }
    //
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode == Activity.RESULT_OK) {

            data?.data?.let { uri ->
                val thisFilePath = getRealPathFromURI(uri)
                callback?.success(thisFilePath)
            } ?: run {
                // Handle the case when the user cancels the image picker
                callback?.error("File picking canceled", null, null)
            }

    }}

    //
    private fun getRealPathFromURI(uri: Uri): String {
        val context: Context = activity as Activity
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()

        return path ?: ""
    }
}
