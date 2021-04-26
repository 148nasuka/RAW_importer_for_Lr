package com.example.RAW_importer_for_Lr

import android.Manifest
import android.app.ProgressDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


class PagerRecyclerAdapter(private val items: List<String>) : RecyclerView.Adapter<PagerViewHolder>() { //ViewPagerのソケットを設定

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
            PagerViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.slidepage,
                            parent,
                            false
                    )
            )

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
    val pages = listOf(
            R.drawable.tu1,
            R.drawable.tu2,
            R.drawable.tu3,
            R.drawable.tu4,
            R.drawable.tu5,
            R.drawable.tu6,
            R.drawable.tu7,
            R.drawable.tu8,
            R.drawable.tu9
    )
    val pages_en = listOf(
            R.drawable.tu1_en,
            R.drawable.tu2_en,
            R.drawable.tu3_en,
            R.drawable.tu4_en,
            R.drawable.tu5_en,
            R.drawable.tu6_en,
            R.drawable.tu7_en,
            R.drawable.tu8_en,
            R.drawable.tu9_en
    )

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
        val presetdir = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES).path + "/RAW_importer/Presets"
        val presets = File(presetdir)
        presets.mkdirs()        //専用ディレクトリを共有ストレージに作成

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
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

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

    private fun OpenPhotoGallery() {    //ドキュメントプロバイダにインテントする
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(
                Intent.createChooser(intent, "Choose Photo"),
                CHOOSE_PHOTO_REQUEST_CODE
        )
    }

    private fun About_app(){    //ポップアップダイアログを表示する
        AlertDialog.Builder(this)
            .setIcon(R.drawable.app_icon)
            .setTitle(R.string.About)
            .setMessage(R.string.About_sent1)
            .setPositiveButton("OK") { dialog, which -> }
            .show()
    }

    fun convert(
            original_name: String?,
            filename: File,
            uri: Uri,
            file:File,
            nowcount: Int,
            Itemcount: Int
    ): Int {
        var add_count = 0
        val Filename = filename
        val URI = uri
        val f = file
        val locale = Locale.getDefault()
        val lang = locale.language

        if (original_name != null) {
            if (original_name.takeLast(3) == "ORF" ||    //オリンパス
                    original_name.takeLast(3) == "CR3" ||    //キャノン
                    original_name.takeLast(3) == "CR2" ||    //キャノン
                    original_name.takeLast(3) == "CRW" ||    //キャノン
                    original_name.takeLast(3) == "SRW" ||    //ソニー
                    original_name.takeLast(3) == "ARW" ||    //ソニー
                    original_name.takeLast(3) == "NEF" ||    //ニコン
                    original_name.takeLast(3) == "NRW" ||    //ニコン
                    original_name.takeLast(3) == "PEF" ||    //ペンタックス
                    original_name.takeLast(3) == "RAF" ||    //富士フィルム
                    original_name.takeLast(3) == "X3F" ||    //シグマ
                    original_name.takeLast(3) == "RW2" ||    //パナソニック
                    original_name.takeLast(3) == "tif")      //汎用
            {
                var comp_message = "Complete!!\n(Non-RAW files will be skipped)"
                if (lang == "ja"){
                    comp_message = "変換完了！！\n(RAW形式ではないファイルはスキップされます)"
                }
                val comp_bar = Snackbar.make(
                        findViewById(R.id.linearLayout),
                        comp_message,
                        Snackbar.LENGTH_LONG
                )
                comp_bar.view.setBackgroundColor(resources.getColor(R.color.sub_point))
                comp_bar.duration = 5000

                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    convertFiles(this, nowcount, Itemcount, comp_bar, Filename, inputStream).execute()
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png") //Androidのメディアストアに登録してギャラリーでも表示可能にする
                    put("_data", f.absolutePath)
                }
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            }else{
                add_count += 1
                var comp_message = "Non-RAW files skipped"
                if (lang == "ja"){
                    comp_message = "RAW形式ではないファイルはスキップされました"
                }
                val comp_bar = Snackbar.make(
                        findViewById(R.id.linearLayout),
                        comp_message,
                        Snackbar.LENGTH_LONG
                )
                comp_bar.view.setBackgroundColor(resources.getColor(R.color.sub_point))
                comp_bar.duration = 5000
                comp_bar.show()
            }
        }
        return add_count
    }

    //バッググラウンド処理
    private class convertFiles(
            private val context: Context,
            nowcount: Int,
            Itemcount: Int,
            bar: Snackbar,
            filename: File,
            InputStream: InputStream
    ) : AsyncTask<File, Int?, Int>() {
        var progressDialog: ProgressDialog? = null
        val now = nowcount
        val stop = Itemcount
        val complete = bar
        var test = ProgressDialog(context)
        val copyRAW = filename
        val inputStream = InputStream
        override fun onPreExecute() {

            val locale = Locale.getDefault()
            val lang = locale.language
            if (now == stop-1){
                if (lang == "ja"){
                    progressDialog = ProgressDialog(context)
                    progressDialog?.setTitle("変換中...")
                    progressDialog?.setMessage("しばらくお待ちください")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()

                }else{
                    progressDialog = ProgressDialog(context)
                    progressDialog?.setTitle("Now converting...")
                    progressDialog?.setMessage("Please wait")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    test = ProgressDialog(context)
                }
            }

        }

        override fun doInBackground(vararg params: File): Int {
            try {
                val fileOutputStream = FileOutputStream(copyRAW, false)
                val buffer = ByteArray(1024)
                var length = 0
                while (inputStream.read(buffer).also { length = it } >= 0) {
                    fileOutputStream.write(buffer, 0, length)
                }
                fileOutputStream.close()
                inputStream.close()
            } catch (e: IOException) {

            }
            return 0
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            val toast: Toast = Toast.makeText(context, "$now / $stop Completed", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 700)
            toast.show()

            if (now == stop-1){
                val toast: Toast = Toast.makeText(context, R.string.Comp, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 700)
                toast.show()
                complete.show()
                progressDialog?.dismiss()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //ドキュメントプロバイダからの返り値を受け取って処理をする
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {

            val itemCount = data?.clipData?.itemCount?: 0
            val uriList = mutableListOf<Uri>()
            var skip_count = 0

            //一枚のみ変換
            if (itemCount == 0){
                var Original_Name: String? = null
                val calendar = Calendar.getInstance()
                val filename = "RW_" + (android.text.format.DateFormat.format(
                        "yyyyMMddHHmmssss_",
                        calendar
                )).toString() +0+".dng"
                val rawdir = File(
                        Environment.getExternalStorageDirectory(),
                        Environment.DIRECTORY_PICTURES
                ).path + "/RAW_importer/"

                val f = File(rawdir, filename)
                val copyRAW = File(rawdir + filename)

                data?.data?.also { uri ->
                    val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
                    val cursor: Cursor? = this.contentResolver
                            .query(uri, projection, null, null, null)
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            Original_Name = cursor.getString(
                                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                        }
                        cursor.close()
                    }
                    skip_count += convert(Original_Name, copyRAW, uri, f, 1, 2)
                }
            }

            //２枚以上変換
            for (i in 0 until itemCount) {
                val uri = data?.clipData?.getItemAt(i)?.uri
                uri?.let { uriList.add(it) }

                var Original_Name: String? = null
                val calendar = Calendar.getInstance()
                val filename = "RW_" + (android.text.format.DateFormat.format(
                        "yyyyMMddHHmmssss_",
                        calendar
                )).toString() +i+".dng"
                val rawdir = File(
                        Environment.getExternalStorageDirectory(),
                        Environment.DIRECTORY_PICTURES
                ).path + "/RAW_importer/"

                val f = File(rawdir, filename)
                val copyRAW = File(rawdir + filename)

                val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
                val cursor: Cursor? = uri?.let {
                    this.contentResolver
                            .query(it, projection, null, null, null)
                }
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        Original_Name = cursor.getString(
                                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                    }
                    cursor.close()
                }
                skip_count += convert(Original_Name, copyRAW, uriList[i], f, i, itemCount)
            }
        }
    }
}
