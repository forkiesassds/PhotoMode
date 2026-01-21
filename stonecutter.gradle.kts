plugins {
    id("dev.kikugie.stonecutter")

    (kotlin("jvm") version "2.3.0").apply(false)
    (id("com.google.devtools.ksp") version "2.3.3").apply(false)
    (id("dev.kikugie.fletching-table") version "0.1.0-alpha.22").apply(false)
    id("me.modmuss50.mod-publish-plugin")
}

plugins.apply("dev.kikugie.stonecutter")
stonecutter.active("1.21.11")

publishMods {
    @Suppress("LocalVariableName")
    val mod_version: String by extra
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE

    github {
        accessToken = providers.environmentVariable("_GITHUB_TOKEN")
        displayName = mod_version
        version = mod_version
        repository = "forkiesassds/PictureMode"
        tagName = providers.environmentVariable("FORGEJO_REF_NAME")
        commitish = ""

        allowEmptyFiles = true
    }

    forgejo {
        accessToken = providers.environmentVariable("FORGEJO_TOKEN")
        host(uri("https://codeberg.org"))
        displayName = mod_version
        version = mod_version
        repository = "icanttellyou/PictureMode"
        tagName = providers.environmentVariable("FORGEJO_REF_NAME")
        commitish = ""

        allowEmptyFiles = true
    }
}

stonecutter.tasks {
    order("build")
    order("publishModrinth", filter = { this.branch.id == "fabric" || this.branch.id == "forgelike" })
}

stonecutter.parameters {
    replacements {
        string {
            direction = eval(current.version, "<1.21.2")
            replace("getDeltaTracker()", "getTimer()")
        }

        string {
            direction = eval(current.version, "<1.21.11")
            replace("Identifier", "ResourceLocation")
        }
    }
}