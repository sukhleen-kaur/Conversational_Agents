package furhatos.app.mathhelper

import furhatos.app.mathhelper.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class MathhelperSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
