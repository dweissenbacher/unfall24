package de.unfall24

import de.unfall24.component.card
import de.unfall24.view.EditPanel
import de.unfall24.view.ListPanel
import io.kvision.*
import io.kvision.core.Background
import io.kvision.core.BgRepeat
import io.kvision.core.BgSize
import io.kvision.core.onEvent
import io.kvision.form.select.select
import io.kvision.html.*
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.navbar.NavbarExpand
import io.kvision.navbar.NavbarType
import io.kvision.navbar.navForm
import io.kvision.navbar.navbar
import io.kvision.panel.ContainerType
import io.kvision.panel.gridPanel
import io.kvision.panel.root
import io.kvision.panel.vPanel
import io.kvision.theme.ThemeManager
import io.kvision.theme.themeSwitcher
import io.kvision.utils.em
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {
    init {
        ThemeManager.init()
//        require("css/kvapp.css")
    }

    override fun start(state: Map<String, Any>) {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "de" to require("i18n/messages-de.json"),
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json")
                )
            )

        root("kvapp", ContainerType.FLUID) {

            navbar(label = "Unfall24", type = NavbarType.STICKYTOP, containerType = ContainerType.LG, expand = NavbarExpand.LG) {

                navForm(rightAlign = true) {
                    select(listOf("en" to "English", "de" to "Deutsch", "pl" to "Polski"), I18n.language) {
                        onEvent {
                            change = {
                                I18n.language = self.value ?: "en"
                            }
                        }
                    }

                    themeSwitcher(style = ButtonStyle.OUTLINESECONDARY, round = true)
                }
            }

            div(className = "page-content") {
                div(className = "background-overlay") {
                    background = Background(image = "img/stock-photo-two-cars-crashed.jpg", repeat = BgRepeat.NOREPEAT, size = BgSize.COVER, positionY = 50.perc)
                    height = 400.px

                    div(className = "container") {
                        paddingTop = 2.em
                        gridPanel(templateColumns = "auto auto", columnGap = 5, rowGap = 5) {
                            card(title = "Second header") {
                                + "Some content of the second card"
                                ul {
                                    li { content = "1" }

                                    li { content = "2" }

                                    li { content = "3" }
                                }

                            }
                        }
                    }
                }

                div(className = "container") {
                    vPanel {
                        add(ListPanel)
                        add(EditPanel)
                    }
                }
            }
        }

        AppScope.launch {
//            Model.getAddressList()
        }
    }

    override fun dispose(): Map<String, Any> {
        return mapOf()
    }
}

fun main() {
    startApplication(::App,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        BootstrapUploadModule,
        BootstrapIconsModule,
        FontAwesomeModule,
        CoreModule)
}
