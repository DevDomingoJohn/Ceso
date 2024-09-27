package com.domindev.ceso.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun readTextFileFromUri(
    context: Context,
    uri: Uri
): String? {
    // Read the file from the URI (assuming the URI was obtained correctly)
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { it.readText() }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            // Get the display name column index
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex).replace(".txt","")
            }
        }
    }
    return fileName
}