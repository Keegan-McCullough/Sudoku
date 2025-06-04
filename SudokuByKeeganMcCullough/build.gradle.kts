plugins {
    id("java")
}

group = "edu.wm.cs.cs301.sudoku"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // added manually for autograder
    // https://mvnrepository.com/artifact/com.spertus/jacquard
    implementation("com.spertus:jacquard:1.0.1")

    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20250107")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

task("autograde", JavaExec::class) {
    //println(sourceSets["test"].runtimeClasspath.asPath)
    group = "AutograderTasks"
    mainClass = "edu.wm.cs.cs301.sudoku.AutograderMain"
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.register<Zip>("buildAutograder") {
    group = "AutograderTasks"

    archiveFileName.set("Autograder.zip")
    destinationDirectory.set(layout.projectDirectory)

    from(layout.projectDirectory)
    include("gradle/**")
    include("lib/**")
    include("src/main/java/.gitkeep")
    include("src/test/**")
    include(".gitignore")
    include("build.gradle.kts")
    include("gradlew")
    include("gradlew.bat")
    include("run_autograder")
    include("settings.gradle.kts")
    include("setup.sh")
}
