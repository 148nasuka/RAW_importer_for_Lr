package com.example.RAW_importer_for_Lr

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val locale = Locale.getDefault()
        val lang = locale.language

        val titleView = TextView(this)
        titleView.text = ""

        val iv = ImageView(this)
        if (lang == "ja") {
            iv.setImageResource(R.drawable.pop1)
        } else {
            iv.setImageResource(R.drawable.pop1_en)
        }
        iv.setBackgroundColor(resources.getColor(R.color.point));
        iv.setPadding(5, 5, 5, 5)
        iv.adjustViewBounds = true

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
        val btn_pastel = findViewById<Button>(R.id.I_pastel) as ImageButton
        val btn_unreal = findViewById<Button>(R.id.I_unreal) as ImageButton

        btn_retoro.setOnClickListener(){    //レトロプリセットインストールボタン
            if (check_verrsion() == 0){
                pack_comp("Retro")
            }
        }
        btn_pastel.setOnClickListener(){    //パステルプリセットインストールボタン
            if (check_verrsion() == 0) {
                pack_comp("Pastel")
            }
        }
        btn_unreal.setOnClickListener(){    //アンリアルプリセットインストールボタン
            if (check_verrsion() == 0) {
                pack_comp("Unreal")
            }
        }
        btn_Lr.setOnClickListener {   //lightroom起動ボタン
            OpenLr()
        }
        btn_hint.setOnClickListener {   //ヒントボダン
            About_app()
        }
    }

    fun check_verrsion(): Int {     //Androidバージョン確認
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 ||     //検証済みAndroidバージョンのみでインストールを行う（Android11からの/Android/data/下がRead onlyになった対策）
            Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.M||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.N||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.O||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.P||
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            return 0
        }else{
            AlertDialog.Builder(this)
                .setTitle(R.string.Notice)
                .setMessage(R.string.Sorry)
                .setPositiveButton("OK",null)
                .show()
            return 1
        }
    }

    fun Move_Store(){   //Lightroomがインストールされていない場合
        Toast.makeText(
            applicationContext,
            R.string.not_install,
            Toast.LENGTH_LONG
        ).show()

        val url = "https://play.google.com/store/apps/details?id=com.adobe.lrmobile"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    fun pack_comp(pack_name: String){   //プリセットインストール確認処理
        var Install = "install"
        val locale = Locale.getDefault()
        val lang = locale.language
        if (lang == "ja"){
            Install = "以下のパックをインストールしますか？\n"
        } else {
            Install = "Install the following packs?\n"
        }

        AlertDialog.Builder(this)
                .setTitle(R.string.Confirm)
                .setMessage("$Install $pack_name")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    Install_pack(pack_name)

                    Toast.makeText(
                            applicationContext,
                            R.string.complete,
                            Toast.LENGTH_LONG
                    ).show()   //通知

                    val locale = Locale.getDefault()
                    val lang = locale.language

                    val titleView = TextView(this)
                    titleView.text = ""

                    val iv = ImageView(this)
                    if (lang == "ja") {
                        iv.setImageResource(R.drawable.pop2)
                    } else {
                        iv.setImageResource(R.drawable.pop2_en)
                    }
                    iv.setBackgroundColor(resources.getColor(R.color.point));
                    iv.setPadding(5, 5, 5, 5)
                    iv.adjustViewBounds = true

                    AlertDialog.Builder(this)
                            .setView(iv)
                            .show()
                })
                .setNegativeButton("Cancel", null)
                .show()

    }

    fun Install_pack(pack_name: String){    //Lightroomのユーザーフォルダを検索

        val external_m1 = File(Environment.getExternalStorageDirectory(), "/Android/data/com.adobe.lrmobile/files/carouselDocuments/").path//プリセット設置
        val list = File(external_m1).list()

        val packageManager = packageManager
        val intentable = packageManager.getLaunchIntentForPackage("com.adobe.lrmobile"!!)
        if (intentable == null ) {  //Lightroomがインストールされていないとき
            Move_Store()
        } else {    //Lightroomがインストールされているとき
            if (list[0] == "Originals"){    //Lightroom内のユーザーデータ保管先の捜索
                put_files(1, pack_name)
            }else{
                put_files(0, pack_name)
            }
        }
    }

    fun put_files(put_mode: Int, pack_name: String){    //Lightroomのユーザーフォルダに設定ファイルを設置
        try {
            val external_m1 = File(Environment.getExternalStorageDirectory(), "/Android/data/com.adobe.lrmobile/files/carouselDocuments/").path//プリセット設置
            val list = File(external_m1).list()
            val assetMgr = resources.assets
            val files = assetMgr.list(pack_name)

            var j = 0
            if (files != null) {
                for (i in files) {
                    val LrUser = File(Environment.getExternalStorageDirectory(), "/Android/data/com.adobe.lrmobile/files/carouselDocuments/").path + "/" + list[put_mode] + "/Profiles/Settings/UserStyles/"+files?.get(j)
                    try {
                        val inputStream: InputStream = assets.open(files?.get(j))
                        val fileOutputStream: FileOutputStream = FileOutputStream(File(LrUser), false)
                        val buffer = ByteArray(1024)
                        var length = 0
                        while (inputStream.read(buffer).also { length = it } >= 0) {
                            fileOutputStream.write(buffer, 0, length)
                        }
                        fileOutputStream.close()
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    j += 1
                }
            }
        } catch (e: IOException) {}
    }

    fun OpenLr(){   //Lightroomのインストールを確認してインテントする
        val intent = Intent()
        val packageManager = packageManager
        val intentable = packageManager.getLaunchIntentForPackage("com.adobe.lrmobile"!!)
        if (intentable == null ) {  //Lightroomがインストールされていないとき
            Move_Store()
        } else {    //Lightroomがインストールされているとき
            intent.setClassName("com.adobe.lrmobile", "com.adobe.lrmobile.StorageCheckActivity")
            startActivity(intent)
        }
    }

    fun About_app(){    //ポップアップダイアログを表示する
        AlertDialog.Builder(this)
            .setIcon(R.drawable.app_icon)
            .setTitle(R.string.About)
            .setMessage(R.string.About_sent1)
            .setPositiveButton("OK") { dialog, which -> }
            .show()

    }

}


