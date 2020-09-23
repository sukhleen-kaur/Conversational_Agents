package furhatos.app.spaceassist.flow

import furhatos.app.spaceassist.nlu.*
import furhatos.app.spaceassist.order
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*
import furhatos.nlu.common.Number

var AvailableCitizenRoom = 3
var AvailableSuiteRoom = 3

/* When the user enter, initial greeting */
val Start = state(Interaction) {
    onEntry {
        random(
                {   furhat.say("Hi there") },
                {   furhat.say("Oh, hello there") }
        )

        goto(WelcomeAboard)
    }
}

/* Ask initial question  */
val WelcomeAboard : State = state(Interaction) {
    onEntry {
        furhat.ask("How can I help you?")
    }
    // user wants to check in
    onResponse<CheckIn> {
        goto(CheckInIntro)
    }
    // user is confused
    onResponse<Confused> {
        goto(RobotInfo)
    }
}

/* If confused, provide the info about the voyage and ask if they wanna check in */
val RobotInfo = state(Interaction){
    onEntry {
        furhat.say("Welcome to Starship Enterprise. We are currently leaving for a 12 day voyage " +
                "from planet Earth to planet Vulkan. My name is Data and I am your check-in assistant " +
                "for today.")
        furhat.ask("Would you like to check in?")
    }
    // if yes, go ask if the assistant can ask more questions
    onResponse<Yes>{
        goto(CheckInIntro)
    }
    onResponse<CheckIn> {
        goto(CheckInIntro)
    }
    // if no, then say goodbye and return to initial state
    onResponse<No> {
        furhat.say("Goodbye then.")
        goto(Idle)
    }
}

/* Ask if assistant can ask regulation questions */
val CheckInIntro = state(Interaction){
    onEntry {
        furhat.say("Great! As the travel is longer than 2 days on our journey to Vulkan, " +
                "regulation requires we ask a few questions." )
        furhat.ask("Is that okay with you?")
    }
    onResponse<Yes> {
       goto(AskGuestNo)
    }
    onResponse<No> {
        goto(GuestInfoNot)
    }
}

/* Reassure the user that they cannot check-in if they do not answer the questions */
val GuestInfoNot = state(Interaction){
    onEntry {
        furhat.say("Without your information, I cannot book you in.")
        furhat.ask("Are you sure?")
    }
    onResponse<Yes> {
        furhat.say("Goodbye then.")
        goto(Idle)
    }
    onResponse<No> {
        goto(AskGuestNo)
    }
}

/* Save the number of guests of the user */
val GuestNo = state(Interaction){
    onResponse<ProvideGuestNumber> {
        val guestNo = it.intent.guestNo
        if (guestNo != null){
            users.current.order.guestNo = guestNo
            goto(Amenities)
        } else {
            propagate()
        }

    }
}

val ChangedGuestNo: State = state(Interaction){
    onResponse<ProvideGuestNumber> {
        val guestNo = it.intent.guestNo
        if (guestNo != null){
            users.current.order.guestNo = guestNo
            if(CheckRoomAvailability(users.current.order.roomType.toString(), users.current.order.guestNo)){
                UpdateAvailableRooms(users.current.order.roomType.toString(), users.current.order.guestNo)
                goto(AskWishes)
            } else{
                goto(NotEnoughRooms)
            }
        } else {
            propagate()
        }

    }
}

/* Ask the number of guests the user has */
val AskGuestNo = state(GuestNo){
    onEntry {
        furhat.say("Let's get started then.")
        furhat.ask("How many people would you like to check in?")
    }
    onResponse<No> {
        goto(EndState)
    }
}

/* Ask if the user wants to know about the amenities */
val Amenities = state(Interaction){
    onEntry {
        furhat.say("Great!")
        furhat.ask("By the way, would you like to know about the " +
                "available amenities in our rooms?")
    }
    onResponse<Yes> {
        goto(AmenitiesInfo)
    }
    onResponse<No> {
        goto(AskFurtherDetails)
    }
}

/* Give the amenities */
val AmenitiesInfo = state(Interaction){
    onEntry {
        furhat.say("You are provided a bed, a table, " +
                "a chair, and a Replicator, which allows you to instantly " +
                "create any dish you have ever wanted to eat, in the comfort of " +
                "your own room.")
        goto(AskFurtherDetails)
    }
}

/* Save the user's futher details */
val FurtherDetails = state(Interaction){
    onResponse<ProvideFurtherDetails> {
        val guestName = it.intent.guestName
        val duration = it.intent.duration
        val roomType = it.intent.roomType
        if (guestName != null && duration != null && roomType != null){
            users.current.order.guestName = guestName
            users.current.order.duration = duration
            users.current.order.roomType = roomType
            furhat.say("${CheckRoomAvailability(roomType.toString(), users.current.order.guestNo)}")
            println(roomType.toString())
            if(CheckRoomAvailability(roomType.toString(), users.current.order.guestNo)){
                UpdateAvailableRooms(roomType.toString(), users.current.order.guestNo)
                goto(AskWishes)
            } else{
                goto(NotEnoughRooms)
            }
        } else {
            propagate()
        }
    }
}

