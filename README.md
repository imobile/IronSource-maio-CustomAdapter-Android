# IronSourceMaioCustomAdapter

- MaioSDK >= 2.0.1 https://github.com/imobile/MaioSDK-v2-Android
- IronSourceSDK >= 8.4.0

## Installation

バージョンカタログを使用しない場合/For kotlin projects not using version catalogs:

- プロジェクトの`build.gradle`に、以下のリポジトリを追加します。
- Add these repositories to your Project build.gradle:

<pre><code>allprojects {
    repositories {
        maven{ url "https://imobile-maio.github.io/maven" }
        maven { url "https://android-sdk.is.com" }
        google()
        jcenter()
    }
}
</pre></code>

- 下記を`build.gradle`に追加します：
- Add the following implementations inside your Module build.grade:

### jar 

<pre><code>dependencies {
    implementation 'com.maio:android-ironsource-customadapter:1.0.0'
    implementation 'com.ironsource.sdk:mediationsdk:8.4.0'
}
</pre></code>

### aar

<pre><code>dependencies {
    implementation 'com.maio:android-ironsource-customadapter:1.0.0@aar'
    implementation 'com.ironsource.sdk:mediationsdk:8.4.0'
}
</pre></code>

 バージョンカタログを使用する場合/For kotlin projects using version catalogs:

- プロジェクトの`settings.gradle.kts`に、以下のリポジトリを追加します。
- Add these repositories to your Project settings.gradle.kts:

<pre><code>dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven{
            url = uri("https://android-sdk.is.com")
            url = uri("https://imobile-maio.github.io/maven")
        }
    }
}
</pre></code>


- プロジェクトの`libs.versions.toml`に、以下のリポジトリを追加します。
- Add these repository to your Project libs.versions.toml:

<pre><code>[versions]
maioIS = "1.0.0"
mediationsdk = "8.4.0"

[libraries]
maio_ironsource = { group = "com.maio", name = "android-ironsource-customadapter", version.ref = "maioIS" }
mediationsdk = { module = "com.ironsource.sdk:mediationsdk", version.ref = "mediationsdk" }
</pre></code>


- 下記を`build.gradle`に追加します：
- Add the following implementations inside your Module build.grade:

### jar 

<pre><code>dependencies {
    implementation(libs.maio.ironsource)
    implementation(libs.mediationsdk)
}
</pre></code>

### aar

<pre><code>dependencies {      
    implementation(libs.mediationsdk) 
    implementation(libs.maio.ironsource){
    artifact {
        type = "aar"
    }
}
}
</pre></code>


## Author

(c) 2024 i-mobile.


## License

IronSourceMaioCustomAdapter is available under the Apache-2.0 license. See the LICENSE file for more info.
