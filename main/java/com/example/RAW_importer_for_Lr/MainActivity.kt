package com.example.RAW_importer_for_Lr

import android.Manifest
import android.app.ProgressDialog.show
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
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
    val pages = listOf(R.drawable.tu1, R.drawable.tu2, R.drawable.tu3, R.drawable.tu4, R.drawable.tu5, R.drawable.tu6, R.drawable.tu7, R.drawable.tu8, R.drawable.tu9)
    val pages_en = listOf(R.drawable.tu1_en, R.drawable.tu2_en, R.drawable.tu3_en, R.drawable.tu4_en, R.drawable.tu5_en, R.drawable.tu6_en, R.drawable.tu7_en, R.drawable.tu8_en, R.drawable.tu9_en)

    fun bind(text: String) {
        textView.text = text
        if (lang == "ja") {
            ImageView.setImageResource(pages[position])
        } else {
            ImageView.setImageResource(pages_en[position])
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

        btn_Lr.setOnClickListener {   //プリセット追加画面起動ボタン
            OpenPresets()
        }
        btn_chose.setOnClickListener {  //画像選択ボタン
            OpenPhotoGallery()
        }
        btn_hint.setOnClickListener {   //ヒントボダン
            About_app()
        }
    }

    private fun OpenPresets(){   //Lightroomのインストールを確認してインテントする
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
                    R.string.Gphoto,
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

    private fun About_app(){    //ポップアップダイアログを表示する
        AlertDialog.Builder(this)
            .setIcon(R.drawable.app_icon)
            .setTitle(R.string.About)
            .setMessage(R.string.About_sent1)
            .setPositiveButton("OK") { dialog, which -> }
            .show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //Googleフォトからの返り値を受け取って処理をする
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            val itemCount = data?.clipData?.itemCount?: 0
            val uriList = mutableListOf<Uri>()
            progressB = 1
            var skip_count = 0

            for (i in 0 until itemCount) {
                val uri = data?.clipData?.getItemAt(i)?.uri
                uri?.let { uriList.add(it) }

                val calendar = Calendar.getInstance()
                val filename = "RW_" + (android.text.format.DateFormat.format("yyyyMMddssss_", calendar)).toString() +i+".dng"
                val rawdir = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).path + "/RAW_importer/"
                val f = File(rawdir, filename)

                val contentResolver: ContentResolver = this.contentResolver
                val columns = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(uri!!, columns, null, null, null)
                cursor!!.moveToFirst()
                val path = cursor!!.getString(0)
                cursor!!.close()

                val original = File(path)
                val copyRAW = File(rawdir + filename)

                //RAW形式の場合のみ変換を行う
                if (path.takeLast(3) == "ORF" ||    //オリンパス
                    path.takeLast(3) == "CR3" ||    //キャノン
                    path.takeLast(3) == "CR2" ||    //キャノン
                    path.takeLast(3) == "CRW" ||    //キャノン
                    path.takeLast(3) == "SRW" ||    //ソニー
                    path.takeLast(3) == "ARW" ||    //ソニー
                    path.takeLast(3) == "NEF" ||    //ニコン
                    path.takeLast(3) == "NRW" ||    //ニコン
                    path.takeLast(3) == "PEF" ||    //ペンタックス
                    path.takeLast(3) == "RAF" ||    //富士フィルム
                    path.takeLast(3) == "X3F" ||    //シグマ
                    path.takeLast(3) == "RW2" ||    //パナソニック
                    path.takeLast(3) == "tif")      //汎用
                    {
                    original.copyTo(copyRAW)
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Images.Media.MIME_TYPE, "image/png") //Androidのメディアストアに登録してギャラリーでも表示可能にする
                            put("_data", f.absolutePath)
                        }
                        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                }else{
                    skip_count++
                }
            }

            progressB = 0

            val locale = Locale.getDefault()
            val lang = locale.language
            var comp_message = "Complete!!\n ($skip_count non-RAW images skipped)"
            if (lang == "ja"){
                comp_message = "完了！！\n ($skip_count 個のRAW画像ではないファイルをスキップしました)"
            }

            val comp_bar = Snackbar.make(findViewById(R.id.linearLayout),comp_message,Snackbar.LENGTH_LONG)
            comp_bar.view.setBackgroundColor(resources.getColor(R.color.sub_point))
            comp_bar.duration = 8000
            comp_bar.show()

        }
    }
}