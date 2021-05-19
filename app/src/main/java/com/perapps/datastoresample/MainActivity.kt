package com.perapps.datastoresample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.perapps.datastoresample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "myPrefs")

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                    binding.tietSaveKey.text.toString(),
                    binding.tietSaveValue.text.toString()
                )
            }
        }

        binding.btnGet.setOnClickListener {
            lifecycleScope.launch {
                val resultValue = read(binding.tietGetKey.text.toString())
                binding.tvResultValue.text = resultValue
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        dataStore.edit { preferences ->
            val preferenceKey = stringPreferencesKey(key)
            preferences[preferenceKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val preferenceKey = stringPreferencesKey(key)
        val preference = dataStore.data.first()

        return preference[preferenceKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}