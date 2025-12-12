/**
 * Course: MAD204 - Assignment 3
 * Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: Optional settings Activity for editing username
 *              and dark/light theme preferences with SharedPreferences.
 */


package com.example.assignment3notesmediamanager

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3notesmediamanager.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etUsername: EditText = findViewById(R.id.etUsernameSettings)
        val swTheme: Switch = findViewById(R.id.swThemeSettings)
        val btnSave: Button = findViewById(R.id.btnSaveSettings)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        etUsername.setText(prefs.getString("username", ""))
        swTheme.isChecked = prefs.getBoolean("dark_theme", false)

        btnSave.setOnClickListener {
            prefs.edit()
                .putString("username", etUsername.text.toString())
                .putBoolean("dark_theme", swTheme.isChecked)
                .apply()
            finish()
        }
    }
}
