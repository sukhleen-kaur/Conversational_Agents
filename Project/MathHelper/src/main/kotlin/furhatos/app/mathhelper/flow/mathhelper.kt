package furhatos.app.mathhelper.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.mathhelper.nlu.*
import furhatos.flow.kotlin.voice.CereprocVoice
import furhatos.gestures.*
import furhatos.nlu.common.Number
import kotlin.random.Random

val Sadness = defineGesture("MySmile") {
    frame(0.32, 0.72) {
        BasicParams.EXPR_SAD to 0.5
    }
    frame(0.2, 0.72){
        BasicParams.BROW_DOWN_LEFT to 2.0
        BasicParams.BROW_DOWN_RIGHT to 2.0
    }
    frame(0.16, 0.72){
        BasicParams.BLINK_LEFT to 0.1
        BasicParams.BLINK_RIGHT to 0.1
    }
    reset(1.04)
}

val Start = state(Interaction) {
    onEntry {
        furhat.gesture(Gestures.BigSmile)
        random(
                {   furhat.say("Hey there!") },
                {   furhat.say("Oh, howdy there!") }
        )


        goto(Welcome)
    }
}

val Welcome : State = state(Interaction){
    onEntry {
        furhat.ask("Are you ready to learn some math?")
    }
    onResponse<Yes> {
        goto(AskKidName)
    }
    onResponse<No>{
        goto(Scared)
    }
    onResponse<Confused>{
        goto(Info)
    }
}

val Info = state(Interaction){
    onEntry{
        furhat.gesture(Gestures.BigSmile)
        furhat.say("My name is Sally and I am your math tutor! I am here to help you learn " +
                "subtraction and addition.")
        furhat.ask("Are you ready?")
    }
    onResponse<Yes> {
        goto(AskKidNameInfo)
    }
    onResponse<No>{
        goto(Scared)
    }
}

val Scared = state(Interaction){
    onEntry {
        furhat.gesture(Gestures.Smile)
        furhat.say("It's okay, it will only take a couple of minutes. We will have fun learning!")
        furhat.ask("Are you ready now?")
    }
    onResponse<Yes> {
        goto(AskKidName)
    }
    onResponse<No> {
        furhat.say("Aww. I am sad to see you go. But you have a fun day! See you another time! Bye Bye!")
        goto(Idle)
    }
}

val KidName = state(Interaction){
    onResponse<ProvideName>{
        val name = it.intent.name
        if (name != null){
            users.current.info.name = name
            goto(GetStarted)
        }
    }
}

val AskKidName = state(KidName){
    onEntry {
        //furhat.say("Great! My name is Sally and today I will ask you a few questions to help you learn math. If you get a question correct, you will get 1 gold star!")
        furhat.ask("Now, before we begin, please tell me your name.")
    }
    onReentry {
        furhat.ask("What is your name?")
    }

}

val AskKidNameInfo = state(KidName){
    onEntry {
        furhat.say("Great! Today I will ask you a few questions to help you learn math. If you get a question correct, you will get 1 gold star!")
        furhat.ask("Now, before we begin, please tell me your name.")
    }
    onReentry {
        furhat.ask("What is your name?")
    }

}

val GetStarted = state(Interaction){
    onEntry {
        furhat.say("Okay then, ${users.current.info.name}, let's get to it!")
        goto(FirstQuestion)
    }
}

/*----------- INITIAL QUESTIONS ---------------*/

val FirstQuestion : State = state(Interaction){
    val first = Random.nextInt(1, 6)
    val second = Random.nextInt(1, 6)
    val question = AdditionWordQuestion(first, second)
    val answer = first + second
    onEntry {
        println(first)
        println(second)
        println(question)
        furhat.ask(question)
        furhat.listen(timeout = 10000)
    }

    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            furhat.say("$userAnswer! That is correct! Good job, ${users.current.info.name}! Let's try another one!")
            users.current.info.goldstars += 1
            goto(SecondQuestionCorrect)

        } else {
            furhat.say("The correct answer is ${answer}. Let's try another one!")
            goto(SecondQuestionWrong)
        }
        //goto(GetStarted)
    }
    onResponse<DontKnow> {
        furhat.say("The correct answer is ${answer}. Let's try another one!")
        goto(SecondQuestionWrong)
    }
}

