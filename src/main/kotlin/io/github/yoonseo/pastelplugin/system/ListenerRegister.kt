package io.github.yoonseo.pastelplugin.system

import io.github.yoonseo.pastelplugin.lib.isSubtypeOf
import io.github.yoonseo.pastelplugin.plugin
import io.github.yoonseo.pastelplugin.projectPath
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.io.File
import kotlin.reflect.KClass

fun Listener.register() {
    Bukkit.getPluginManager().registerEvents(this, plugin)
}

object ListenerRegister {
    val list = ArrayList<Listener>()

    @ExperimentalStdlibApi
    fun registerAllEvents(){
        val r = loadClassesFromPackage("pastelplugin")
        val t = loadClassesFromPackage(projectPath)
        t.filter { it isSubtypeOf Listener::class }.forEach{ list + it.constructors.first().call() }
    }
    fun registerEvent(name: String){
        val t = Class.forName(name).constructors[0].newInstance() as Listener
        t.register()
        list + t
    }

    private fun loadClassesFromPackage(packageName: String): List<KClass<*>> {
        val classLoader = Thread.currentThread().contextClassLoader
        val packagePath = packageName.replace(".", File.separator)
        val packageUrl = classLoader.getResource(packagePath)

        val classes = mutableListOf<Class<*>>()

        if (packageUrl != null) {
            val packageFile = File(packageUrl.file)
            if (packageFile.isDirectory) {
                val classFiles = packageFile.listFiles { file ->
                    file.isFile && file.name.endsWith(".class")
                }
                classFiles ?: Bukkit.getLogger().info("Oh CannotFind Package")

                classFiles?.forEach { classFile ->
                    val className = packageName + '.' + classFile.nameWithoutExtension
                    try {
                        val clazz = Class.forName(className)
                        classes.add(clazz)
                        Bukkit.getLogger().info("loading class: ${clazz.name},$className,$classFile")
                    } catch (e: ClassNotFoundException) {
                        // 클래스를 로드할 수 없는 경우 처리
                        e.printStackTrace()
                    }
                }
            }
        }

        return classes.map { it.kotlin }
    }

}