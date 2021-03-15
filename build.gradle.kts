import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.compose") version "0.3.2"
    id("com.google.protobuf") version "0.8.15"
}

group = "me.user"
version = "1.0.0"

sourceSets {
    main {
        java {
            srcDir("src/main/generated/main/java")
            srcDir("src/main/generated/main/grpc")
        }
        proto {
            srcDir("src/main/OmnishellProcessManagerModel")
        }
    }
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("src/main/generated/main/grpckt")
}

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
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    //grpc
    implementation("io.grpc:grpc-netty-shaded:1.36.0")
    implementation("io.grpc:grpc-protobuf:1.36.0")
    implementation("io.grpc:grpc-stub:1.36.0")

    //grpc kotlin stub
    implementation("io.grpc:grpc-kotlin-stub:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

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

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:4.0.0-rc-2"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.36.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
        }
    }

    generatedFilesBaseDir = "$projectDir/src/main/generated"
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
