package com.postemall.sharetofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.postemall.sharetofile.ui.theme.ShareToFileTheme
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>
    private var receivedText: String? = null
    private val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShareToFileTheme {
                MainScreen(snackbarHostState)
            }
        }

        val intent = intent
        val action = intent.action
        val type = intent.type

        filePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                if (uri != null && receivedText != null) {
                    appendTextToFile(uri, receivedText!!)
                    showSnackbar("Text Appended", "OK")
                }
            }
            // TODO: error handling
        }

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent)
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/*"
        }
        filePickerLauncher.launch(intent)
    }

    private fun appendTextToFile(uri: Uri, text: String) {
        contentResolver.openFileDescriptor(uri, "wa")?.use { pfd ->
            FileOutputStream(pfd.fileDescriptor).use { outputStream ->
                // TODO make timestamp and it's format settings
                val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                val formattedText = "\n$timestamp\n$text\n"

                outputStream.write(formattedText.toByteArray())
                outputStream.flush()
            }
        }
    }

    private fun handleSendText(intent: Intent) {
        // .let is similar to a nullish coalescing operator and a lambda function in C#
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { text ->
            receivedText = text
            openFilePicker()
        }
    }

    private fun showSnackbar(message: String, actionText: String) {
        // Use a coroutine to show the Snackbar
        lifecycleScope.launch {
            snackbarHostState.showSnackbar(message, actionText, duration = SnackbarDuration.Short)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShareToFileTheme {
        Greeting("Android")
    }
}

@Composable
fun MainScreen(snackbarHostState: SnackbarHostState) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            // Your main content here
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}