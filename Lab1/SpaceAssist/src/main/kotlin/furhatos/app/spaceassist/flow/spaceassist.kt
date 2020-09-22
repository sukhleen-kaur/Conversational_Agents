package furhatos.app.spaceassist.flow

import furhatos.app.spaceassist.nlu.*
import furhatos.app.spaceassist.order
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*
import furhatos.util.Language

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
val WelcomeAboard = state(Interaction) {
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
            goto(AskWishes)
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
                "or a Citizen-class room?")
    }
}

/* Ask user for their wishes */
val AskWishes = state(Interaction){
    onEntry {
        furhat.say("Amazing! The data has been entered to your name, " +
                "${users.current.order.guestName}.")
        furhat.ask("Now, before asking you about the different activities we offer on board, " +
                "I would like to ask you if you have any specific wishes for your stay here?")
    }
    onResponse<No> {
        goto(NoWishes)
    }
}

/*If there are no wishes*/
val NoWishes = state(Interaction){
    onEntry {
        furhat.say("Alright, then let's move on.")
        goto(AskActivities)
    }

}

/* Ask if user interested in any of the activities */
val AskActivities = state(Interaction){
    onEntry {
        furhat.say("On Starship Enterprise we offer numerous simulated activities, namely: " +
                "Skiing, Tennis, Badminton, and Zombie Survival. Please tell me which ones of those " +
                "activities you would like to sign up for today.")
    }
    onResponse<No> {
        goto(EndState)
    }
}

val EndState = state(Interaction){
    onEntry {
        furhat.say("Unerstood. You have now successfully checked in. You will soon be teleported " +
                "to your room, and your luggage will be delivered by our staff. We hope your stay at Starship Enterprise " +
                "will be a fun and relaxing one.")
        goto(Idle)
    }
}

