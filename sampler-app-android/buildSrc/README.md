# BuildSrc

## How to use it

### PublishRelease

This task generate in the `<module-folder>/build/outputs/publish` a folder with APKs, AABs, Mappings.

```groovy
import com.mercandalli.build_src.publish_release.PublishReleaseTask

task publishRelease(type: PublishReleaseTask) {
    targetModule = "app"
    flavorsToVersionCodeIncrement = [
            "": 0L
    ]
    versionName = appVersionName
    versionCode = appVersionCode
}
```

### Translate

This task is using Lokalise to download translation into feature folder. Strings should be tags with feature name.

```groovy
import com.mercandalli.build_src.translation.TranslateTask

task translate(type: TranslateTask) {
    targetModule = "app"
    projectId = ""
    projectToken = ""
    tagsToLocalFolderPath = [
            "history_row": "src/main/res/history_row",
            "main": "src/main/res/main"
    ]
}
```
