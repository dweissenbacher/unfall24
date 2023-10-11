package de.unfall24.component

import io.kvision.core.Container
import io.kvision.core.ResString
import io.kvision.html.*

fun Container.card(image: ResString? = null, title: String? = null, contentBuilder: P.() -> Unit) {
    div(className = "card") {
        image?.let {
            image(src = image, alt = "Card image", className = "card-img-top")
        }

        div(className = "card-body") {
            title?.let {
                h5(title, className = "card-title")
            }

            p(className = "card-text", init = contentBuilder)
        }
    }
}