package com.example.videothumbnail

import android.R
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.videothumbnail.ui.theme.VideoThumbnailTheme
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import java.io.IOException
import java.net.URL
import java.util.regex.Pattern

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoThumbnailTheme {
                val imageUrl = remember { mutableStateOf(TextFieldValue()) }
                val isImageVisible = remember { mutableStateOf(false) }
                Column {
                    TextField(
                        value = imageUrl.value,
                        onValueChange = { imageUrl.value = it },
                        label = { Text("Enter Image URL",style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Normal))},
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        onClick = {
                            isImageVisible.value = true
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Show Thumbnail")
                    }

                    if (isImageVisible.value) {
                        YouTubeThumbnail(videoId = extractYouTubeVideoId(imageUrl.value.text).toString())
                    }
                }
            }
        }
    }
}
fun getYouTubeVideoThumbnailUrl(videoId: String): String {
    return "https://img.youtube.com/vi/$videoId/0.jpg"
}

@Composable
fun YouTubeThumbnail(videoId: String) {

    val thumbnailUrl = getYouTubeVideoThumbnailUrl(videoId)

    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = thumbnailUrl).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                placeholder(R.drawable.ic_media_play)
                error(R.drawable.ic_dialog_alert)
            }).build()
        ),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}

fun extractYouTubeVideoId(url: String): String? {
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|v=|\\/v\\/|watch\\?v=|\\/videos\\/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|v=|\\/v\\/|watch\\?v=|\\/videos\\/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/)\\w+"
    val compiledPattern = Pattern.compile(pattern)
    val matcher = compiledPattern.matcher(url)
    return if (matcher.find()) {
        matcher.group()
    } else {
        null
    }
}