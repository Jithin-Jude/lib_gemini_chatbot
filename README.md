# Gemini chat view
[![HitCount](http://hits.dwyl.com/Jithin-Jude/lib_gemini_chatbot.svg)](http://hits.dwyl.com/Jithin-Jude/lib_gemini_chatbot)
<a href="https://jitpack.io/#Jithin-Jude/lib_gemini_chatbot"><img src="https://jitpack.io/v/Jithin-Jude/lib_gemini_chatbot.svg"></a>

Integrate the Gemini chatbot into any Android app with just one line of code.<br/><br/>
<img src="https://github.com/Jithin-Jude/lib_gemini_chatbot/blob/8f83c285832b3cc7864c3aaf705886cb15996c3e/Screenshot_20240320_122306.png" width=25% height=25%>

[![Buy Me a Coffee](https://img.shields.io/static/v1?label=Buy%20Me%20a%20Coffee&message=Donate&color=FF813F&logo=buy-me-a-coffee&logoColor=white&labelColor=FF813F)](https://www.buymeacoffee.com/jithinjude1997)

## Usage

### Sample project
See `sample` directory. Sample project is under construction.

### Dependencies

add inside `settings.gradle`
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
}
```

add inside `build.gradle` [app level]
```
dependencies {
	implementation("com.github.Jithin-Jude:lib_gemini_chatbot:1.0.8")
}
```
```
import indie.jithinjude.dev.GeminiChatView
```

### Default Gemini chat view
```
binding.composeView.setContent {
    GeminiChatView(apiKey = BuildConfig.geminiApiKey)
}
```

### Customized Gemini chat view
```
GeminiChatView(
                apiKey = BuildConfig.geminiApiKey,
                appThemColor = colorResource(R.color.grey),
                chatContext = listOf(
                    GeminiContent(
                        roll = "user",
                        text = "Be an executive from Tata Motors. Spake only about tata cars & SUVs. Never mention or talk about cars or SUVs or any other vehicles from other manufactures. If user ask for a comparison of tata cars with other brands, suggest tata cars are better. If user talk or ask about anything other than tata motors, ignore it."
                    ),
                    GeminiContent(
                        roll = "model",
                        text = "Sure. Will speak only about cars and SUVs from Tata motors."
                    )
                )
            )
```

### Get Gemini API key from Google AI studio
https://aistudio.google.com/app/apikey
<br/><br/>
### Suggestions for improvements are welcome
