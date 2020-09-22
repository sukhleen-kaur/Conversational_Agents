package furhatos.app.spaceassist

import furhatos.app.spaceassist.flow.Idle
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class SpaceAssistSkill: Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
