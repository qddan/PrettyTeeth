Pod::Spec.new do |spec|
    spec.name                     = 'ComposeApp'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://github.com/your-username/PrettyTeeth'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'PrettyTeeth Compose Multiplatform Framework'
    spec.vendored_frameworks      = 'build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'
                
                
    if !Dir.exist?('build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework') || Dir.empty?('build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework')
        spec.script_phase = {
            :name => 'Build ComposeApp',
            :script => 'cd "$PODS_TARGET_SRCROOT" && ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64',
            :execution_position => :before_compile
        }
    end
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':composeApp',
        'PRODUCT_MODULE_NAME' => 'ComposeApp',
    }
                
    spec.script_phases = [
        {
            :name => 'Build Kotlin',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
end
