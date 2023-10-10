package io.github.yoonseo.pastelplugin.boss

import io.github.yoonseo.pastelplugin.Delay

class CooldownWithTask(val cooldownTick: Int,val task : ()-> Unit) {
    private var isAvailable = false
    operator fun invoke(): Boolean{
        return if(isAvailable) {
            task()
            isAvailable = false
            Delay(cooldownTick.toLong()){ isAvailable=true }
            true
        }else false
    }

}