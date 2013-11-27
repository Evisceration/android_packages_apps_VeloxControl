echo "===== Cleaning ====="
./gradlew clean
echo "===== Building ====="
./gradlew assembleRelease
echo "=====   Move   ====="
cp -f build/apk/VeloxControl-release.apk /home/alex/android/vendor/velox/proprietary/common/app/VeloxControl.apk
echo "=====   DONE   ====="
