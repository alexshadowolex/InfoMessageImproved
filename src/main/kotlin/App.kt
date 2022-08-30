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

val testSaturdayGames = listOf(
    Game("10:00", "mJC", 2, 0, 1),
    Game("12:00", "mJB", 2, 0, 1),
    Game("14:00", "2. Männer", 2, 1, 1),
    Game("14:00", "2. Männer", 2, 1, 1),
    Game("14:00", "2. Männer", 2, 1, 1),
    Game("14:00", "2. Männer", 2, 1, 1)
)

val testSundayGames = listOf(
    Game("10:00", "mJE", 2, 2, 4),
    Game("15:00", "mJC", 1, 0, 1)
)

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
                                logger.info(value)
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
                                logger.info(value)
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
                                logger.info(value)
                                locationSunday = value
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
                                    text = "Time"
                                )
                            },
                            value = gameTime,
                            onValueChange = { value ->
                                logger.info(value)
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
                                logger.info(value)
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
                                logger.info(value)
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
                                logger.info(value)
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
                                logger.info(value)
                                amountGames = try {
                                    value.toInt()
                                } catch (e: java.lang.NumberFormatException) {
                                    0
                                }
                            }
                        )
                    }

                    val selectedValue = remember { mutableStateOf("") }
                    val labelSaturday = "Saturday"
                    val labelSunday = "Sunday"

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
                            onClick = {},
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
                            onClick = {},
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
                            GameList(testSaturdayGames)
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
                            GameList(testSundayGames)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameList(games: List<Game>) {
    Column (
        modifier = Modifier
            .verticalScroll(ScrollState(0))
    ) {
        if(games.isNotEmpty()) {
            games.forEach { game ->
                GameRow(game)
            }
        } else {
            Text("None")
        }
    }
}

@Composable
fun GameRow(game: Game) {
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
            onClick = {},
            modifier = Modifier
                .size(15.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("x")
        }
    }
}