package com.example.pharmaledger.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.pharmaledger.R
import com.example.pharmaledger.backup.BackupManager
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {
	private val createBackupLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			val uri = result.data?.data ?: return@registerForActivityResult
			viewLifecycleOwner.lifecycleScope.launch {
				try {
					BackupManager.exportToUri(requireContext(), uri)
					Toast.makeText(requireContext(), "Backup exported", Toast.LENGTH_SHORT).show()
				} catch (e: Exception) {
					Toast.makeText(requireContext(), "Backup failed: ${e.message}", Toast.LENGTH_LONG).show()
				}
			}
		}
	}
	private val openBackupLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			val uri = result.data?.data ?: return@registerForActivityResult
			viewLifecycleOwner.lifecycleScope.launch {
				try {
					BackupManager.importFromUri(requireContext(), uri)
					Toast.makeText(requireContext(), "Backup restored", Toast.LENGTH_SHORT).show()
				} catch (e: Exception) {
					Toast.makeText(requireContext(), "Restore failed: ${e.message}", Toast.LENGTH_LONG).show()
				}
			}
		}
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences, rootKey)

		val currencyPref = findPreference<EditTextPreference>("pref_currency")
		currencyPref?.setOnPreferenceChangeListener { _, _ -> true }

		findPreference<Preference>("pref_backup")?.setOnPreferenceClickListener {
			val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
				type = "application/json"
				addCategory(Intent.CATEGORY_OPENABLE)
				putExtra(Intent.EXTRA_TITLE, "pharmaledger-backup.json")
			}
			createBackupLauncher.launch(intent)
			true
		}
		findPreference<Preference>("pref_restore")?.setOnPreferenceClickListener {
			val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
				type = "application/json"
				addCategory(Intent.CATEGORY_OPENABLE)
			}
			openBackupLauncher.launch(intent)
			true
		}
	}
}