import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.compose") version "0.3.2"
}

group = "me.user"
version = "1.0.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.8.0")
    implementation("com.jcraft:jsch:0.1.55")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "15"
}

compose.desktop {
    application {
        javaHome = System.getenv("JDK_15")

        mainClass = "com.kotlang.MainKt"
        nativeDistributions {
            modules("java.net.http")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "OmniShell"

            macOS {
                // macOS DSL settings
                bundleID = "com.kotlang.Omnishell"
                signing {
                    sign.set(true)
                    identity.set("SaiNageswar Satchidanand")
                    // keychain.set("/path/to/keychain")
                }
                notarization {
                    appleID.set("sainageswar@alumni.iitm.ac.in")
                    password.set("passwordPlaceholder")
                }

                iconFile.set(project.file("src/main/resources/osIcon.icns"))
            }

            linux {
                iconFile.set(project.file("src/main/resources/osIcon.png"))
            }
        }
    }
}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions.useIR = true
//compileKotlin.kotlinOptions {
//    jvmTarget = "15"
//}
