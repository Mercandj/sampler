package com.mercandalli.android.apps.sampler.audio

import android.content.Context
import java.io.*

internal class NativeAudioManager(
    private val context: Context
) : AudioManager {

    private val assetsFilePaths = ArrayList<String>()

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun load(assetsFilePaths: List<String>) {
        this.assetsFilePaths.clear()
        this.assetsFilePaths.addAll(assetsFilePaths)
        val internalStoragePaths = copyAssetsOnInternalStorage(context, assetsFilePaths)
        nativeLoad(internalStoragePaths.toTypedArray())
    }

    override fun play(assetsFilePath: String) {
        val id = extractId(assetsFilePath)
        nativePlay(id)
    }

    private fun extractId(assetsFilePath: String): Int {
        for (i in 0 until assetsFilePaths.count()) {
            if (assetsFilePaths[i] == assetsFilePath) {
                return i
            }
        }
        throw IllegalStateException("Error, not found path: $assetsFilePath")
    }

    /**
     * Returns the list of internal storage paths.
     */
    private fun copyAssetsOnInternalStorage(
        context: Context,
        assetsFilePaths: List<String>): ArrayList<String> {
        val assetManager = context.applicationContext.assets
        val internalStorageFilesDirAbsolutePath = context.filesDir.absolutePath
        val result = ArrayList<String>()

        for (assetsFilePath in assetsFilePaths) {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val outFile = File(internalStorageFilesDirAbsolutePath, assetsFilePath)
                result.add(outFile.absolutePath)
                if (!outFile.exists()) {
                    val file = File(outFile.absolutePath).parentFile
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                    inputStream = assetManager.open(assetsFilePath)
                    outputStream = FileOutputStream(outFile)
                    copyFile(inputStream, outputStream)
                }
            } catch (e: IOException) {
                throw IllegalStateException("Failed to copy asset file: $assetsFilePath", e)
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                }
                try {
                    outputStream?.close()
                } catch (e: IOException) {
                }
            }
        }
        return result
    }

    private fun copyFile(inputStream: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        while (true) {
            val bytesRead = inputStream.read(buffer)
            if (bytesRead == -1)
                break
            out.write(buffer, 0, bytesRead)
        }
    }

    private external fun nativeLoad(internalStoragePaths: Array<String>)
    private external fun nativePlay(index: Int)
}
