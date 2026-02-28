plugins {
    java
}

dependencies {
    implementation(project(":core"))
    implementation(project(":datagen"))
    implementation(project(":renderer"))
    implementation(project(":toolkit"))
    implementation(project(":curios"))
}

tasks.jar {
    from(
        project(":core").sourceSets.main.get().output,
        project(":datagen").sourceSets.main.get().output,
        project(":renderer").sourceSets.main.get().output,
        project(":toolkit").sourceSets.main.get().output,
        project(":curios").sourceSets.main.get().output
    )
}