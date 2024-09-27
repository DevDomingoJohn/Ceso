package com.domindev.ceso.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.OutputStream

fun exportNote(
    context: Context,
    title: String,
    description: String
) {
    val fileName = title
    val fileContents = description

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME,fileName)
            put(MediaStore.Downloads.MIME_TYPE,"text/plain")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            try {
                // Get the OutputStream to write into the file
                val outputStream: OutputStream? = contentResolver.openOutputStream(it)
                outputStream?.use { stream ->
                    stream.write(fileContents.toByteArray())
                }
                Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error saving file", Toast.LENGTH_LONG).show()
            }
        }
    } else {
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + fileName
        val file = File(filePath)

        try {
            file.writeText(fileContents)
            Toast.makeText(context, "File saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving file", Toast.LENGTH_LONG).show()
        }
    }

}