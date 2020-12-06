package com.imb.jobtop.fragments.resume

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.GetPhotoActivity
import com.imb.jobtop.R
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.User
import com.imb.jobtop.utils.HawkUtils
import com.imb.jobtop.utils.PermissionHandler
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import com.imb.jobtop.utils.verticalRotate
import com.nabinbhandari.android.permissions.Permissions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.nameEditText
import me.echodev.resizer.Resizer
import java.io.File


class FragmentProfile : BaseFragment(R.layout.fragment_profile) {

    private var isPhotoLoading = false
    private val GALLERY = 1001
    private val CAMERA = 2001
    private val PICK_PDF_CODE = 3001

    private lateinit var auth: FirebaseAuth
    private val db = lazy { Firebase.firestore }.value
    private var user = lazy { HawkUtils.user }.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initFirebase()
    }

    private fun initFirebase() {
        auth = Firebase.auth
        user.id = auth.currentUser?.uid
        user.email = auth.currentUser?.email

        initView()
    }

    private fun initView() {
        user.id?.let {
            db.collection("users")
                .document(it).get().addOnSuccessListener { doc ->
                    val u = doc.toObject<User>()
                    println("Debugging123 - $u")
                    user.interests = u?.interests
                    user.name = u?.name
                    nameEditText.setText(user.name ?: "")
                    mailEditText.setText(user.email ?: "")
                    name.text = user.name ?: ""
                }

        }

        user.bitmap?.let {
            setImageByType(it)
        }

    }

    private fun initListeners() {
        profileImg.setOnClickListener {
            initPhotoLoading()
        }

        buttonBackArrow.setOnClickListener {
            pressBack()
        }

        uploadCvBtn.setOnClickListener {
            readPdfFilesFromInternalStorage()
        }

        createCvBtn.setOnClickListener {
            findNavController().navigate(R.id.fragmentCreateUserCv)
        }

        saveBtn.setOnClickListener {

        }
    }

    private fun readPdfFilesFromInternalStorage() {
        val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
        intentPDF.setType("application/pdf")
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intentPDF, "Select Picture"), PICK_PDF_CODE)
    }

    private fun initPhotoLoading() {
        if (!isPhotoLoading) {

            val pictureDialog = AlertDialog.Builder(requireContext())
            pictureDialog.setTitle("Rasmni olish")
            val pictureDialogItems = arrayOf("Galereyadan", "Rasmga Tushirish")
            pictureDialog.setItems(pictureDialogItems) { dialog, which ->
                when (which) {
                    0 -> {
                        val handler = PermissionHandler()
                        handler.onGranted {
                            choosePhotoFromGallery()
                        }
                        context?.let {
                            Permissions.check(
                                it,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                null,
                                handler
                            )
                        }
                    }
                    1 -> {
                        val permissions = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        context?.let {
                            Permissions.check(
                                it,
                                permissions,
                                null/*options*/,
                                null,
                                object : com.nabinbhandari.android.permissions.PermissionHandler() {
                                    override fun onGranted() {
                                        dispatchTakePictureIntent()
                                    }
                                })
                        }

                    }
                }
            }
            pictureDialog.show()
        }
    }

    private fun choosePhotoFromGallery() {
        try {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            context?.let {
                Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(context, GetPhotoActivity::class.java)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {

        if (resultCode != Activity.RESULT_OK) {
            Log.d("TTT", "resultCode != Activity.RESULT_OK")
            return
        }
        if (requestCode == PICK_PDF_CODE) {
            val handler = Handler()
            progressOn()
            handler.postDelayed({
                progressOff()
                Snackbar.make(
                    requireView(),
                    "Rezyumingiz qabul qilindi, Javobi tez orada sizning emailingizga yuboriladi!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }, 5000)
        }
        if (requestCode == CAMERA) {
            val message = dataIntent?.getStringExtra("filepath");
            dataIntent?.removeExtra("filepath")

            Log.d("TTT", "filepath :$message")
            if (!message.isNullOrEmpty()) {
                try {
                    val image = File(message)
                    val bitmap = Resizer(context)
                        .setTargetLength(1080)
                        .setSourceImage(image)
                        .resizedBitmap
                    setPhoto(bitmap)
                    return
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    context?.let {
                        Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
            }
        } else if (requestCode == GALLERY) {
            if (resultCode != Activity.RESULT_OK) {
                Log.d("TTT", "resultCode != Activity.RESULT_OK")
                return
            }
            val contentURI = dataIntent?.data
            if (contentURI == null) {

            } else {
                context?.let {
                    val fs = getRealPathFromURI(it, contentURI)
                    if (fs != "") {
                        val file = File(fs)
                        val bitmap = Resizer(context)
                            .setTargetLength(1080)
                            .setSourceImage(file)
                            .resizedBitmap
                        setPhoto(bitmap)
                    }
                }
            }
        }
        dataIntent?.data = null
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA);
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return ""
        } finally {
            cursor?.close()
        }
    }

    private fun setPhoto(bitmap: Bitmap?) {
        isPhotoLoading = true
        val bit = bitmap?.verticalRotate()
        user.bitmap = bit

//        loadBitmapOnServer(currentPhotoType, bit) {
//            isPhotoLoading = false
//            setImageByType(currentPhotoType, bit)
//            nextBtn.isEnabled = checkUploadings()
//        }

        isPhotoLoading = false
        setImageByType(bit)
    }


    private fun setImageByType(bit: Bitmap?) {
        setImage(profileImg, bit)
    }

    private fun setImage(image: ImageView, bit: Bitmap?) {
        image.setImageBitmap(bit)
        Glide.with(requireContext())
            .load(bit)
//            .apply(RequestOptions().transform(RoundedCorners(8.toPx(requireContext()).toInt())))
            .into(image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HawkUtils.user = user
    }
}