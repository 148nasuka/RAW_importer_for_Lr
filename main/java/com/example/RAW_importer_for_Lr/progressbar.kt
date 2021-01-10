package com.example.RAW_importer_for_Lr

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


class Progressbar : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {    //アクティビティ起動時
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progressbar)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val locale = Locale.getDefault()
        val lang = locale.language

        val titleView = TextView(this)
        titleView.setText("")

        val iv = ImageView(this)
        if (lang == "ja") {
            iv.setImageResource(R.drawable.pop1)
        } else {
            iv.setImageResource(R.drawable.pop1_en)
        }
        iv.setBackgroundColor(getResources().getColor(R.color.point));
        iv.setPadding(5, 5, 5, 5)
        iv.setAdjustViewBounds(true)

        AlertDialog.Builder(this)
                .setView(iv)
                .show()

    }

    override fun onBackPressed() {  //戻るボタンが押された時の動作
        finish()
    }

    override fun onResume() {   //ボタン押したときの動作
        super.onResume()
        val btn_Lr = findViewById<Button>(R.id.btn_Lr) as Button
        val btn_hint = findViewById<Button>(R.id.hint_button) as Button
        val btn_retoro = findViewById<Button>(R.id.I_retoro) as ImageButton

        btn_retoro.setOnClickListener(){//レトロプリセットインストールボタン
            I_retoro()
        }

        btn_Lr.setOnClickListener {   //lightroom起動ボタン
            OpenLr()
        }
        btn_hint.setOnClickListener {   //ヒントボダン
            About_app()
        }
    }

    fun I_retoro(){
        AlertDialog.Builder(this)
                .setTitle(R.string.Confirm)
                .setMessage(R.string.I_retoro)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    retoro()
                    Toast.makeText(
                            applicationContext,
                            R.string.complete,
                            Toast.LENGTH_LONG
                    ).show()   //通知
                    val locale = Locale.getDefault()
                    val lang = locale.language

                    val titleView = TextView(this)
                    titleView.setText("")

                    val iv = ImageView(this)
                    if (lang == "ja") {
                        iv.setImageResource(R.drawable.pop2)
                    } else {
                        iv.setImageResource(R.drawable.pop2_en)
                    }
                    iv.setBackgroundColor(getResources().getColor(R.color.point));
                    iv.setPadding(5, 5, 5, 5)
                    iv.setAdjustViewBounds(true)

                    AlertDialog.Builder(this)
                            .setView(iv)
                            .show()
                })
                .setNegativeButton("Cancel", null)
                .show()

    }

    fun retoro(){

        val external_m1 = File(Environment.getExternalStorageDirectory(), "/Android/data/com.adobe.lrmobile/files/carouselDocuments/").path//プリセット設置
        val list = File(external_m1).list()
        val assetMgr = resources.assets

        val packageManager = packageManager
        val intentable = packageManager.getLaunchIntentForPackage("com.adobe.lrmobile"!!)
        if (intentable == null ) {  //Lightroomがインストールされていないとき
            Toast.makeText(
                    applicationContext,
                    "Lightroom is not installed",
                    Toast.LENGTH_LONG
            ).show()

            val url = "https://play.google.com/store/apps/details?id=com.adobe.lrmobile"
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent)
            }

        } else {    //Lightroomがインストールされているとき
            if (list[0] == "Originals"){
                try {
                    val files = assetMgr.list("retoro")
                    var j = 0
                    if (files != null) {
                        for (i in files) {
                            val LrUser = File(Environment.getExternalStorageDirectory(), "/Android/data/com.adobe.lrmobile/files/carouselDocuments/").path + "/" + list[1] + "/Profiles/Settings/UserStyles/"+files?.get(j)
                            try {
                                val inputStream: InputStream = assets.open(files?.get(j))
                                val fileOutputStream: FileOutputStream = FileOutputStream(File(LrUser), false)
                                val buffer = ByteArray(1024)
                                var length = 0
                                while (inputStream.read(buffer).also({ length = it }) >= 0) {
                                    fileOutputStream.write(buffer, 0, length)
                                }
                                fileOutputStream.close()
                                inputStream.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            j = j+1
                        }
                    }
                } catch (e: IOException) {}
            }else{
                try {
                    val files = assetMgr.list("retoro")
                    var j = 0
                    if (files != null) {
                        for (i in files) {
                            val LrUser = File(Environment.getExternalStorageDirectory(), "/Android/data/com.adobe.lrmobile/files/carouselDocuments/").path + "/" + list[0] + "/Profiles/Settings/UserStyles/"+files?.get(j)
                            try {
                                val inputStream: InputStream = assets.open(files?.get(j))
                                val fileOutputStream: FileOutputStream = FileOutputStream(File(LrUser), false)
                                val buffer = ByteArray(1024)
                                var length = 0
                                while (inputStream.read(buffer).also({ length = it }) >= 0) {
                                    fileOutputStream.write(buffer, 0, length)
                                }
                                fileOutputStream.close()
                                inputStream.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            j = j+1
                        }
                    }
                } catch (e: IOException) {}
            }
        }

    }

    fun OpenLr(){   //Lightroomのインストールを確認してインテントする
        val intent = Intent()
        val packageManager = packageManager
        val intentable = packageManager.getLaunchIntentForPackage("com.adobe.lrmobile"!!)
        if (intentable == null ) {  //Lightroomがインストールされていないとき
            Toast.makeText(
                    applicationContext,
                    "Lightroom is not installed",
                    Toast.LENGTH_LONG
            ).show()

            val url = "https://play.google.com/store/apps/details?id=com.adobe.lrmobile"
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent)
            }

        } else {    //Lightroomがインストールされているとき
            intent.setClassName("com.adobe.lrmobile", "com.adobe.lrmobile.StorageCheckActivity")
            startActivity(intent)
        }
    }

    fun About_app(){    //ポップアップダイアログを表示する
        val locale = Locale.getDefault()
        val lang = locale.language
        if (lang == "ja") {
            // 日本語環境
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                    .setIcon(R.drawable.app_icon)
                    .setTitle("このアプリについて")
                    .setMessage("本アプリは一眼レフからのRAWファイルをモバイルアプリ版のLightroomで編集可能なファイルに変換するアプリです。\n\n\n" +
                            "データの扱いについて\n" +
                            "本アプリは端末内のファイルを読み取りますが、端末内に保蔵されているメディアファイル（画像）のみを読み取ります。" +
                            "読み取ったファイルは名前の変更のみを行うため、ファイルの内容には変化を加えません。" +
                            "また、このアプリはLightroomの管理するファイルには一切変更を加えず、Lightroomに読み込む前のファイルに手を加えるものとなります。\n\n\n" +
                            "プライバシーについて\n" +
                            "本アプリは完全オフラインで処理を行うのでユーザの個人データを外部に送信することは一切ありませんが、端末名などの機器データはサービスの改善を目的にGoogleに送信されます。\n" +
                            "\n\n" +
                            "意見、フィードバックがございましたらPlayStore&メールアドレスまでご連絡ください。\n" +
                            "Mail：148nasuka@gmail.com")
                    .setPositiveButton("OK", { dialog, which -> })
                    .show()
        } else {
            // その他の言語
            AlertDialog.Builder(this)
                    .setIcon(R.drawable.app_icon)
                    .setTitle("About this app")
                    .setMessage("This app is an app that converts RAW files from a single-lens reflex camera into files that can be edited with the mobile app version of Lightroom.\n\n\n" +
                            "About data handling\n" +
                            "This application reads the files in your device, but only the media files (images) stored in your device." +
                            "The read file only changes its name, so it does not change the contents of the file." +
                            "In addition, this app does not make any changes to the files managed by Lightroom, and modifies the files before they are loaded into Lightroom.\n\n\n" +
                            "About Privacy\n" +
                            "Since this application processes completely offline, it does not send the user's personal data to the outside at all, but device data such as the device name is sent to Google for the purpose of improving the service.\n" +
                            "\n\n" +
                            "If you have any comments or feedback, please contact Play Store & Email Address.\n" +
                            "Mail：148nasuka@gmail.com")
                    .setPositiveButton("OK", { dialog, which -> })
                    .show()
        }

    }

}


