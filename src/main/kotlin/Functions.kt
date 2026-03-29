import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.format.DateTimeFormatterBuilder


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

    logger.info("New List: ${
        if(isSaturday) {
            "Saturday"
        } else {
            "Sunday"
        }}: ${currentList.joinToString("|")}"
    )
}


fun printFile(textFileName: String, locationSaturday: String, locationSunday: String) {
    val outputDirectory = File("output")
    if(!outputDirectory.exists()) {
        outputDirectory.mkdir()
    }
    val outputFile = File("${outputDirectory.name}/$textFileName.txt")
    outputFile.createNewFile()

    var output = ""
    if(saturdayGames.isNotEmpty()) {
        output += "*+++Samstag+++*\nAlle Spiele finden in der *$locationSaturday* statt\n"
        output += buildGamesString(saturdayGames)
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
    var output = ""
    games.forEach {
        output += "\n*${it.time} Uhr* "
        if(it.amountGames > 1) {
            output += "finden ${it.amountGames} Spiele der ${it.gameClass} statt. Dafür "
        }

        val totalAmountOfMissingPeople = it.missingKR + it.missingSR
        output += if( totalAmountOfMissingPeople == 1 ) {
            "wird "
        } else {
            "werden "
        }

        if(it.amountGames == 1) {
            output += "bei der ${it.gameClass} "
        }

        output += "noch "

        if(it.missingSR != 0) {
            output += "*${it.missingSR} SR* "
        }

        if(it.missingKR != 0){
            if(it.missingSR != 0 ) {
                output += "und "
            }
            output += "*${it.missingKR} KR* "

            // TODO
            if(it.gameClass.trim() == "1. Männer" || it.gameClass.trim() == "1.Männer")
                output += "mit Sachsenligaerweiterung "
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