val SecondQuestionWrong = state(Interaction){
    var first = Random.nextInt(1, 11)
    var second = Random.nextInt(1, 11)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    //val question = EquationQuestion(first, second, "-")
    val answer = first - second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "\u2212"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            furhat.say("$userAnswer! That is correct! Good job, ${users.current.info.name}! Let's try another one!")
            users.current.info.goldstars += 1
            users.current.info.level = "Intermediate"
            goto(ChooseQuestion)
        } else {
            furhat.say("The correct answer is ${answer}. Let's try another one!")
            users.current.info.level = "Beginner"
            goto(ChooseQuestion)
        }
    }
    onResponse<DontKnow> {
        furhat.say("The correct answer is ${answer}. Let's try another one!")
        users.current.info.level = "Beginner"
        goto(ChooseQuestion)
    }
}

val SecondQuestionCorrect = state(Interaction){
    var first = Random.nextInt(1, 21)
    var second = Random.nextInt(1, 21)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    //val question = EquationQuestion(first, second, "-")
    val answer = first - second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "\u2212"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            furhat.say("$userAnswer! That is correct! Good job, ${users.current.info.name}! Let's try another one!")
            users.current.info.goldstars += 1
            users.current.info.level = "Advanced"
            goto(ChooseQuestion)
        } else {
            furhat.say("The correct answer is ${answer}. Let's try another one!")
            users.current.info.level = "Intermediate"
            goto(ChooseQuestion)
        }
    }
    onResponse<DontKnow> {
        furhat.say("The correct answer is ${answer}. Let's try another one!")
        users.current.info.level = "Intermediate"
        goto(ChooseQuestion)
    }
}

/* --------------------------------- SIMPLE QUESTIONS -----------------------------------*/
/*--------- WORD ---------*/

val SimpleWordAddition : State = state(Interaction){
    val first = Random.nextInt(1, 6)
    val second = Random.nextInt(1, 6)
    val question = AdditionWordQuestion(first, second)
    val answer = first + second
    onEntry {
        println(first)
        println(second)
        println(question)
        furhat.ask(question)
        furhat.listen(timeout = 15000)
    }

    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)

        } else {
            goto(UpdateWrong)
        }

    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}


