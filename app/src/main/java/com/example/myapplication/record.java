package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.webkit.MimeTypeMap;

public class record extends AppCompatActivity {

    ImageButton audioRecordImageBtn;
    TextView audioRecordText;

    private Button evaluatebtn;
    private TextView script3;
    private TextView textView8;

    // 오디오 권한
    private final String recordPermission = Manifest.permission.RECORD_AUDIO;
    private final int PERMISSION_CODE = 21;

    // 오디오 파일 녹음 관련 변수
    private MediaRecorder mediaRecorder;
    private String audioFileName; // 오디오 녹음 생성 파일 이름
    private boolean isRecording = false;    // 현재 녹음 상태를 확인하기 위함.
    private Uri audioUri = null; // 오디오 파일 uri

    // 오디오 파일 재생 관련 변수
    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    ImageView playIcon;

    private AudioAdapter audioAdapter;
    private ArrayList<Uri> audioList;

    // firebase storage
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    //0526 박수영 추가
    private String recordfilename;

    boolean isAudioRecordImageBtnPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        init();

        // 파이어베이스 스토리지 초기화
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        evaluatebtn = findViewById(R.id.evaluatebtn);
        /*evaluatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(record.this,fivestartest.class);
                startActivity(intent);
            }
        });*/

        evaluatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAudioRecordImageBtnPressed) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(record.this);
                    builder.setTitle("녹음 해주세요")
                            .setMessage("녹음을 완료해야 평가창으로 넘어갈 수 있습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                } else {
                    Intent intent = new Intent(record.this, fivestartest.class);
                    startActivity(intent);
                }
            }
        });

        script3 = findViewById(R.id.script3);
        TextView text = (TextView)findViewById(R.id.script3);
        text.setMovementMethod(new ScrollingMovementMethod());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("output");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String htmlText = dataSnapshot.getValue(String.class);
                Spanned spannedText = Html.fromHtml(htmlText);
                script3.setText(spannedText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the data retrieval is canceled
            }

        });

        textView8 = findViewById(R.id.textView8);

        String fullText = "    빨간색 단어는 강조단어입니다.  \n   조금 더 높게 세게 읽으세요. ";
        String subText = "빨간색";

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fullText);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
        int startIndex = fullText.indexOf(subText);
        int endIndex = startIndex + subText.length();
        spannableStringBuilder.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView8.setText(spannableStringBuilder);
    }


    // 리사이클러뷰 생성 및 클릭 이벤트
    private void init() {

        audioRecordImageBtn = findViewById(R.id.audioRecordImageBtn);
        audioRecordText = findViewById(R.id.audioRecordText);

        audioRecordImageBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording) {
                    // 현재 녹음 중 O
                    // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                    isRecording = false; // 녹음 상태 값
                    audioRecordImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_record, null)); // 녹음 상태 아이콘 변경
                    audioRecordText.setText(""); // 녹음 상태 텍스트 변경
                    stopRecording();

                    // 녹화 이미지 버튼 변경 및 리코딩 상태 변수값 변경
                } else {
                    // 현재 녹음 중 X
                    /*절차
                     *       1. Audio 권한 체크
                     *       2. 처음으로 녹음 실행한건지 여부 확인
                     * */
                    if(checkAudioPermission()) {
                        // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                        isRecording = true; // 녹음 상태 값
                        audioRecordImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_recording_red, null)); // 녹음 상태 아이콘 변경
                        audioRecordText.setText("녹음 중"); // 녹음 상태 텍스트 변경
                        startRecording();

                    }
                }
                isAudioRecordImageBtnPressed = true;
            }
        });


        // 리사이클러뷰
        RecyclerView audioRecyclerView = findViewById(R.id.recyclerview);

        audioList = new ArrayList<>();
        audioAdapter = new AudioAdapter(this, audioList);
        audioRecyclerView.setAdapter(audioAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);

        // 커스텀 이벤트 리스너 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnIconClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String uriName = String.valueOf(audioList.get(position));

                File file = new File(uriName);

                if(isPlaying){

                    if(playIcon == view){

                        stopAudio();

                    } else {

                        stopAudio();

                        playIcon = (ImageView)view;
                        playAudio(file);
                    }
                } else {
                    playIcon = (ImageView)view;
                    playAudio(file);
                }
            }
        });
    }

    // 오디오 파일 권한 체크
    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }


    // 녹음 시작
    private void startRecording() {
        String recordPath = getExternalFilesDir("/").getAbsolutePath();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = recordPath + "/" +"Speech King_" + timeStamp + "_"+"audio.wav";

        //0526 realtime database 전달
        recordfilename = "recordings/" + "Speech King_" + timeStamp + "_" + "audio.wav";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    // 녹음 종료
    private void stopRecording() {

        // 녹음 종료 종료
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        audioUri = Uri.fromFile(new File(audioFileName));

        //0526 업로드할 파일 경로
        StorageReference audioRef = storageReference.child("recordings").child(audioUri.getLastPathSegment());

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/wav")
                .build();

        // 녹음 파일 업로드
        audioRef.putFile(audioUri, metadata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 업로드 성공 시 처리
                        Toast.makeText(record.this, "녹음 파일이 업로드되었습니다.", Toast.LENGTH_SHORT).show();

                        // 업로드된 파일의 다운로드 URL 가져오기
                        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                String downloadUrl = downloadUri.toString();
                                // TODO: 업로드된 파일의 다운로드 URL 활용
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 다운로드 URL 가져오기 실패 시 처리
                                Toast.makeText(record.this, "다운로드 URL 가져오기 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 업로드 실패 시 처리
                        Toast.makeText(record.this, "녹음 파일 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        audioUri = Uri.parse(audioFileName);

        audioList.add(audioUri);

        audioAdapter.notifyDataSetChanged();

        //0526 realtime database 전달
        String firebaseFileName = recordfilename;

        DatabaseReference filenameRef = FirebaseDatabase.getInstance().getReference().child("filename");
        filenameRef.setValue(firebaseFileName);
    }

    // 녹음 파일 재생
    private void playAudio(File file) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_pause, null));
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
            }
        });

    }

    // 녹음 파일 중지
    private void stopAudio() {
        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_play, null));
        isPlaying = false;
        mediaPlayer.stop();
    }


}