package com.perapps.datastoresample.protods

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.perapps.datastoresample.Person
import com.perapps.datastoresample.PersonSerializer
import com.perapps.datastoresample.databinding.FragmentProtoStoreBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.personDataStore: DataStore<Person> by dataStore(
    fileName = "my_person.pb",
    serializer = PersonSerializer
)

class ProtoStoreFragment : Fragment() {

    private lateinit var binding: FragmentProtoStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProtoStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        lifecycleScope.launch {
            requireContext().personDataStore.data.collect { person ->
                binding.tvResultFname.text = person.firstName
                binding.tvResultLname.text = person.lastName
                binding.tvResultAge.text = person.age.toString()

            }
        }
    }

    private fun setListeners() {
        binding.btnSaveProto.setOnClickListener {
            lifecycleScope.launch {
                saveData(
                    binding.tietFname.text.toString(),
                    binding.tietLname.text.toString(),
                    binding.tietAge.text.toString().toInt(),
                )
            }
        }
    }

    private suspend fun saveData(firstName: String, lastName: String, age: Int) {
        requireContext().personDataStore.updateData { person ->
            person.toBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAge(age)
                .build()
        }
    }
}