val SimpleWordSubtraction = state(Interaction){
    var first = Random.nextInt(1, 11)
    var second = Random.nextInt(1, 11)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    val question = SubtractionWordQuestion(first, second)
    val answer = first - second
    onEntry {
        furhat.ask(question)
        furhat.listen(timeout = 15000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

/*--------- NON-WORD ---------*/

val SimpleAddition = state(Interaction){
    val first = Random.nextInt(1, 6)
    val second = Random.nextInt(1, 6)
    val answer = first + second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "+"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

val SimpleSubtraction = state(Interaction){
    var first = Random.nextInt(1, 11)
    var second = Random.nextInt(1, 11)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    //val question = EquationQuestion(first, second, "-")
    val answer = first - second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "\u2212"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

/* --------------------------------- INTER QUESTIONS -----------------------------------*/
/*--------- WORD ---------*/

val InterWordAddition = state(Interaction){
    var first = Random.nextInt(1, 11)
    var second = Random.nextInt(1, 11)

    val question = AdditionWordQuestion(first, second)
    val answer = first + second
    onEntry {
        furhat.ask(question)
        furhat.listen(timeout = 15000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

val InterWordSubtraction = state(Interaction){
    var first = Random.nextInt(1, 21)
    var second = Random.nextInt(1, 21)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    val question = SubtractionWordQuestion(first, second)
    val answer = first - second
    onEntry {
        furhat.ask(question)
        furhat.listen(timeout = 15000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}


/*--------- NON-WORD ---------*/
val InterAddition = state(Interaction){
    var first = Random.nextInt(1, 11)
    var second = Random.nextInt(1, 11)

    val answer = first + second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "+"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

val InterSubtraction = state(Interaction){
    var first = Random.nextInt(1, 21)
    var second = Random.nextInt(1, 21)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    val answer = first - second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "\u2212"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

/* --------------------------------- ADVANCED QUESTIONS -----------------------------------*/
/*--------- WORD ---------*/
val AdvWordAddition = state(Interaction){
    var first = Random.nextInt(1, 51)
    var second = Random.nextInt(1, 51)

    val question = AdditionWordQuestion(first, second)
    val answer = first + second
    onEntry {
        furhat.ask(question)
        furhat.listen(timeout = 15000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

val AdvWordSubtraction = state(Interaction){
    var first = Random.nextInt(1, 101)
    var second = Random.nextInt(1, 101)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    val question = SubtractionWordQuestion(first, second)
    val answer = first - second
    onEntry {
        furhat.ask(question)
        furhat.listen(timeout = 15000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}


/*--------- NON-WORD ---------*/
val AdvAddition = state(Interaction){
    var first = Random.nextInt(1, 51)
    var second = Random.nextInt(1, 51)

    val answer = first + second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "+"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}
val AdvSubtraction = state(Interaction){
    var first = Random.nextInt(1, 101)
    var second = Random.nextInt(1, 101)
    if (first < second){
        // make sure first variable never smaller than the second value
        first = second.also{second = first}
    }
    val answer = first - second
    onEntry {
        furhat.ask(EquationQuestion(first, second, "\u2212"))
        furhat.listen(timeout = 10000)
    }
    onResponse<Number> {
        val userAnswer = it.intent.value
        if (userAnswer == answer){
            goto(UpdateCorrect)
        } else {
            goto(UpdateWrong)
        }
    }
    onResponse<DontKnow> {
        goto(UpdateWrong)
    }
}

/* --------------------------------- GOODBYE -----------------------------------*/
val GoodBye : State = state(Interaction){
    onEntry {
        furhat.say("Good job, ${users.current.info.name}! You get ${users.current.info.goldstars} goldstars for your hard work today!")
        furhat.ask("Did you have fun today?")
    }
    onResponse<Yes> {
        furhat.say("I am so happy to hear that! See you later alligator!")
        goto(Idle)
    }
    onResponse<No> {
        furhat.say("Awww, that's too bad. Maybe it will be fun next time!")
    }
}

/* --------------------------------- QUESTIONS -----------------------------------*/

fun EquationQuestion(first: Int, second: Int, sign: String) = utterance {
    random{
        +"What is ${first} ${sign} ${second}?"
        +"What does ${first} ${sign} ${second} equal to?"
    }
}

fun AdditionWordQuestion(first: Int, second: Int): String {
    val questions = arrayOf(
            "Alex has ${first} ${if(first == 1){"apple"}else{"apples"}}. Zoe has ${second} ${if(second == 1){"apple"}else{"apples"}}. How many apples do they have together?",
            "David's pet snake is ${first} ${if(first == 1){"metre"}else{"metres"}} long. Monica's pet snake is ${second} ${if(second == 1){"metre"}else{"metres"}} longer than David's. How long is Monica's pet snake?",
            "Eli has two bookshelves. On the first bookshelf, he has ${first} ${if(first == 1){"book"}else{"books"}}. On the second bookshelf, he has ${second} ${if(second == 1){"book"}else{"books"}}. How may books does he have all together?",
            "Joey went to the zoo today and saw ${first} ${if(first == 1){"monkey"}else{"monkeys"}}. That is ${second} fewer than he saw last Saturday. How many monkeys did Joey see last Saturday?",
            "Abby has ${first} ${if(first == 1){"lollipop"}else{"lollipops"}} old. Her brother Ben has ${second} more ${if(second == 1){"lollipop"}else{"lollipops"}} than she does. How old is Ben?",
            "Jack swam ${first} ${if(first == 1){"lap"}else{"laps"}}. He swam ${second} fewer ${if(second == 1){"lap"}else{"laps"}} than Lea. How many laps did Lea swim?",
            "Ann and Rachel love ice cream with cherries on top. Ann puts ${first} ${if(first == 1){"cherry"}else{"cherries"}} on hers. Ann had ${second} fewer ${if(second == 1){"cherry"}else{"cherries"}} than Rachel. How many cherries does Rachel have?",
            "King Arthur had ${first} gold ${if(first == 1){"coin"}else{"coins"}} and ${second} silver ${if(second == 1){"coin"}else{"coins"}}. How many coins did he have in all?",
            "Eli had too many fish in his old fish tank. He got a new fish tank and moved ${first} fish from the old tank to the new tank. He still had ${second} fish in his old tank. How many fish were in Eli's old fish tank at the start?",
            "A giant and a dragon live next door to each other. The giant's house is ${first} ${if(first == 1){"metre"}else{"metres"}} tall. His house is ${second} ${if(second == 1){"metre"}else{"metres"}} shorter than the dragon's house. How tall is the dragon's house?",
            "Momma chipmunk had some acorns. Her babies ate ${first} of the ${if(first == 1){"acorn"}else{"acorns"}}. Then she ate the ${second} of the ${if(second == 1){"acorn"}else{"acorns"}} that were left. What is the total number of acorns that Momma chipmunk had?"
    )
    return questions.random()
}

fun SubtractionWordQuestion(first: Int, second: Int): String {
    val questions = arrayOf(
            "The King gave Jerry ${first} gold ${if(first == 1){"coin"}else{"coins"}}. He gave Elaine ${second} fewer ${if(second == 1){"coin"}else{"coins"}} than he gave Jerry. How many coins did Elaine get?",
            "Sue is ${first} ${if(first == 1){"year"}else{"years"}} old. Her brother George is ${second} ${if(second == 1){"year"}else{"years"}} old. How many years older is Sue than George?",
            "Ross has ${first} ${if(first == 1){"dinosaur"}else{"dinosaurs"}}. Ted has ${second} ${if(second == 1){"dinosaur"}else{"dinosaurs"}}. How many fewer dinosaurs does Ted have than Ross?",
            "Fiona fought a dragon with ${first} ${if(first == 1){"head"}else{"heads"}}. Then she fought another dragon with ${second} ${if(second == 1){"head"}else{"heads"}} fewer heads than the first dragon. How many heads did the second dragon have?",
            "James has ${second} ${if(second == 1){"pencil"}else{"pencils"}}. Dave has ${first} ${if(first == 1){"pencil"}else{"pencils"}}. How many fewer pencils does James have than Dave?",
            "Yuri read ${first} ${if(first == 1){"book"}else{"books"}}. Hiro read ${second} ${if(second == 1){"book"}else{"books"}}. How many more books did Yuri read than Hiro?",
            "Yesterday Ali caught ${second} ${if(second == 1){"bug"}else{"bugs"}}. Today he caught ${first} ${if(first == 1){"bug"}else{"bugs"}}. How many fewer bugs did Ali catch yesterday the today?",
            "Sam rode a bull for ${first} ${if(first == 1){"second"}else{"seconds"}}. Maddie rode a bull for ${second} fewer ${if(second == 1){"second"}else{"seconds"}} than Sam did. How long did Maddie ride her bull?",
            "Phoebe jumped ${first} ${if(first == 1){"metre"}else{"metres"}}. Aria jumped ${second} ${if(second == 1){"metre"}else{"metres"}}. How much farther did Phoebe jump than Aria?",
            "Ross's class has ${first} ${if(first == 1){"crayon"}else{"crayons"}}. At the end of the year only ${second} ${if(second == 1){"crayon"}else{"crayons"}} remain. How many crayons have been used?",
            "In a basketball game, Ross scored ${second} fewer ${if(second == 1){"point"}else{"points"}} than Chandler. Chandler scored ${first} ${if(first == 1){"point"}else{"points"}}. How many points did Ross score?",
            "Vic used ${first} ${if(first == 1){"marker"}else{"markers"}} to draw his pictures. His friend Susan drew her picture with ${second} fewer ${if(second == 1){"marker"}else{"markers"}} than Vic. How many markers did Susan use?",
            "Sparky the dragon was born with ${second} ${if(second == 1){"spike"}else{"spikes"}}. He grew several more spikes as he got older. Now sparky has ${first} ${if(first == 1){"spike"}else{"spikes"}}. How many new spikes did Sparky grow?",
            "There are ${first} ${if(first == 1){"camper"}else{"campers"}} at Fun Camp. Only ${second} ${if(second == 1){"camper"}else{"campers"}} brought their sleeping bags. How many campers did not bring sleeping bags?",
            "Mary has a collection of shark teeth. When her uncle returns from vacation, he brings her ${second} more shark ${if(second == 1){"tooth"}else{"teeth"}}. Now Mary has ${first} shark ${if(first == 1){"tooth"}else{"teeth"}}. How many did Mary have before her uncle's gift?"

    )
    return questions.random()
}


val UpdateCorrect : State = state(Interaction){
    onEntry {
        users.current.info.question += 1
        users.current.info.goldstars += 1
        furhat.say("Well done!")
        // update correct questions in row
        if(users.current.info.correctInRow < 0){
            users.current.info.correctInRow = 1
        } else {
            users.current.info.correctInRow += 1
        }
        // update level if needed
        if(users.current.info.correctInRow == 3){
            users.current.info.correctInRow = 0
            if (users.current.info.level == "Beginner"){
                users.current.info.level = "Intermediate"
            } else if(users.current.info.level == "Intermediate"){
                users.current.info.level = "Advanced"
            }
        }
        if(users.current.info.question < users.current.info.maxQuestions){
            goto(ChooseQuestion)
        }
        else{
            goto(GoodBye)
        }


    }
}

val UpdateWrong : State = state(Interaction){
    onEntry {
        users.current.info.question += 1
        furhat.say("Let's try again!")
        // update correct questions in row
        if(users.current.info.correctInRow > 0){
            users.current.info.correctInRow = -1
        } else {
            users.current.info.correctInRow -= 1
        }
        // update level if needed
        if(users.current.info.correctInRow == -3){
            users.current.info.correctInRow = 0
            if (users.current.info.level == "Intermediate"){
                users.current.info.level = "Beginner"
            } else if(users.current.info.level == "Advanced"){
                users.current.info.level = "Intermediate"
            }
        }
        if(users.current.info.question < users.current.info.maxQuestions){
            goto(ChooseQuestion)
        }
        else{
            goto(GoodBye)
        }



    }
}

val ChooseQuestion : State = state(Interaction){
    val questiontype = Random.nextInt(0, 4)
    onEntry {
        when (users.current.info.level) {
            "Beginner" -> {
                when(questiontype){
                    0 -> goto(SimpleAddition)
                    1 -> goto(SimpleWordAddition)
                    2 -> goto(SimpleSubtraction)
                    else -> {
                        goto(SimpleWordSubtraction)
                    }
                }

            }
            "Intermediate" -> {
                when(questiontype){
                    0 -> goto(InterAddition)
                    1 -> goto(InterWordAddition)
                    2 -> goto(InterSubtraction)
                    else -> {
                        goto(InterWordSubtraction)
                    }
                }
            }
            "Advanced" -> {
                when(questiontype){
                    0 -> goto(AdvAddition)
                    1 -> goto(AdvWordAddition)
                    2 -> goto(AdvSubtraction)
                    else -> {
                        goto(AdvWordSubtraction)
                    }
                }
            }
        }
    }

}

