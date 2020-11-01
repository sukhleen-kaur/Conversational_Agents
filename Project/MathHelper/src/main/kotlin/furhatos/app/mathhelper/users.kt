package furhatos.app.mathhelper.flow

import furhatos.app.mathhelper.nlu.*
import furhatos.records.User
import furhatos.nlu.common.*

class KidInfo(
        var name: PersonName? = null,
        var goldstars : Int = 0,
        var level : String? = null,
        var correctInRow : Int = 0,
        var question : Int = 0
)
val User.info : KidInfo
    get() = data.getOrPut(KidInfo::class.qualifiedName, KidInfo())