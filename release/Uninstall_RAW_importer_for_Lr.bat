setlocal
adb devices
adb shell pm uninstall com.nasuka.rifl
@echo 関連ファイルを消去します
adb shell rm -r /sdcard/Android/data/com.nasuka.rifl
adb shell rm -r /sdcard/Pictures/RAW
@echo RAW_importer_for_Lrのアンインストールが完了しました
@pause
