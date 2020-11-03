package furhatos.app.mathhelper

import java.io.File
import com.google.gson.JsonParser
import java.nio.file.Files
import java.nio.file.Files.isRegularFile
import jdk.nashorn.internal.objects.NativeArray.forEach
import java.nio.file.Paths
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.stream.Stream





fun main(args: Array<String>) {
    var data_directory = "/Users/sukhleenkaur/Documents/University/TU_Delft/MSc_2/ConvAgents/Conversational_Agents/Project/emotiondetection/input"

    while (true) {
        val directoryPath = File(data_directory)
        //List of all files and directories
        val fileList = directoryPath.list()
        if(fileList.isEmpty()){
            println("No more files in the directory")
            break;
        }
        println("List of files and directories in the specified directory:")
        for (i in fileList!!.indices) {
            println(fileList[i])
            var fullFilePath = data_directory + "/" + fileList[i]
            var file = File(fullFilePath)
            var data = file.readText(Charsets.UTF_8)
            println("calling api")
            var r = khttp.get("http://localhost:8000/emotions", params = mapOf("np" to data))
            println(r.text) // emotion that is returned
            file.delete()

        }
        TimeUnit.SECONDS.sleep(1);

    }
}