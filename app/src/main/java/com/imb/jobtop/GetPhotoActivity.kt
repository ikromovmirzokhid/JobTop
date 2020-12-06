package com.imb.jobtop

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nabinbhandari.android.permissions.Permissions
import java.util.ArrayList

class GetPhotoActivity : AppCompatActivity() {

    private var mImageCaptureUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.baseWhite)
        }

        val permissions = arrayOf(
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        Permissions.check(
            this/*context*/,
            permissions,
            null/*options*/,
            null,
            object : com.nabinbhandari.android.permissions.PermissionHandler() {
                override fun onGranted() {
                    doTakePhotoAction()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    cancelled()
                    super.onDenied(context, deniedPermissions)
                }

                override fun onJustBlocked(
                    context: Context?,
                    justBlockedList: ArrayList<String>?,
                    deniedPermissions: ArrayList<String>?
                ) {
                    cancelled()
                    super.onJustBlocked(context, justBlockedList, deniedPermissions)
                }

                override fun onBlocked(
                    context: Context?,
                    blockedList: ArrayList<String>?
                ): Boolean {
                    cancelled()
                    return super.onBlocked(context, blockedList)
                }
            })

    }

    private fun cancelled() {
        val output = Intent()
        setResult(Activity.RESULT_CANCELED, output)
        finish()
    }


    private fun doTakePhotoAction() {
        val fileName = "temp.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        mImageCaptureUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
        startActivityForResult(intent, PICK_FROM_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            cancelled()
            return
        }

        when (requestCode) {
            PICK_FROM_CAMERA -> {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cr = contentResolver
                val cursor = mImageCaptureUri?.let { cr.query(it, projection, null, null, null) }
                val columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor?.moveToFirst()
                val capturedImageFilePath = columnIndexData?.let { cursor.getString(it) }

                // result
                val intent = Intent()
                intent.putExtra("filepath", capturedImageFilePath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else -> cancelled()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val PICK_FROM_CAMERA = 111
    }
}