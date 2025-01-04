import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object PreferencesKey {
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_ID = stringPreferencesKey("user_id")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val USER_IMAGE = stringPreferencesKey("user_image")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val USER_EMAIL = stringPreferencesKey("email")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
