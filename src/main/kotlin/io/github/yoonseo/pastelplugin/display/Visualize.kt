package io.github.yoonseo.pastelplugin.display

import io.github.yoonseo.pastelplugin.ScheduleRepeating
import org.bukkit.Location
import org.bukkit.util.Vector

fun Vector.visualize(loc : Location,remainTick: Int) {
    ScheduleRepeating { loc }
}