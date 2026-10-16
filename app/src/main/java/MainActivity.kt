package hidaktoziza.example.ugadaikajetpack

import android.R.attr.value
import android.os.Bundle
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hidaktoziza.example.ugadaikajetpack.ui.theme.UgadaikajetpackTheme
import org.intellij.lang.annotations.JdkConstants
import java.nio.file.WatchEvent
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UgadaikajetpackTheme {
                var maxChislo by remember { mutableStateOf(25) }
                var minChislo by remember { mutableStateOf(1) }
                var maxAttempt by remember { mutableStateOf(6) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator(
                        maxChislo = maxChislo,
                        minChislo = minChislo,
                        maxAttempt = maxAttempt,
                        onSettingsChanged = { newMaxChislo, newMaxAtt ->
                            maxChislo = newMaxChislo
                            maxAttempt = newMaxAtt
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun AppNavigator(onSettingsChanged:(Int,Int) -> Unit,maxChislo: Int, minChislo: Int, maxAttempt: Int, modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf("main") }

    when (currentScreen) {
        "main" -> {
            MainScreen(
                onSettingsClick = { currentScreen = "settings" },
                maxChislo = maxChislo,
                maxAttempt = maxAttempt,
                minChislo = minChislo,
                modifier = modifier,
            )
        }
        "settings" ->
        {
            SettingsScreen(

                maxChislo = maxChislo,
                maxAttempt = maxAttempt,
                onSaveSettings = { newMaxChislo, newMaxAtt ->
                    onSettingsChanged(newMaxChislo, newMaxAtt)
                    currentScreen = "main"

                },


                modifier = modifier,
            )
        }


    }
}











@Composable
fun MainScreen(onSettingsClick: () -> Unit,maxAttempt: Int, minChislo: Int, maxChislo: Int, modifier: Modifier = Modifier)
{
    var attTime by remember { mutableStateOf(maxAttempt) }
    var textState by remember{mutableStateOf("")}
    var generateNumber by remember { mutableStateOf(Random.nextInt(minChislo, maxChislo +1)) }
    var isGameOver by remember { mutableStateOf(false) }
    var imageId by remember { mutableStateOf<Int?>(null) }
    var message by remember { mutableStateOf("Введите число от ${minChislo} до ${maxChislo}") }
    Box(modifier = Modifier.fillMaxSize())
    {
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                "Настройки",
                modifier = Modifier.size(32.dp)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.padding(50.dp))

        Text(text = "Кол-во попыток: ${attTime}")

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = message, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        TextField(

            value = textState,
            onValueChange = {newText ->
                textState = newText.filter {it.isDigit()}
            },
            placeholder = {Text("Введите число")},
            singleLine = true,
            keyboardOptions = KeyboardOptions
             (

                keyboardType = KeyboardType.Number

             )
        )
        Button(
            onClick =
            {
                val userGuess = textState.toIntOrNull()
                if(userGuess != null && !isGameOver)
                {
                    if(userGuess == generateNumber)
                    {
                        message = "Вы победили!"
                        isGameOver = true
                        imageId = R.drawable.win
                    }
                    else{
                        attTime--
                        if(attTime <= 0)
                        {
                            message = "Вы проиграли, было загадано  ${generateNumber}"
                            isGameOver = true
                            imageId = R.drawable.lose
                        }

                        else if(userGuess > generateNumber)
                        {
                            message = "Нужно число меньше"
                        }
                        else if(userGuess < generateNumber)
                        {
                            message = "Нужно число больше"
                        }
                    }
                }
                textState = ""
            },
            enabled = !isGameOver && textState.isNotEmpty()

        )
        {
            Text(text = "Проверить")
        }
        if(isGameOver){
            Spacer(modifier = Modifier.height(20.dp))
            imageId?.let{
                imgId ->
                Image(
                    painter = painterResource(id = imgId),
                    contentDescription = "Результат",
                    modifier = Modifier.size(120.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                generateNumber = Random.nextInt(minChislo, maxChislo + 1)
                attTime = maxAttempt
                message = "Введите число от ${minChislo} до ${maxChislo}"
                isGameOver = false
                textState = ""
                imageId = null
            }) {
                Text(text = "Играть снова")
            }

        }

    }

}
@Composable
fun SettingsScreen(maxChislo: Int, maxAttempt: Int, onSaveSettings: (Int,Int) -> Unit,modifier: Modifier = Modifier)
{
    var textState by remember{mutableStateOf("")}
    var txtState by remember{mutableStateOf("")}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Spacer(modifier = Modifier.padding(40.dp))
        Text(text = "Настройки")
        TextField(
            value = textState,
            onValueChange = {newText ->
                textState = newText.filter {it.isDigit()}
            },
            placeholder = {Text("Введите максимальное число")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(

                keyboardType = KeyboardType.Number

            )
        )
        Spacer(modifier = Modifier.padding(10.dp))

        TextField(
            value = txtState,
            onValueChange = {newtext ->
                txtState = newtext.filter {it.isDigit()}
            },
            placeholder = {Text("Введите кол-во попыток")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(

                keyboardType = KeyboardType.Number

            )
        )
        Button(onClick = {
            val newMaxChislo = textState.toIntOrNull() ?: maxChislo
            val newMaxAtt = txtState.toIntOrNull() ?: maxAttempt
            onSaveSettings(newMaxChislo, newMaxAtt)
        }) {
            Text(text = "Сохранить")
        }
    }
}
