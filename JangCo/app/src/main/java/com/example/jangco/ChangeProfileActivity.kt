package com.example.jangco

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_change_profile.*
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileInputStream


class ChangeProfileActivity : AppCompatActivity(),View.OnClickListener {


    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference

    private lateinit var dataBaseHelper: DataBaseHelper

    private var userProfile: User? = null
    var dirPath: String? = null
    var contentUri: Uri? = null
    var picPath: String? = null

    lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        loadingDialog = CustomLoadingDialog(CustomLoadingDialog.DATA_LOADING_DIALOG_TYPE,this)
        loadingDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        userProfile = intent.getSerializableExtra("userProfile") as User
        changeProfileActivityNickNameEditText.setText(userProfile?.nickName)
        settingProfileImage()

        setFilePath()
        settingToolBar()
        dataBaseHelper =  DataBaseHelper(userProfile?.id!!)

        changeProfileActivityCameraButton.setOnClickListener(this)
        changeProfileActivityAlbumButton.setOnClickListener(this)
        changeProfileActivityUpdateButton.setOnClickListener(this)
        changeProfileActivityCancelButton.setOnClickListener(this)

        // 뷰안에 Drawable을 터치시 터치 이벤트 등록.
        changeProfileActivityNickNameEditText.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= changeProfileActivityNickNameEditText.getRight() - changeProfileActivityNickNameEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                    changeProfileActivityNickNameEditText.text = null
                    return@OnTouchListener true
                }
            }
            false
        })
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.changeProfileActivityCameraButton -> {
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // 저장될 사진의 이름.
                var fileName  = "temp_${System.currentTimeMillis()}.jpg"
                // 폴더 경로+ 사진이름 을 통해 최종 경로 생성.
                picPath = "${dirPath}/${fileName}"
                // 최종 경로를 통한 사진파일 생성.
                var file = File(picPath)

                // 7.0 마쉬멜로우 이상부턴 어플간 파일을 공유하는 기능을 이용해야 된다.
                // 그냥 파일 경로를 통해서 파일을 가져오면 오류가 생겨 앱이 터진다.

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    contentUri = FileProvider.getUriForFile(this, "com.example.jangco.file_provider", file)
                }else {
                    contentUri = Uri.fromFile(file)
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                startActivityForResult(intent, 1)
            }
            R.id.changeProfileActivityAlbumButton -> {
                var intent = Intent(Intent.ACTION_PICK)
                intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(intent,2)
            }
            R.id.changeProfileActivityUpdateButton -> {
                if (!picPath.isNullOrEmpty()) {
                    loadingDialog.show()
                    val stream = FileInputStream(File(picPath))
                    var userProfileImagesRef: StorageReference? =
                        storageRef.child("userProfileImages/${userProfile?.id}")
                    val uploadTask = userProfileImagesRef?.putStream(stream)
                    uploadTask?.addOnFailureListener {
                        // Handle unsuccessful uploads
                        Log.d("Upload", "실패")
                        Toast.makeText(this, "프로필 변경에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
                    }?.addOnSuccessListener {
                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                        // ...
                        Log.d("Upload", "성공")
                        // Toast.makeText(this,"프로필 변경 성공",Toast.LENGTH_LONG).show()
                        // task가 성공시 URL을  유저데이터베이스에 넣어주는 메소드.
                        updateUserProfile()
                    }
                }else if(!userProfile?.nickName?.equals(changeProfileActivityNickNameEditText.text.toString())!!){
                    loadingDialog.show()
                    updateUserNickNameOnly()
                }
                else{
                    Toast.makeText(this, "프로필 변경된 사항이 없습니다.", Toast.LENGTH_LONG).show()
                }
            }
            R.id.changeProfileActivityCancelButton -> {
                finish()
            }

        }
    }

    private fun updateUserNickNameOnly() {
        userProfile?.nickName = changeProfileActivityNickNameEditText.text.toString()
        dataBaseHelper.upDateUserProfile(userProfile!!)

        intent.putExtra("userProfile", userProfile)
        setResult(RESULT_OK, intent)
        loadingDialog.dismiss()
        finish()
    }

    private fun updateUserProfile() {
        var ref = storageRef.child("userProfileImages/${userProfile?.id}")
        ref.downloadUrl.addOnCompleteListener {

            var url = it.result.toString()
            userProfile?.proFileImageUri = url
            userProfile?.nickName = changeProfileActivityNickNameEditText.text.toString()
            dataBaseHelper.upDateUserProfile(userProfile!!)

            intent.putExtra("userProfile",userProfile)
            setResult(RESULT_OK, intent)
            loadingDialog.dismiss()
            finish()
        }
    }

    private fun settingProfileImage() {
        if (!userProfile?.proFileImageUri.isNullOrEmpty()) {
            val requestOptions = RequestOptions()
                // .skipMemoryCache(true)    //캐시 사용 해제, Firebase 사용 시 느리기 때문에 사용 필수
                // .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_loading) // 로딩시.
                .error(R.drawable.ic_default_user_profile_256dp) // 이미지 없을때.
            Glide.with(this)
                .load(userProfile?.proFileImageUri)
                .apply(requestOptions)
                .thumbnail(0.5f)
                .into(changeProfileActivityProImageCircleVIew)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            1 -> {
                if(resultCode == Activity.RESULT_OK) {
                    var bitmap = BitmapFactory.decodeFile(contentUri?.path)
                    bitmap = resizeBitmap(1024, bitmap)

                    var degree = getDegree(contentUri?.path!!)
                    changeProfileActivityProImageCircleVIew.rotation = degree
                    changeProfileActivityProImageCircleVIew.setImageBitmap(bitmap)
                }
            }
            2 -> {
                if(resultCode == Activity.RESULT_OK) {
                    var cursor = contentResolver.query(data?.data, null ,null,null,null)
                    cursor.moveToNext()

                    var index  = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    // 사진 파일의 경로
                    var sources =  cursor.getString(index)
                    picPath = cursor.getString(index)
                    var bitmap = BitmapFactory.decodeFile(sources)
                    bitmap = resizeBitmap(1024, bitmap)

                    var degree = getDegree(sources)
                    changeProfileActivityProImageCircleVIew.rotation = degree
                    changeProfileActivityProImageCircleVIew.setImageBitmap(bitmap)
                }
            }
        }
    }


    fun setFilePath() {
        Log.d("test", "setFilePath")
        var tempPath = Environment.getExternalStorageDirectory().absolutePath
        dirPath = "${tempPath}/Android/data/${packageName}"

        // 파일 객체에 파일 경로를 넣어줌.
        var file = File(dirPath)
        // 경로에 파일이 없다면, 폴더 생성.
        if (!file.exists()) {
            file.mkdir()
        }
    }

    //이미지 크기 보정.
    fun resizeBitmap(targetWidth: Int, source: Bitmap): Bitmap {

        var ratio = source.height.toDouble() / source.width.toDouble()
        var targetHeight = (targetWidth * ratio).toInt()

        var result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        // source 이미지 삭제.
        if (result != source) {
            source.recycle()
        }
        return result
    }

    // 이미지 회전.
    fun getDegree(source: String): Float{
        // 사진 경로를 가지고 와서 exif 인터페이스 생성.
        var exif = ExifInterface(source)
        //
        var degree = 0
        var ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

        when (ori) {

            ExifInterface.ORIENTATION_ROTATE_90 -> {
                degree = 90
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                degree = 180
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                degree = 270
            }
        }
        return degree.toFloat()
    }

    private fun settingToolBar(){
        setSupportActionBar(changeProfileActivityToolbar)
        // 액션바의 커스텀 허용
        supportActionBar?.setDisplayShowCustomEnabled(true)
        // 기존 액션바 요소 안보이도록 설정.
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 홈버튼 비활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) // 타이틀 안보이게
        supportActionBar?.setDisplayShowHomeEnabled(false) // 홈버튼 안보이게

        var toolbar = layoutInflater.inflate(R.layout.activity_change_toolbar, null)
        supportActionBar?.customView = toolbar

        var title = toolbar.findViewById<TextView>(R.id.changeToolbarTitleTextView)
        title.text = getString(R.string.moreInfoFragmentMyInfoProfileChange)

        var closeButton = toolbar.findViewById<ImageView>(R.id.changeToolbarBackImageView)
        closeButton.setOnClickListener { view ->
            finish()
        }
    }
}






