plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

loom {
    accessWidenerPath = stonecutter.process(commonProject.file("../../src/main/resources/photomode.accesswidener"), "build/dev.aw")

    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../../run"
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "photomode-fabric.mixins.json")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.propOrNull("parchment_mappings")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-$parchmentVersion@zip")
        }
    })

    modImplementation("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.prop("fabric_api_version")}")

    if (commonMod.prop("mod_menu_supported").toBoolean()) {
        modImplementation("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    } else {
        modCompileOnly("com.terraformersmc:modmenu:${commonMod.prop("mod_menu_version")}")
    }
}

tasks.remapJar {
    destinationDirectory = rootProject.layout.buildDirectory.dir("libs/$loader")
}