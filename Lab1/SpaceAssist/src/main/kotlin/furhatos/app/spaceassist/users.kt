package furhatos.app.spaceassist

import furhatos.app.spaceassist.nlu.ActivityList
import furhatos.app.spaceassist.nlu.Room
import furhatos.records.User
import furhatos.nlu.common.Number
import furhatos.nlu.common.PersonName

/* usser data */
class CheckInData(
        var guestNo : Number? = null,
        var guestName : PersonName? = null,
        var duration : Number? = null,
        var roomType : Room? = null,
        var activities : ActivityList = ActivityList()
)

val User.order : CheckInData
    get() = data.getOrPut(CheckInData::class.qualifiedName, CheckInData())