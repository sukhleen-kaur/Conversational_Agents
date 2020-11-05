package furhatos.app.mathhelper.flow

import furhatos.app.mathhelper.nlu.*
import furhatos.records.User
import furhatos.nlu.common.*

class KidInfo(
        var name: PersonName? = null,
        var goldstars : Int = 0, // number of correct answers
        var level : String? = null, // user level (beginner, intermediate, advanced)
        var correctInRow : Int = 0, // correct answers in a row if positive, wrong answers if negative
        var question : Int = 0 // number of questions answered by user (not including first 2)
)
val User.info : KidInfo
    get() = data.getOrPut(KidInfo::class.qualifiedName, KidInfo())