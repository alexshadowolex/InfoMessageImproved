import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.format.DateTimeFormatterBuilder
import kotlin.time.Clock
import kotlin.time.toJavaInstant


fun addGameToList(
    location: String,
    day: String,
    gameTime: String,
    gameClass: String,
    missingKR: Int,
    missingSR: Int,
    amountGames: Int
) {
    allGames.add(
        Game(
            gameTime,
            gameClass,
            missingKR,
            missingSR,
            amountGames,
            location,
            day
        )
    )

    try {
        allGames.sortBy {
            it.time.substringBefore(":").toInt()
        }
    } catch (e: Exception) {
        logger.error("Error wile sorting the list. Did you add garbage as a time?")
        e.printStackTrace()
    }

    logger.info("New List: ${allGames.joinToString("|")}"
    )
}


fun printFile(textFileName: String, locationSaturday: String, locationSunday: String) {
    val outputDirectory = File("output")
    if(!outputDirectory.exists()) {
        outputDirectory.mkdir()
    }
    val outputFile = File("${outputDirectory.name}/$textFileName.txt")
    outputFile.createNewFile()

    val saturdayGames = allGames.filter { it.day == labelDaySaturday }
    val sundayGames = allGames.filter { it.day == labelDaySunday }

    var output = ""
    if(saturdayGames.isNotEmpty()) {
        output += "*+++Samstag+++*\nAlle Spiele finden in der *$locationSaturday* statt\n"
        output += buildGamesString(allGames)
    }

    if(sundayGames.isNotEmpty()) {
        output += "*+++Sonntag+++*\nAlle Spiele finden in der *$locationSunday* statt\n"
        output += buildGamesString(sundayGames)
    }

    logger.info("Print Finished.")
    logger.info("Full printed text:\n$output")

    outputFile.writeText(output, charset("UTF-8"))
}

fun buildGamesString(games: List<Game>): String {
    val specialKrGameClasses = listOf(
        "1. Männer",
        "1.Männer",
        "1. Frauen",
        "1.Frauen"
    )
    var output = ""
    games.forEach { game ->
        output += "\n*${game.time} Uhr* "
        if(game.amountGames > 1) {
            output += "finden ${game.amountGames} Spiele der ${game.gameClass} statt. Dafür "
        }

        val totalAmountOfMissingPeople = game.missingKR + game.missingSR
        output += if( totalAmountOfMissingPeople == 1 ) {
            "wird "
        } else {
            "werden "
        }

        if(game.amountGames == 1) {
            output += "bei der ${game.gameClass} "
        }

        output += "noch "

        if(game.missingSR != 0) {
            output += "*${game.missingSR} SR* "
        }

        if(game.missingKR != 0){
            if(game.missingSR != 0 ) {
                output += "und "
            }
            output += "*${game.missingKR} KR* "

            if(specialKrGameClasses.any { game.gameClass.trim() == it })
                // TODO
                output += "mit HVS-Erlaubnis "
        }

        output += "benötigt.\n\n====================\n"
    }

    return output
}


private const val LOG_DIRECTORY = "logs"

fun setupLogging() {
    Files.createDirectories(Paths.get(LOG_DIRECTORY))

    val logFileName = DateTimeFormatterBuilder()
        .appendInstant(0)
        .toFormatter()
        .format(Clock.System.now().toJavaInstant())
        .replace(':', '-')

    val logFile = Paths.get(LOG_DIRECTORY, "${logFileName}.log").toFile().also {
        if (!it.exists()) {
            it.createNewFile()
        }
    }

    System.setOut(PrintStream(MultiOutputStream(System.out, FileOutputStream(logFile))))

    logger.info("Log file '${logFile.name}' has been created.")
}