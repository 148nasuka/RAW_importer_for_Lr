setlocal
adb devices
adb shell pm uninstall com.nasuka.rifl
@echo �֘A�t�@�C�����������܂�
adb shell rm -r /sdcard/Android/data/com.nasuka.rifl
adb shell rm -r /sdcard/Pictures/RAW
@echo RAW_importer_for_Lr�̃A���C���X�g�[�����������܂���
@pause
