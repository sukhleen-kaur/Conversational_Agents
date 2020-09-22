package furhatos.app.spaceassist

import furhatos.app.spaceassist.nlu.Room
import furhatos.records.User
import furhatos.nlu.common.Number

/* usser data */
class CheckInData(
        var guestNo : Number? = null,
        var guestName : String? = null,
        var duration : Number? = null,
        var roomType : Room? = null
)

val User.order : CheckInData
    get() = data.getOrPut(CheckInData::class.qualifiedName, CheckInData())