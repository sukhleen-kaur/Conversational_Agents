package furhatos.app.spaceassist.nlu

import furhatos.nlu.*
import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number
import furhatos.nlu.common.PersonName
import furhatos.util.Language

/* User wants to check in */
class CheckIn: Intent() {
    override fun getExamples(lang: Language): List<String> { // user asking to check in
        return listOf("Yes, I would like to check in",
                "I would like to check in",
                "Check in")
    }
}

/* User does not know what this is about */
class Confused: Intent(){
    override fun getExamples(lang: Language): List<String> { // user does not know what is going on
        return listOf("Who are you?",
                "Where am I?",
                "What is this?",
                "What?")
    }
}

/* User provides the number of guests */
class ProvideGuestNumber(var guestNo : Number? = null):Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf("@guestNo",
                "@guestNo guests",
                "@guestNo people",
                "@guestNo of us")
    }
}
/* Class for the type of rooms */
class Room : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Citizen", "Suite")
    }
}
/* User provides the further details */
class ProvideFurtherDetails(var guestName : PersonName? = null, var duration: Number? = null, var roomType : Room? = null):Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@guestName, @duration, @roomType",
                "@guestName, @duration days, @roomType class",
                "My name is @guestName, I would like to stay for @duration days in the @roomType class")
    }
}
class ActivityList : ListEntity<Activities>()

class Activities : EnumEntity(stemming = true, speechRecPhrases = true){
    override fun getEnum(lang: Language): List<String> {
        return listOf("Skiing", "Tennis", "Badminton", "Zombie Survival")
    }
}

class ProvideActivities(var activities : ActivityList? = null):Intent(){
    override fun getExamples(lang: Language): List<String>{
        return listOf("@activities",
        "I would like to do @activities")
    }
}

class StartOver : Intent(){
    override fun getExamples(lang: Language): List<String> { // user asking to check in
        return listOf("I would like to start over",
                "Start over",
                "I'd like to check in again",
                "Yes", "Yes, start over")
    }
}



