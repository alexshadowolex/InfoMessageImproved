import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess
import com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

val lightColorPalette = lightColors(
    primary = Color(0xff4466ff),
    onPrimary = Color.White,
    secondary = Color(0xff0b5b8e),
    background = Color.White,
    onBackground = Color.Black,
)

val darkColorPalette = darkColors(
    primary = Color(0xff2244bb),
    onPrimary = Color.White,
    secondary = Color(0xff5bbbfe),
    background = Color.DarkGray,
    onBackground = Color.White,
)

val allGames = mutableStateListOf<Game>()

const val labelLocationLH = "Lößnitzsporthalle"
const val labelLocationEH = "Elbsporthalle"
const val labelDaySaturday = "Samstag"
const val labelDaySunday = "Sonntag"

@Composable
@Preview
fun App(){
    var isInDarkMode by remember { mutableStateOf(false) }
    var textFileName by remember { mutableStateOf("") }
    var gameTime by remember { mutableStateOf("") }
    var gameClass by remember { mutableStateOf("") }
    var missingKR by remember { mutableStateOf(0) }
    var missingSR by remember { mutableStateOf(0) }
    var amountGames by remember { mutableStateOf(0) }

    val selectedLocation = remember { mutableStateOf(labelLocationLH) }

    val selectedDay = remember { mutableStateOf(labelDaySaturday) }

    val isTextGenerated = mutableStateOf(false)
    var lastGeneratedFileName = ""

    LaunchedEffect(Unit) {
        while (true) {
            isInDarkMode = if (NativeParameterStoreAccess.IS_WINDOWS) {
                WindowsRegistry.getWindowsRegistryEntry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "AppsUseLightTheme") == 0x0
            } else {
                false
            }

            delay(1.seconds)
        }
    }
    val scaffoldState = rememberScaffoldState()

    MaterialTheme(
        colors = if (isInDarkMode) {
            darkColorPalette
        } else {
            lightColorPalette
        }
    ) {
        Scaffold (
            scaffoldState = scaffoldState
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxHeight(0.25F)
                ) {
                    Column (
                        modifier = Modifier
                            .padding(end = 12.dp)
                    ) {
                        // TODO: Folder name with config?

                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "Dateiname"
                                )
                            },
                            value = textFileName,
                            onValueChange = { value ->
                                textFileName = value
                            }

                        )

                        if (isTextGenerated.value) {
                            Text(
                                style = MaterialTheme.typography.body1,
                                text = "Öffne Ordner der erzeugten Textdatei",
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .clickable {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            if (lastGeneratedFileName.isNotEmpty()) {
                                                val filePathAndName = System.getProperty("user.dir") +
                                                    "\\output\\" +
                                                    lastGeneratedFileName

                                                Runtime.getRuntime().exec(
                                            "explorer.exe " +
                                                    "/select," +
                                                    filePathAndName
                                                )
                                            }
                                        }
                                    }
                                    .pointerHoverIcon(PointerIcon.Hand),
                                color = MaterialTheme.colors.secondary,
                            )
                        }
                    }


                    Column (
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .padding(end = 12.dp)
                    ) {
                        Text("Halle")

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedLocation.value == labelLocationLH,
                                onClick = {
                                    selectedLocation.value = labelLocationLH
                                }
                            )
                            Text(
                                text = labelLocationLH,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedLocation.value == labelLocationEH,
                                onClick = {
                                    selectedLocation.value = labelLocationEH
                                }
                            )
                            Text(
                                text = labelLocationEH,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }


                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text("Tag")

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedDay.value == labelDaySaturday,
                                onClick = {
                                    selectedDay.value = labelDaySaturday
                                }
                            )
                            Text(
                                text = labelDaySaturday,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedDay.value == labelDaySunday,
                                onClick = {
                                    selectedDay.value = labelDaySunday
                                }
                            )
                            Text(
                                text = labelDaySunday,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                Divider(
                    color = MaterialTheme.colors.secondary,
                    thickness = 2.dp,
                    modifier = Modifier.padding(top = 9.dp)
                )

                Row (
                    modifier = Modifier.padding(top = 9.dp)
                ) {
                    Column {
                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "Anwurfzeit"
                                )
                            },
                            value = gameTime,
                            onValueChange = { value ->
                                gameTime = value
                            },
                            modifier = Modifier
                                .padding(end = 12.dp)

                        )
                    }

                    Column {
                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "Spielklasse"
                                )
                            },
                            value = gameClass,
                            onValueChange = { value ->
                                gameClass = value
                            },
                            modifier = Modifier
                                .padding(end = 12.dp)

                        )
                    }

                    Column {
                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "Fehlende KR"
                                )
                            },
                            value = missingKR.toString(),
                            onValueChange = { value ->
                                missingKR = try {
                                    value.trim().toInt()
                                } catch (_: java.lang.NumberFormatException) {
                                    0
                                }
                            }
                        )
                    }
                }

                Row (
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Column {
                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "Fehlende SR"
                                )
                            },
                            value = missingSR.toString(),
                            onValueChange = { value ->
                                missingSR = try {
                                    value.trim().toInt()
                                } catch (_: java.lang.NumberFormatException) {
                                    0
                                }
                            },
                            modifier = Modifier
                                .padding(end = 12.dp)
                        )
                    }

                    Column {
                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "Anzahl der Spiele"
                                )
                            },
                            value = amountGames.toString(),
                            onValueChange = { value ->
                                amountGames = try {
                                    value.trim().toInt()
                                } catch (_: NumberFormatException) {
                                    0
                                }
                            }
                        )
                    }
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(0.5F)
                            .padding(end = 12.dp)
                    ) {
                        Button(
                            onClick = {
                                addGameToList(
                                    selectedLocation.value,
                                    selectedDay.value,
                                    gameTime.trim(),
                                    gameClass.trim(),
                                    missingKR,
                                    missingSR,
                                    amountGames
                                )

                                gameTime = ""
                                gameClass = ""
                                missingKR = 0
                                missingSR = 0
                                amountGames = 0
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Zur Liste hinzufügen")
                        }
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val textFileNameTrimmed = textFileName.trim()
                                var validationErrorMessage = ""

                                if(textFileNameTrimmed.isEmpty()) {
                                    validationErrorMessage = "Name der Textdatei fehlt!"
                                }

                                if(allGames.isEmpty()) {
                                    validationErrorMessage = "Keine Spiele vorhanden!"
                                }

                                if(validationErrorMessage.isNotEmpty()) {
                                    logger.info(validationErrorMessage)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = validationErrorMessage,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                }

                                lastGeneratedFileName = printFile(textFileNameTrimmed)
                                isTextGenerated.value = true

                                CoroutineScope(Dispatchers.IO).launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Text in Textdatei \"${lastGeneratedFileName}\" erzeugt",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Text erzeugen")
                        }
                    }
                }

                Row (
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(0.5F)
                            .padding(end = 5.dp)
                    ) {
                        Row (
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            Text(
                                text = labelDaySaturday,
                                fontSize = 20.sp
                            )
                        }
                        Row {
                            GameList(isSaturday = true)
                        }
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp)
                    ) {
                        Row (
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            Text(
                                text = labelDaySunday,
                                fontSize = 20.sp
                            )
                        }
                        Row {
                            GameList(isSaturday = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameList(isSaturday: Boolean) {
    Column (
        modifier = Modifier
            .verticalScroll(ScrollState(0))
    ) {

        val currentDaysList = allGames.filter { game ->
            if(isSaturday) {
                game.day == labelDaySaturday
            } else {
                game.day == labelDaySunday
            }
        }

        if(currentDaysList.isNotEmpty()) {

            @Composable
            fun gameForEachCallback(game: Game) {
                GameRow(game)

                Divider(
                    color = MaterialTheme.colors.secondary,
                    thickness = 1.dp
                )
            }

            val gamesGymLH = currentDaysList.filter { it.location == labelLocationLH}
            val gamesGymEH = currentDaysList.filter { it.location == labelLocationEH}
            val areBothListsFilled = gamesGymLH.isNotEmpty() && gamesGymEH.isNotEmpty()

            if(gamesGymLH.isNotEmpty()) {
                Text(labelLocationLH)
                gamesGymLH.forEach { game ->
                    gameForEachCallback(game)
                }
            }

            if(areBothListsFilled) {
                Divider(
                    color = MaterialTheme.colors.secondary,
                    thickness = 2.dp
                )
            }

            if(gamesGymEH.isNotEmpty()) {
                Text(labelLocationEH)
                gamesGymEH.forEach { game ->
                    gameForEachCallback(game)
                }
            }
        } else {
            Text(
                text = "Keine Spiele vorhanden",
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
        }
    }
}

@Composable
fun GameRow(game: Game) {
    Row (
        modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
    ) {
        Text(
            text = game.time,
            modifier = Modifier
                .weight(0.14F)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = game.gameClass,
            modifier = Modifier
                .weight(0.22F)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = "KR: ${game.missingKR}",
            modifier = Modifier
                .weight(0.15F)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = "SR: ${game.missingSR}",
            modifier = Modifier
                .weight(0.15F)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = "Anzahl: ${game.amountGames}",
            modifier = Modifier
                .weight(0.2F)
                .align(Alignment.CenterVertically)
        )
        Button(
            onClick = {
                allGames.remove(game)
                logger.info("New List: ${allGames.joinToString("|")}")
            },
            modifier = Modifier
                .size(15.dp)
                .align(Alignment.CenterVertically),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("x")
        }
    }
}