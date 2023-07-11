rootProject.name = "Lodestone"
pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		maven("https://maven.minecraftforge.net/")
		maven("https://maven.parchmentmc.org")
		maven("https://repo.spongepowered.org/repository/maven-public/")
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "org.spongepowered.mixin") {
				useModule("org.spongepowered:mixingradle:0.7-SNAPSHOT") // TODO is this needed?
			}
		}
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven {
			name = 'MinecraftForge'
			url = 'https://maven.minecraftforge.net/'
		}
		maven {
			name = "parchmentmc"
			url = "https://maven.parchmentmc.org"
		}
		maven {
			name = "spongepowered"
			url = "https://repo.spongepowered.org/repository/maven-public/"
		}
	}
}

plugins {
	id 'org.gradle.toolchains.foojay-resolver-convention' version '0.5.0'
}