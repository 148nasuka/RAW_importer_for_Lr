setlocal

adb devices
adb uninstall com.nasuka.rifl
adb shell rm -r /sdcard/Android/data/com.nasuka.rifl
adb shell rm -r /sdcard/Pictures/RAW
@echo �������������܂���

@echo RAW_importer_for_Lr���C���X�g�[�����܂�
adb install RAW_importer_for_Lr.apk
adb shell pm grant com.nasuka.rifl android.permission.READ_EXTERNAL_STORAGE
adb shell pm grant com.nasuka.rifl android.permission.WRITE_EXTERNAL_STORAGE
adb shell am start -n com.nasuka.rifl/com.example.RAW_importer_for_Lr.MainActivity
@echo RAW_importer_for_Lr���N�����܂�

@pause