/* Ask the user for further details */
val AskFurtherDetails = state(FurtherDetails){
    onEntry {
        furhat.ask("Perfect! Now, could you give me your name, " +
                "how long you intend to stay on Starship Enterprise, " +
                "and whether you would like a Suite-class room " +
                "or a Citizen-class room? The Suite-class rooms have 2 beds " +
                "and the Citizen-class rooms have 1 bed.")
    }
}

val NotEnoughRooms = state(Interaction){
    onEntry{

        furhat.say("Unfortunately there are no rooms left of this kind. We only have " +
                "${if(users.current.order.roomType.toString() == "citizen"){AvailableCitizenRoom}else{AvailableSuiteRoom}} " +
                "rooms of this kind free.")
        furhat.ask("Would  you like to change the number of people you are checking in?")
    }
    onResponse<Yes> {
        goto(ChangeGuestNo)
    }
    onResponse<No> {
        goto(CheckInCancel)
    }
    onTime(delay=100000){
        goto(CheckInCancel)
    }
}

val ChangeGuestNo = state(ChangedGuestNo){
    onEntry {
        furhat.say("Wonderful.")
        furhat.ask("Please tell me how many guests you would like to check in.")
    }
}

val CheckInCancel = state(Interaction){
    onEntry {
        furhat.ask("Alright then, please tell me if you'd like to start over. Otherwise, I wish you a good day.")
    }
    onResponse<StartOver>{
        goto(WelcomeAboard)
    }
    onTime(delay=50000) {
        goto(Start)
    }

}

/* Ask user for their wishes */
val AskWishes = state(Interaction){
    onEntry {
        furhat.say("AFTER: So ${users.current.order.guestNo} guests then. We have ${AvailableCitizenRoom} Citizen rooms and ${AvailableSuiteRoom} Suite Rooms.")
        furhat.say("Amazing! The data has been entered to your name, " +
                "${users.current.order.guestName}.")
        furhat.ask("Now, before asking you about the different activities we offer on board, " +
                "I would like to ask you if you have any specific wishes for your stay here?")
    }
    onResponse<No> {
        goto(NoWishes)
    }
    onResponse<Yes>{
        furhat.ask("What are your wishes?")
    }

}

/*If there are no wishes*/
val NoWishes = state(Interaction){
    onEntry {
        furhat.say("Alright, then let's move on.")
        goto(AskActivities)
    }

}

val WhichActivities = state(Interaction) {
    onResponse<ProvideActivities> {
        val activities = it.intent.activities
        if (activities != null) {
            activities.list.forEach {
                users.current.order.activities.list.add(it)
            }
            goto(EndState)
        }
        else {
            propagate()
        }
    }
}

/* Ask if user interested in any of the activities */
val AskActivities = state(WhichActivities){
    onEntry {
        furhat.say("On Starship Enterprise we offer numerous simulated activities, namely: " +
                "${Activities().optionsToText()}.")
        furhat.ask("Please tell me which ones of those activities you would like to sign up for today.")
    }
    onResponse<No> {
        goto(EndState)
    }
}

val EndState = state(Interaction){
    onEntry {
        furhat.say("Understood. You have now successfully checked in. You will soon be teleported " +
                "to your room, and your luggage will be delivered by our staff. We hope your stay at Starship Enterprise " +
                "will be a fun and relaxing one.")
        goto(Idle)
    }
}

fun CheckRoomAvailability(roomType: String, guestNo: Number?): Boolean {
    val roomNo = CountRoomsNeeded(roomType, guestNo)
    var RoomAvailable = true
    if(roomType == "citizen"){
        RoomAvailable = (AvailableCitizenRoom - roomNo) >= 0
    } else if(roomType == "suite"){
        RoomAvailable = (AvailableSuiteRoom - roomNo) >= 0
    }
    return RoomAvailable
}

fun UpdateAvailableRooms(roomType: String, guestNo: Number?) {
    val guestNoInt = guestNo
    val roomNo = CountRoomsNeeded(roomType, guestNoInt)
    if(roomType == "citizen"){
        AvailableCitizenRoom -= roomNo
    } else if(roomType == "suite"){
        AvailableSuiteRoom -= roomNo
    }
}

fun CountRoomsNeeded(roomType: String, guestNo: Number?): Int {
    var roomNo = 0
    var guestNoStr = guestNo.toString()
    var guestNoInt = guestNoStr.toInt()
    if(roomType == "citizen"){
        roomNo = guestNoInt
    } else if(roomType == "suite"){
        roomNo = guestNoInt/2
        roomNo = guestNoInt/2
        if (guestNoInt%2 != 0){
            roomNo++
        }
    }
    return roomNo
}
