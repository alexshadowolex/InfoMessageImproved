import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter
import javax.swing.JOptionPane
import kotlin.system.exitProcess


val logger: Logger = LoggerFactory.getLogger("App")
fun main() = try {
    setupLogging()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Create Info Message v2",
            state = WindowState(
                size = DpSize(900.dp, 500.dp),
                position = WindowPosition(Alignment.Center)
            ),
            resizable = false,
            icon = painterResource(resourcePath = "icon.jfif"),
        ) {
            App()
        }
    }
} catch (e: Throwable) {
    JOptionPane.showMessageDialog(null, e.message + "\n" + StringWriter().also { e.printStackTrace(PrintWriter(it)) }, "InfoBox: File Debugger", JOptionPane.INFORMATION_MESSAGE)
    logger.error("Error while executing program.", e)
    exitProcess(-1)
}