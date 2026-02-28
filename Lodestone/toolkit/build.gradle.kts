plugins {
    java
}

dependencies {
    implementation(project(":core"))
}

tasks.jar {
    from(
        project(":core").sourceSets.main.get().output
    )
}