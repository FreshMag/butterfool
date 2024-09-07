plugins {
    kotlin("jvm")
    antlr
    alias(libs.plugins.ktlint)
}

group = "io.github.freshmag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            // telling that output generateGrammarSource should be part of main source set
            // actually passed value will be equal to `outputDirectory` that we configured above
            srcDir(tasks.generateGrammarSource)
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest)
    implementation(libs.asm)
    antlr(libs.antlr) // use ANTLR version 4
}

ktlint {
    filter {
        exclude { it.file.path.contains(layout.buildDirectory.dir("generated").get().toString()) }
    }
}


tasks.generateGrammarSource {
    // set output directory to some arbitrary location in `/build` directory.
    // by convention `/build/generated/sources/main/java/<generator name>` is often used
    outputDirectory = file("${project.projectDir}/generated/sources/main/java/antlr")

    // pass -package to make generator put code in not default space
    arguments = listOf("-package", "io.github.freshmag.butterfool.compiler.grammar")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}