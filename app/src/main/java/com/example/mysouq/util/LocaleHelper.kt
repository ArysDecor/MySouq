package com.example.mysouq.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun wrap(context: Context, languageCode: String): ContextWrapper {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val configuration = resources.configuration
        
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        
        return ContextWrapper(context.createConfigurationContext(configuration))
    }
}
