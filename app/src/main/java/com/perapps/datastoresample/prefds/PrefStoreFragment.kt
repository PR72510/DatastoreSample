package com.perapps.datastoresample.prefds

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.perapps.datastoresample.R
import com.perapps.datastoresample.databinding.FragmentPrefStoreBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "myPrefs")

class PrefStoreFragment : Fragment() {

    private lateinit var binding: FragmentPrefStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrefStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.btnMove.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_prefStoreFragment_to_protoStoreFragment)
        }
    }

    private suspend fun save(key: String, value: String) {
        requireContext().dataStore.edit { preferences ->
            val preferenceKey = stringPreferencesKey(key)
            preferences[preferenceKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val preferenceKey = stringPreferencesKey(key)
        val preference = requireContext().dataStore.data.first()

        return preference[preferenceKey]
    }
}