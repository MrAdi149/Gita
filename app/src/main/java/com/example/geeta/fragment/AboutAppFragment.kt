package com.example.geeta.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.geeta.databinding.AboutAppFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AboutAppFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = AboutAppFragmentBinding.inflate(layoutInflater)
        val dialogView = binding.root

        binding.githubIcon.setOnClickListener {
            openUrl("https://github.com/MrAdi149")
        }

        binding.githubIssueButton.setOnClickListener {
            openUrl("https://github.com/MrAdi149/Gita/issues")
        }

        binding.licenseText.setOnClickListener {
            openUrl("https://www.gnu.org/licenses/gpl-3.0.en.html")
        }

        binding.shareIcon.setOnClickListener {
            val githubUrl = "https://github.com/MrAdi149/Gita"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, githubUrl)
            startActivity(Intent.createChooser(shareIntent, "Share App Link"))
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}