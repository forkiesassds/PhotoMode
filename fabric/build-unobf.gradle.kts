plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../../run"
    }

    runs {
        removeIf { it.environment == "server" }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")

    implementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    api("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        implementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        compileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }
}

tasks.processResources {
    exclude("assets/moderner_beta/banner.png")
}