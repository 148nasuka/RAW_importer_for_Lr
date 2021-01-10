package com.example.RAW_importer_for_Lr

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.util.*


class PagerRecyclerAdapter(private val items: List<String>) : RecyclerView.Adapter<PagerViewHolder>() { //ViewPagerのソケットを設定

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
            PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.slidepage, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //コンテンツの切り替え
    private val textView: TextView = itemView.findViewById(R.id.page_name)
    private val ImageView : ImageView =itemView.findViewById(R.id.slideimg)
    val locale = Locale.getDefault()
    val lang = locale.language

    fun bind(text: String) {
        textView.text = text
        if (text == "Page 1"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu1)
            } else {
                ImageView.setImageResource(R.drawable.tu1_en)
            }
        }
        if (text == "Page 2"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu2)
            } else {
                ImageView.setImageResource(R.drawable.tu2_en)
            }
        }
        if (text == "Page 3"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu3)
            } else {
                ImageView.setImageResource(R.drawable.tu3_en)
            }
        }
        if (text == "Page 4"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu4)
            } else {
                ImageView.setImageResource(R.drawable.tu4_en)
            }
        }
        if (text == "Page 5"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu5)
            } else {
                ImageView.setImageResource(R.drawable.tu5_en)
            }
        }
        if (text == "Page 6"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu6)
            } else {
                ImageView.setImageResource(R.drawable.tu6_en)
            }
        }
        if (text == "Page 7"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu7)
            } else {
                ImageView.setImageResource(R.drawable.tu7_en)
            }
        }
        if (text == "Page 8"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu8)
            } else {
                ImageView.setImageResource(R.drawable.tu8_en)
            }
        }
        if (text == "Page 9"){
            if (lang == "ja") {
                ImageView.setImageResource(R.drawable.tu9)
            } else {
                ImageView.setImageResource(R.drawable.tu9_en)
            }
        }
    }
}


class MainActivity : AppCompatActivity() {
    var progressB: Int = 0
    private lateinit var viewPager: ViewPager2
    private val items: MutableList<String> = mutableListOf( //ViewPagerのページ名指定
            "Page 1",
            "Page 2",
            "Page 3",
            "Page 4",
            "Page 5",
            "Page 6",
            "Page 7",
            "Page 8",
            "Page 9"
    )

    override fun onCreate(savedInstanceState: Bundle?) {    //アクティビティ起動時
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        viewPager = findViewById(R.id.pager)
        val pagerAdapter = PagerRecyclerAdapter(items)
        viewPager.adapter = pagerAdapter

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {//パーミッション確認
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
            )
        }


        val rawdir = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).path + "/RAW_importer/"
        val raw = File(rawdir)
        raw.mkdirs()        //専用ディレクトリを共有ストレージに作成
        Toast.makeText(
                applicationContext,
                "Save location：" + rawdir,
                Toast.LENGTH_LONG
        ).show()   //通知

    }

    override fun onBackPressed() {  //戻るボタンが押された時の動作
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onRequestPermissionsResult(    //パーミッションの動作
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ){}

    companion object {
        private const val CHOOSE_PHOTO_REQUEST_CODE: Int = 43
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onResume() {   //ボタン押したときの動作
        super.onResume()
        val btn_chose = findViewById<Button>(R.id.btn_chose) as Button
        val btn_Lr = findViewById<Button>(R.id.btn_Lr) as Button
        val btn_hint = findViewById<Button>(R.id.hint_button) as Button

        btn_Lr.setOnClickListener {   //lightroom起動ボタン
            OpenPresets()
        }
        btn_chose.setOnClickListener {  //画像選択ボタン
            OpenPhotoGallery()
        }
        btn_hint.setOnClickListener {   //ヒントボダン
            About_app()
        }
    }

    fun OpenPresets(){   //Lightroomのインストールを確認してインテントする
        val intent = Intent(this@MainActivity, Progressbar::class.java)
        startActivity(intent)
    }

    private fun OpenPhotoGallery() {    //Googleフォトのインストールを確認してインテントする
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        val packageManager = packageManager
        val intentable = packageManager.getLaunchIntentForPackage("com.google.android.apps.photos"!!)
        intent.type = "image/*"
        if (intentable == null){    //Googleフォトがインストールされていないとき
            Toast.makeText(
                    applicationContext,
                    "Googleフォトがインストールされていません！！",
                    Toast.LENGTH_LONG
            ).show()
            val url = "https://play.google.com/store/apps/details?id=com.google.android.apps.photos"
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent)
            }

        }else{  //Googleフォトがインストールされているとき
            intent.setClassName(
                    "com.google.android.apps.photos",
                    "com.google.android.apps.photos.picker.external.ExternalPickerActivity"
            )
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                    Intent.createChooser(intent, "Choose Photo"),
                    CHOOSE_PHOTO_REQUEST_CODE
            )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //Googleフォトからの返り値を受け取って処理をする
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            val itemCount = data?.clipData?.itemCount?: 0
            val uriList = mutableListOf<Uri>()
            progressB = 1

            for (i in 0..itemCount - 1) {
                val uri = data?.clipData?.getItemAt(i)?.uri
                uri?.let { uriList.add(it) }

                val calendar = Calendar.getInstance()
                val filename = "RW_" + (android.text.format.DateFormat.format("yyyyMMddssss_", calendar)).toString() +i+".dng"
                val rawdir = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).path + "/RAW_importer/"
                val f = File(rawdir, filename)
                val contentResolver: ContentResolver = this.getContentResolver()
                val columns = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(uri!!, columns, null, null, null)
                cursor!!.moveToFirst()
                val path = cursor!!.getString(0)
                cursor!!.close()
                val original = File(path)
                val copyRAW = File(rawdir + filename)
                original.copyTo(copyRAW)

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png") //Androidのメディアストアに登録してギャラリーでも表示可能にする
                    put("_data", f.absolutePath)
                }
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            }

            progressB = 0
            Toast.makeText(
                    applicationContext,
                    "Complete!!",
                    Toast.LENGTH_LONG
            ).show()
        }
    }
}