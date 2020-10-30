package furhatos.app.mathhelper.nlu

import furhatos.nlu.*
import furhatos.nlu.common.PersonName
import furhatos.util.Language


class Confused: Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("What?",
                "What is this?",
                "Who are you?")
    }
}

class ProvideName(var name : PersonName? = null):Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("@name", "My @name is", "I am @name")
    }
}

//class Level: EnumEntity(){
//    override fun getEnum(lang: Language): List<String> {
//        return listOf("Beginner", "Intermediate", "Advanced")
//    }
//}