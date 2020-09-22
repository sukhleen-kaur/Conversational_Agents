package furhatos.app.spaceassist.nlu

import furhatos.nlu.*
import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number
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
class ProvideFurtherDetails(var guestName: String? = null, var duration: Number? = null, var roomType : Room? = null):Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@guestName, @duration, @roomType",
                "My name is @guestName, for @duration days and @roomType class")
    }
}

