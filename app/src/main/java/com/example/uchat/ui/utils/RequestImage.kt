import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import com.example.uchat.domain.viewmodel.UserViewModel
import com.example.uchat.ui.screens.LogoutDialog
import com.example.uchat.ui.screens.getGoogleLoginAuth
import com.example.uchat.ui.theme.Pink
import com.example.uchat.ui.theme.Roboto
import com.example.uchat.ui.utils.ScreensNav
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

@Composable
fun AlertDialogBox(
    title: String,
    text: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = Roboto,
                modifier = Modifier.padding(6.dp)
            )
        },
        text = {
            Text(
                text = text,
                fontSize = 16.sp,
                fontFamily = Roboto,
                modifier = Modifier.padding(6.dp)
            )
        },
        confirmButton = {
            Text(text = confirmText,
                fontSize = 20.sp,
                color = Pink,
                fontFamily = Roboto,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        onConfirm()
                    })
        },
        dismissButton = {
            Text(text = dismissText,
                fontSize = 20.sp,
                color = Pink,
                fontFamily = Roboto,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onDismiss() })
        }
    )
}

@Composable
fun RequestContentPermission(
    userImage: String,
    onImageChange:(String)->Unit
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    if (imageUri == null) {
        Image(
            painter = rememberImagePainter(data = userImage),
            contentDescription = "",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                },
            contentScale = ContentScale.Crop
        )

    }
    else {
        onImageChange(imageUri.toString())
        Image(
            painter = rememberImagePainter(data = imageUri),
            contentDescription = "",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                },
            contentScale = ContentScale.Crop

        )


    }
}