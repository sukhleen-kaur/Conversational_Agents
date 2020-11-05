package furhatos.app.mathhelper.flow

import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.util.Gender
import furhatos.util.Language
import org.apache.commons.io.FileUtils
import java.io.File
import java.sql.Timestamp

val Idle: State = state {

    init {
        furhat.setTexture("Rene")
        furhat.voice = PollyNeuralVoice(language = Language.ENGLISH_US, gender = Gender.FEMALE, pitch = "high")
        furhat.voice = PollyNeuralVoice.Salli()

//        furhat.voice = PollyVoice(language= Language.ENGLISH_US, gender=Gender.FEMALE, pitch = "high")
//        furhat.voice = PollyVoice.Salli()

        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

val Interaction: State = state {
    // update emotion everytime it goes to interaction
    ReadEmotion()

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }

}

// update emotion using the emotion classifier that is running in the back
fun ReadEmotion(){
    //val data_directory = "/Users/sukhleenkaur/Documents/University/TU_Delft/MSc_2/ConvAgents/Labs/Project/emotiondetection/input"
    val data_directory = System.getProperty("user.dir")+"/../emotiondetection/input"
    val directoryPath = File(data_directory)
    //println(directoryPath.canonicalPath)

    val fileList = directoryPath.list()
    if(fileList.isEmpty()){
        println("No more files in the directory")
        EMOTION = "neutral"
    }else {
        //println("List of files and directories in the specified directory:")
        //println("Sorted descending: ${fileList.sortedDescending()}")
        val fullFilePath = data_directory + "/" + fileList.sortedDescending()[0]
        val file = File(fullFilePath)
        val data = file.readText(Charsets.UTF_8)
        // println("calling api")
        EMOTION = khttp.get("http://localhost:8000/emotions", params = mapOf("np" to data)).text

        // delete files
        FileUtils.cleanDirectory(directoryPath)

    }
    // print and save emotion and time to evaluate the accuracy of emotion detection
    val timestamp = Timestamp(System.currentTimeMillis())
    println("${timestamp}: $EMOTION")

    var outputFile = File(System.getProperty("user.dir")+"/../emotiondetection/output/", "output.txt")
    outputFile.createNewFile()
    outputFile.appendText("${timestamp}: $EMOTION \n")

}