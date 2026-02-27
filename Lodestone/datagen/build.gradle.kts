plugins {
    java
}

dependencies {
    implementation(project(":core"))
}

tasks.jar {
    archiveClassifier.set("datagen")

    from(
        project(":core").sourceSets.main.get().output
    )
}