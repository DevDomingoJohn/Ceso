package com.domindev.ceso.core.util

import android.content.ContentResolver
import android.net.Uri
import com.domindev.ceso.data.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

suspend fun readZipFile(
    contentResolver: ContentResolver,
    zipUri: Uri
): List<Notes> = withContext(Dispatchers.IO) {
    val files = mutableListOf<Notes>()

    contentResolver.openInputStream(zipUri)?.use { inputStream ->
        ZipInputStream(inputStream).use { zipStream ->
            var entry: ZipEntry? = zipStream.nextEntry

            while (entry != null) {
                if (!entry.isDirectory && entry.name.endsWith(".txt")) {
                    val title = entry.name
                        .removeSuffix(".txt")
                        .replace("_", " ")  // Reverse filename sanitization

                    val content = buildString {
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (zipStream.read(buffer).also { bytesRead = it } != -1) {
                            append(buffer.decodeToString(0, bytesRead))
                        }
                    }

                    files.add(Notes(title = title, description = content))
                }
                entry = zipStream.nextEntry
            }
        }
    }
    return@withContext files
}