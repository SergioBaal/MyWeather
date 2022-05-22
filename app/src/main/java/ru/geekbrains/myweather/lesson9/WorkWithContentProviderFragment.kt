package ru.geekbrains.myweather.lesson9

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.geekbrains.myweather.databinding.FragmentWorkWithContentProviderBinding
import ru.geekbrains.myweather.utlis.REQUEST_CODE

class WorkWithContentProviderFragment : Fragment() {

    private var _binding: FragmentWorkWithContentProviderBinding? = null
    private val binding: FragmentWorkWithContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkWithContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                getContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                explainForContacts()
            }
            else -> {
                mRequestContactPermission()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {

            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.CALL_PHONE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    makeCall()
                }
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()

                } else {
                    explainForContacts()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        ) // или DESC
        cursor?.let {
            for (i in 0 until it.count) {
                if (cursor.moveToPosition(i)) {
                    val columnNameIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name: String = cursor.getString(columnNameIndex)
                    binding.containerForContacts.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text = name
                    })
                }
            }
        } //вызываем имена контактов

        val cursorForPhone = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC"
        ) // или DESC
        cursorForPhone?.let {
            for (i in 0 until it.count) {
                if (cursorForPhone.moveToPosition(i)) {
                    val columnNameIndex =
                        cursorForPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    phone = cursorForPhone.getString(columnNameIndex)
                    binding.containerForPhones.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text = phone.toString()
                        setOnClickListener {
                            makeCall()
                        }
                    })
                }
            }
        }  //вызываем телефоны контактов
    }



    private val launcherReadContacts =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                getContacts()
            } else {
                explainForContacts()
            }
        }

    private fun mRequestContactPermission() {
        // requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
        launcherReadContacts.launch(Manifest.permission.READ_CONTACTS)
    }




    private var phone: String? = null
    private val launcherMakeCall = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            phone?.let {
                if (result) {
                    val intent =
                        Intent(Intent.ACTION_CALL, Uri.fromParts("tel", it, null))
                    startActivity(intent)
                } else {
                    explainForCall()
                }
            }
    }

    private fun makeCall() {
        phone?.let{
            launcherMakeCall.launch(Manifest.permission.CALL_PHONE)
            }
        }

    private fun explainForContacts() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Объяснение бла бла бла бла")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                mRequestContactPermission()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun explainForCall() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к звонки")
            .setMessage("Объяснение бла бла бла бла")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                makeCall()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }



    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()
    }

}