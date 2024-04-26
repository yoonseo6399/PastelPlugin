
import com.google.common.base.Predicate
import io.github.yoonseo.pastelplugin.debug
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
@Deprecated(message = "it will replaced with Selector and other thing", level = DeprecationLevel.WARNING)
object RayCast {

    inline fun <reified T : Entity> run(startingPoint: Location, direction: Vector, detectionRange: Double, range: Double,crossinline except: (Entity) -> Boolean = {false}): List<T>? {
        val location = startingPoint.clone()
        var distance = 0.0
        direction.multiply(0.25)
        val world = location.world
        while (distance < range){
            location.add(direction)
            distance += 0.25
            val get = world.getNearbyEntities(location,detectionRange,detectionRange,detectionRange) { it is T && !except(it) }
            @Suppress("UNCHECKED_CAST")
            if(!get.isEmpty()) return get as List<T>
        }
        return null
    }
    inline fun <reified T : Entity> runWithCode(startingPoint: Location, direction: Vector, detectionRange: Double, range: Double, crossinline except: (Entity) -> Boolean = {false}, code : (Location) -> Unit): List<T>? {
        val location = startingPoint.clone()
        var distance = 0.0
        val directionn = direction.clone().multiply(0.25)
        val world = location.world
        while (distance < range){
            code(location.clone())

            //debug("nomalrun $location + $directionn = ${location.add(directionn).also { location = it }}")
            location.add(directionn)
            distance += 0.25
            val get = try {
                world.getNearbyEntities(location,detectionRange,detectionRange,detectionRange) { it is T && !except(it) }
            }catch (e : IllegalArgumentException){
                debug("Error")
                e.printStackTrace()
                return null
            }
            //debug(distance)
            @Suppress("UNCHECKED_CAST")
            if(!get.isEmpty()) return get as List<T>
        }
        return null
    }
    inline fun <reified T : Block> runBlock(startingPoint: Location, direction: Vector, range: Double,predicate: (T) -> Boolean = {true}): T? {
        val location = startingPoint.clone()
        var distance = 0.0
        direction.multiply(0.25)
        val world = location.world
        while (distance < range){
            location.add(direction)
            distance += 0.25
            world.getBlockAt(location).takeIf { it is T && it.type != Material.AIR}?.let { return it as T }
        }
        return null
    }


}
infix fun Player.isLookedAt(entity: Player): Boolean =
        RayCast.run<Entity>(location, location.direction, 1.0, 100.0)?.contains(entity) ?: false

fun Player.rayCast(detectionRange: Double, range: Double) =
        RayCast.run<LivingEntity>(location, location.direction, detectionRange, range) { it.uniqueId == uniqueId }