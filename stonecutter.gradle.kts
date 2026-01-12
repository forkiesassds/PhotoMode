plugins {
    id("dev.kikugie.stonecutter")

    (kotlin("jvm") version "2.3.0").apply(false)
    (id("com.google.devtools.ksp") version "2.3.3").apply(false)
    (id("dev.kikugie.fletching-table") version "0.1.0-alpha.22").apply(false)
}

plugins.apply("dev.kikugie.stonecutter")
stonecutter.active("1.21.11")

stonecutter.tasks {
    order("build")
}