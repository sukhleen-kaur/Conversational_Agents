package furhatos.app.mathhelper.flow

import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.CereprocVoice
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.flow.kotlin.voice.PollyVoice
import furhatos.util.*

val Idle: State = state {

    init {
        furhat.setTexture("Rene")
        furhat.voice = PollyNeuralVoice(language= Language.ENGLISH_US, gender=Gender.FEMALE, pitch = "high")
        furhat.voice = PollyNeuralVoice.Salli()

        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

val Interaction: State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }

}