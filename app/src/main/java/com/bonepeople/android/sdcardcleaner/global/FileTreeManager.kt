package com.bonepeople.android.sdcardcleaner.global

import android.os.Environment
import com.bonepeople.android.sdcardcleaner.data.FileTreeInfo
import com.bonepeople.android.widget.CoroutinesHolder
import com.bonepeople.android.widget.util.AppTime
import kotlinx.coroutines.launch
import java.io.File

object FileTreeManager {
    object STATE {
        const val READY = 0
        const val SCAN_EXECUTING = 1
        const val SCAN_STOPPING = 2
        const val SCAN_FINISH = 3
    }

    object Summary {
        val totalSpace by lazy { Environment.getExternalStorageDirectory().totalSpace }
        val freeSpace by lazy { Environment.getExternalStorageDirectory().freeSpace }
        var rootFile: FileTreeInfo? = null
        var rubbishCount = 0L
        var rubbishSize = 0L
    }

    var currentState = STATE.READY
    private var startTime = 0L
    private var endTime = 0L

    fun getProgressTimeString(): String {
        if (currentState != STATE.SCAN_FINISH) endTime = System.currentTimeMillis()
        val time = endTime - startTime
        return "(${AppTime.getTimeString(time)}秒)"
    }

    fun startScan() {
        currentState = STATE.SCAN_EXECUTING
        CoroutinesHolder.default.launch {
            startTime = System.currentTimeMillis()
            Summary.rubbishCount = 0
            Summary.rubbishSize = 0
            val file = Environment.getExternalStorageDirectory()
            Summary.rootFile = FileTreeInfo()
            scanFile(null, Summary.rootFile!!, file)
            currentState = STATE.SCAN_FINISH
            endTime = System.currentTimeMillis()
        }
    }

    fun stopScan() {
        currentState = STATE.SCAN_STOPPING
    }

    private fun scanFile(parentFile: FileTreeInfo?, fileInfo: FileTreeInfo, file: File) {
        fileInfo.parent = parentFile
        fileInfo.name = file.name
        fileInfo.path = file.absolutePath
        fileInfo.directory = file.isDirectory
        fileInfo.size = if (fileInfo.directory) 0 else file.length()
        //垃圾文件的判断
        when (true) {
            CleanPathManager.whiteList.contains(fileInfo.path) -> {
                fileInfo.rubbish = false
            }
            CleanPathManager.blackList.contains(fileInfo.path), fileInfo.parent?.rubbish -> {
                fileInfo.rubbish = true
                Summary.rubbishCount++
                Summary.rubbishSize += fileInfo.size
            }
            else -> {
                fileInfo.rubbish = false
            }
        }
        parentFile?.children?.add(fileInfo)
        updateParentFile(fileInfo.parent, fileInfo)

        if (fileInfo.directory) {
            file.listFiles()?.forEach {
                if (currentState == STATE.SCAN_EXECUTING) {
                    scanFile(fileInfo, FileTreeInfo(), it)
                }
            }
        }
    }

    private fun updateParentFile(parentFile: FileTreeInfo?, fileInfo: FileTreeInfo) {
        parentFile?.let {
            it.size += fileInfo.size
            it.fileCount += 1
            updateParentFile(it.parent, fileInfo)
        }
    }
}