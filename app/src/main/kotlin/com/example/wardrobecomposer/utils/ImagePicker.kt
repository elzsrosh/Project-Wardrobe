@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.wardrobecomposer.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ImagePicker {
    @Composable
    fun rememberImagePicker(onImageSelected: (Uri) -> Unit): Pair<Intent, () -> Unit> {
        val context = LocalContext.current
        val activity = context as Activity

        // Создаем временный файл для хранения фото
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir("images")
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        val imageUri =
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile,
            )

        // Intent для выбора фото из галереи
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .apply { type = "image/*" }

        // Intent для съемки фото
        val cameraIntent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .apply { putExtra(MediaStore.EXTRA_OUTPUT, imageUri) }

        // Intent для выбора способа загрузки
        val chooserIntent =
            Intent
                .createChooser(galleryIntent, "Выберите изображение")
                .apply { putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent)) }

        return remember {
            Pair(
                chooserIntent,
                { activity.startActivityForResult(chooserIntent, REQUEST_CODE_IMAGE_PICKER) },
            )
        }
    }

    fun handleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        context: Context,
        onImageSelected: (Uri) -> Unit,
    ) {
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            onImageSelected(uri)
        }
    }

    private const val REQUEST_CODE_IMAGE_PICKER = 1001
}
