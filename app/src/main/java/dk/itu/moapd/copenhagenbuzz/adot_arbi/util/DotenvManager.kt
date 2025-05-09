package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

/**
 * Utility object responsible for loading and exposing environment variables
 * from a `env` file located in the `/assets` directory.
 *
 * This class ensures thread-safe and lazy access to Firebase-related configuration values.
 */
object DotenvManager {

    /**
     * Thread-safe access to the Realtime Database URL defined in the `.env` file.
     */
    val RT_DATABASE_URL: String by lazy { dotenv["RT_DATABASE_URL"] }

    /**
     * Thread-safe access to the Firebase Storage URL defined in the `.env` file.
     */
    val STORAGE_DATABASE_URL: String by lazy { dotenv["STORAGE_DATABASE_URL"] }

    /**
     * Lazily initializes the Dotenv instance to load variables from `assets/env`.
     */
    private val dotenv: Dotenv by lazy {
        dotenv {
            directory = "/assets"
            filename = "env"
        }
    }
}
