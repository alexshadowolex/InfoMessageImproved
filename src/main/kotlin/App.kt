import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess
import com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry
import kotlinx.coroutines.delay
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

val saturdayGames = mutableStateListOf<Game>()

val sundayGames = mutableStateListOf<Game>()

@Composable
@Preview
fun App(){
    var isInDarkMode by remember { mutableStateOf(false) }
    var textFileName by remember { mutableStateOf("") }
    var locationSaturday by remember { mutableStateOf("") }
    var locationSunday by remember { mutableStateOf("") }
    var gameTime by remember { mutableStateOf("") }
    var gameClass by remember { mutableStateOf("") }
    var missingKR by remember { mutableStateOf(0) }
    var missingSR by remember { mutableStateOf(0) }
    var amountGames by remember { mutableStateOf(0) }

    val selectedValue = remember { mutableStateOf("") }
    val labelSaturday = "Saturday"
    val labelSunday = "Sunday"


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

    MaterialTheme(
        colors = if (isInDarkMode) {
            darkColorPalette
        } else {
            lightColorPalette
        }
    ) {
        Scaffold {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row {
                    Column {
                        TextField(
                            label = {
                                Text(
                                    color = MaterialTheme.colors.onPrimary,
                                    text = "File Name"
                                )
                            },
                            value = textFileName,
                            onValueChange = { value ->
                                textFileName = value
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
                                    text = "Location Saturday"
                                )
                            },
                            value = locationSaturday,
                            onValueChange = { value ->
                                locationSaturday = value
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
                                    text = "Location Sunday"
                                )
                            },
                            value = locationSunday,
                            onValueChange = { value ->
                                locationSunday = value
                            }
                        )
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
                                    text = "Time"
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
                                    text = "Game Class"
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
                                    text = "Missing KR"
                                )
                            },
                            value = missingKR.toString(),
                            onValueChange = { value ->
                                missingKR = try {
                                    value.toInt()
                                } catch (e: java.lang.NumberFormatException) {
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
                                    text = "Missing SR"
                                )
                            },
                            value = missingSR.toString(),
                            onValueChange = { value ->
                                missingSR = try {
                                    value.toInt()
                                } catch (e: java.lang.NumberFormatException) {
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
                                    text = "Amount Games"
                                )
                            },
                            value = amountGames.toString(),
                            onValueChange = { value ->
                                amountGames = try {
                                    value.toInt()
                                } catch (e: java.lang.NumberFormatException) {
                                    0
                                }
                            }
                        )
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxWidth(0.5F)
                            .padding(start = 12.dp)
                    ) {
                        Row {
                            RadioButton(
                                selected = selectedValue.value == labelSaturday,
                                onClick = {
                                    selectedValue.value = labelSaturday
                                }
                            )
                            Text(
                                text = labelSaturday,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row {
                            RadioButton(
                                selected = selectedValue.value == labelSunday,
                                onClick = {
                                    selectedValue.value = labelSunday
                                }
                            )
                            Text(
                                text = labelSunday,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
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
                                addGameToList(selectedValue.value == labelSaturday, gameTime, gameClass, missingKR, missingSR, amountGames)
                                gameTime = ""
                                gameClass = ""
                                missingKR = 0
                                missingSR = 0
                                amountGames = 0
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Add To List")
                        }
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                printFile(textFileName, locationSaturday, locationSunday)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Print")
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
                                text = "Saturday:",
                                fontSize = 20.sp
                            )
                        }
                        Row {
                            GameList(isSaturday = true, games = saturdayGames)
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
                                text = "Sunday:",
                                fontSize = 20.sp
                            )
                        }
                        Row {
                            GameList(isSaturday = false, sundayGames)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameList(isSaturday: Boolean, games: List<Game>) {
    Column (
        modifier = Modifier
            .verticalScroll(ScrollState(0))
    ) {
        if(games.isNotEmpty()) {
            games.forEach { game ->
                GameRow(isSaturday, game)
            }
        } else {
            Text("None")
        }
    }
}

@Composable
fun GameRow(isSaturday: Boolean, game: Game) {
    Row (
        modifier = Modifier.padding(bottom = 5.dp)
    ) {
        Text(
            text = game.time,
            modifier = Modifier
                .weight(0.11F)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = game.gameClass,
            modifier = Modifier
                .weight(0.25F)
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
            text = "Amount: ${game.amountGames}",
            modifier = Modifier
                .weight(0.2F)
                .align(Alignment.CenterVertically)
        )
        Button(
            onClick = {
                val currentList = if (isSaturday) {
                    saturdayGames
                } else {
                    sundayGames
                }
                currentList.remove(game)
                logger.info("New List: ${
                    if(isSaturday) {
                        "Saturday"
                    } else {
                        "Sunday"
                    }}: ${currentList.joinToString("|")}")
            },
            modifier = Modifier
                .size(15.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("x")
        }
    }
}

fun addGameToList(isSaturday: Boolean, gameTime: String, gameClass: String, missingKR: Int, missingSR: Int, amountGames: Int) {
    val currentList = if(isSaturday) {
        saturdayGames
    } else {
        sundayGames
    }

    currentList.add(
        Game(
            gameTime,
            gameClass,
            missingKR,
            missingSR,
            amountGames
        )
    )

    try {
        val tempList = currentList.toMutableList()
        tempList.sortBy {
            it.time.substringBefore(":").toInt()
        }

        currentList.clear()
        tempList.forEach {
            currentList.add(it)
        }
    } catch (e: Exception) {
        logger.error("Error wile sorting the list. Did you add garbage as a time?")
        e.printStackTrace()
    }
}


fun printFile(textFileName: String, locationSaturday: String, locationSunday: String) {

}