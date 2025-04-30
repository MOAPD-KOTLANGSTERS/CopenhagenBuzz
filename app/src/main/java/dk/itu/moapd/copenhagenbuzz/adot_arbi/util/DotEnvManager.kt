package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object DotEnvManager {

    // this is quite cursed
    var dotenv: Dotenv? = null
        get() = if(field == null) {init(); field } else { field }

    private fun init() {
        dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }
    }

}