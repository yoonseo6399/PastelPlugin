package io.github.yoonseo.pastelplugin.lib

import kotlin.reflect.KClass

infix fun KClass<*>.isSubtypeOf(classB: KClass<*>) = java.isAssignableFrom(classB.java)