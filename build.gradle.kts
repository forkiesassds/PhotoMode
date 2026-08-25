plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    alias(ft.plugins.mixin)
    alias(ft.plugins.relocate)
}

loom {
    mixin {
        useLegacyMixinAp = false
    }

    decompilers {
        named("vineflower") { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}

fletchingTable {
    mixins.configure(sourceSets.main) {
        mixin("picturemode-common.mixins.json", "default") {
            env("CLIENT")
        }
    }

    relocate.configure(sourceSets.main) {
        matching("(**)/(*).json5") {
            into("$1/$2.json")
            with(Json5ToJson)
        }
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

    modCompileOnly("net.fabricmc:fabric-loader:${commonMod.prop("fabric_loader_version")}")
    modCompileOnly("dev.isxander:yet-another-config-lib:${commonMod.prop("yacl_version")}-fabric")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    afterEvaluate {
        val mainSourceSet = sourceSets.main.get()
        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }
    }
}