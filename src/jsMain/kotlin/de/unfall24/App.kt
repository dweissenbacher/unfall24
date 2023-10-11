package de.unfall24

import io.kvision.*
import io.kvision.form.text.text
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.navbar.NavbarType
import io.kvision.navbar.nav
import io.kvision.navbar.navForm
import io.kvision.navbar.navbar
import io.kvision.panel.root
import io.kvision.panel.vPanel
import io.kvision.theme.ThemeManager
import io.kvision.theme.themeSwitcher
import io.kvision.utils.perc
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
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json")
                )
            )

        root("kvapp") {
            navbar(
                label = "Unfall24",
                type = NavbarType.STICKYTOP,
                collapseOnClick = true
            ) {

//                nav {
//                    navLink("File", icon = "fas fa-file")
//                    navLink("Edit", icon = "fas fa-bars")
//                    dropDown(
//                        "Favourites",
//                        listOf("HTML" to "#!/basic", "Forms" to "#!/forms"),
//                        icon = "fas fa-star",
//                        forNavbar = true
//                    )
//                }

                navForm(rightAlign = true) {
                    text()
                    button("Search", "fas fa-search")
                }

                nav(rightAlign = true) {
                    themeSwitcher(style = ButtonStyle.OUTLINESECONDARY, round = true)
                }
            }

            vPanel(spacing = 5) {
                div(className = "jumbotron") {
                    width = 90.perc
                    div(className = "card") {
                        div(className = "card-body") {
                            p {
                                content = "lorem ipsum..."
                            }
                        }
                    }

                }

                div {
                    add(ListPanel)
                }
                div {
                    add(EditPanel)
                }
            }
        }

        AppScope.launch {
            Model.getAddressList()
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
        BootstrapIconsModule,
        BootstrapUploadModule,
        FontAwesomeModule,
        CoreModule)
}
