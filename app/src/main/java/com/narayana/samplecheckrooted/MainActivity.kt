package com.narayana.samplecheckrooted

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isRooted = isRooted()
        Toast.makeText(this, "".plus(isRooted), Toast.LENGTH_SHORT).show()
    }

    fun isRooted(): Boolean {

        // get from build info
        val buildTags = android.os.Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
        } catch (e1: Exception) {
            // ignore
        }

        // try executing commands
        return (canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su"))
    }

    // executes a command on the system
    private fun canExecuteCommand(command: String): Boolean {
        var executedSuccesfully: Boolean
        try {
            Runtime.getRuntime().exec(command)
            executedSuccesfully = true
        } catch (e: Exception) {
            executedSuccesfully = false
        }

        return executedSuccesfully
    }
}