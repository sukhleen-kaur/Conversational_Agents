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
        return listOf("Citizen", "Suite: suite, sweet, suit")
    }
}
/* User provides the further details */
class ProvideFurtherDetails(var guestName : PersonName? = null, var duration: Number? = null, var roomType : Room? = null):Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@guestName, @duration, @roomType",
                "@guestName, @duration days, @roomType class",
                "My name is @guestName, I would like to stay for @duration days in the @roomType class",
                "I am @guestName, staying @duration days in a @roomType class room")
    }
}
/*List for saving the activites of the user*/
class ActivityList : ListEntity<Activities>()

/*Possible activities*/
class Activities : EnumEntity(stemming = true, speechRecPhrases = true){
    override fun getEnum(lang: Language): List<String> {
        return listOf("Skiing", "Tennis", "Badminton", "Zombie Survival")
    }
}
/*Uer provides the activities they want to do*/
class ProvideActivities(var activities : ActivityList? = null):Intent(){
    override fun getExamples(lang: Language): List<String>{
        return listOf("@activities",
        "I would like to do @activities")
    }
}

/*User wants to start over*/
class StartOver : Intent(){
    override fun getExamples(lang: Language): List<String> { // user asking to check in
        return listOf("I would like to start over",
                "Start over",
                "I'd like to check in again",
                "Yes", "Yes, start over")
    }
}

/*List for saving the wishes of the user*/
class WishList : ListEntity<WishEntity>()

class WishEntity: WildcardEntity("wish", GetWish())

/*User tells their wishes*/
class GetWish(): Intent() {
    var wish: WishEntity?= null
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I would like @wish", "@wish please", "@wish", "yes, @wish", "I want @wish", "Can I @wish", "May I @wish"
       );
    }

}



