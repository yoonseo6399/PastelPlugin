package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.Delay
import io.github.yoonseo.pastelplugin.debug

class CooldownWithTask(val cooldownTick: Int,val task : ()-> Unit) {
    private var isAvailable = true
    operator fun invoke(): Boolean{
        return if(isAvailable) {

            task()

            isAvailable = false
            Delay(cooldownTick.toLong()){ isAvailable=true;debug("cooldown comple") }
            true
        }else false
    }

}