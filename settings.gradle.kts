rootProject.name = "Lodestone"

rootProject.name = "lodestone"

listOf(
    "core",
    "datagen",
    "renderer",
    "toolkit",
    "curios",
    "all"
).forEach {
    include(it)
    project(":$it").projectDir = file("Lodestone/$it")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.parchmentmc.org")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        maven("https://maven.neoforged.net/releases")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.spongepowered.mixin") {
                useModule("org.spongepowered:mixingradle:0.7-SNAPSHOT")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}