package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object DotenvManager {
    /**
     * Thread-Safe database url
     */
    val DATABASE_URL : String by lazy { dotenv["DATABASE_URL"] }

    /**
     * Thread-Safe dotenv getter
     */
    private val dotenv : Dotenv by lazy {
        dotenv {
            directory = "/assets"
            filename = "env"
        }
    }
}