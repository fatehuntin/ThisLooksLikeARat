package floppaclient.module.impl.misc

import floppaclient.FloppaClient.Companion.mc
import floppaclient.module.Category
import floppaclient.module.Module
import floppaclient.module.SelfRegisterModule
import floppaclient.module.settings.impl.BooleanSetting
import floppaclient.module.settings.impl.StringSetting
import floppaclient.utils.ChatUtils.modMessage
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent

val client = HttpClient()


/**
 * A simple example of a Module written in Kotlin.
 *
 * In Kotlin Modules are declared as objects inheriting from the [Module] class.
 * The [SelfRegisterModule] annotation is required for this Module to be registered by the ModuleManager.
 * Specify the name, category and description of your module when delegating to the [Module] constructor.
 * Keep in mind that the name of your Module has to be unique!
 *
 * You can use this as a template for your own Modules.
 * The documentation of the members of this class should help you understand what everything does.
 *
 * Refer to the [Module] documentation for more information about Modules.
 * @author Aton
 */
@SelfRegisterModule
object ExternalModule : Module(
    "External Module",
    category = Category.MISC,
    description = "An external module"
) {
    /**
     * Here a Setting is added to your Module.
     *
     * This property is defined through delegation.
     *
     * **Remember to register your Setting**. Otherwise it will not appear in the GUI.
     * In this case this is done automatically by the provider of the delegate.
     * If you do not use delegation use the register method.
     *
     *     private val mySetting = register(BooleanSetting("My Setting"))
     * Or the operator form using +
     *
     *     private val mySetting = +BooleanSetting("My Setting")
     * You can remove or replace this Setting if you don't need it.
     */
    private val URL = StringSetting("URL", description = "This is the url for the server that you will be authenticating to")
    private val uuid = StringSetting("uuid", description = "UUID associated with the minecraft account you're on. Get from namemc.com")
    private val authcode = StringSetting("Auth code", description = "Authentication code")
    private val player = StringSetting("Player", description = "Who you are (not ur mc username maybe to avoid confusion)")
    init {
        this.addSettings(
            URL,
            player
        )
    /**
     * An Event listener for your Module.
     *
     * Methods annotated with [SubscribeEvent] are event listeners.
     * Such a method listens for the event specified by the Type of the single parameter of the method.
     * In this case that event is the Forge [ClientChatReceivedEvent].
     * Whenever that event occurs Forge will invoke this method and the code will be run.
     *
     * The event listeners in your Module will only be active when the Module is enabled.
     * You can make them always active by annotation the class with [AlwaysActive][floppaclient.module.AlwaysActive].
     */
    @SubscribeEvent
    suspend fun onGameOpen(event: ClientConnectedToServerEvent) {
        val response: HttpResponse = client.request(URL.value.toString()) {
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody((authcode.value.toString() + uuid.value.toString() + player.value.toString()))
        }
    }
}}