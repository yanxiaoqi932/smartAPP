package com.example.xhello;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.ml.common.base.SmartLog;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentation;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting;


import java.io.IOException;

import utils.BitmapUtils;
import utils.Constant;
import utils.ImageUtils;

/**
 * Created on 2021/2/19.
 */

public class StillCutPhotoActivity extends Activity {
    private RelativeLayout relativeLayoutLoadPhoto, relativeLayoutCut, relativeLayoutSave;
    private ImageView preview;
    private Uri imageUri;
    private Bitmap originBitmap, backgroundBitmap;
    private static String TAG = "CaptureImageFragment";
    private Integer maxWidthOfImage;
    private Integer maxHeightOfImage;
    boolean isLandScape;
    private int REQUEST_CHOOSE_ORIGINPIC = 2001;
    private static final String KEY_IMAGE_URI = "KEY_IMAGE_URI";
    private static final String KEY_IMAGE_MAX_WIDTH =
            "KEY_IMAGE_MAX_WIDTH";
    private static final String KEY_IMAGE_MAX_HEIGHT =
            "KEY_IMAGE_MAX_HEIGHT";
    private Bitmap processedImage;
    // Portrait foreground image.
    private Bitmap foreground;
    private int index;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.setContentView(R.layout.activity_still_cut);
        this.preview = this.findViewById(R.id.previewPane);
        Intent intent = this.getIntent();
        try {
            this.index = intent.getIntExtra(Constant.VALUE_KEY, -1);
        } catch (RuntimeException e) {
            SmartLog.e(StillCutPhotoActivity.TAG, "Get intent value failed:" + e.getMessage());
        }
        this.findViewById(R.id.backSec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StillCutPhotoActivity.this.finish();
            }
        });
        this.isLandScape =
                (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        this.initView();
        this.initAction();
    }

    private void initView() {
        this.relativeLayoutLoadPhoto = this.findViewById(R.id.relativate_chooseImg);
        this.relativeLayoutCut = this.findViewById(R.id.relativate_cut);
        this.relativeLayoutSave = this.findViewById(R.id.relativate_save);
        this.preview = this.findViewById(R.id.previewPane);
    }

    private void initAction() {
        this.relativeLayoutLoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StillCutPhotoActivity.this.selectLocalImage(StillCutPhotoActivity.this.REQUEST_CHOOSE_ORIGINPIC);
            }
        });

        // Outline the edge.
        this.relativeLayoutCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StillCutPhotoActivity.this.imageUri == null) {
                    System.out.println("???????????????");
                    Toast.makeText(StillCutPhotoActivity.this.getApplicationContext(), R.string.please_select_picture, Toast.LENGTH_SHORT).show();
                } else {
                    StillCutPhotoActivity.this.createImageTransactor();
                    Toast.makeText(StillCutPhotoActivity.this.getApplicationContext(), R.string.cut_success, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Save the processed picture.
        this.relativeLayoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StillCutPhotoActivity.this.processedImage == null) {
                    Toast.makeText(StillCutPhotoActivity.this.getApplicationContext(), R.string.no_pic_neededSave, Toast.LENGTH_SHORT).show();
                    try {
                        throw new Exception("null processed image");
                    } catch (Exception e) {
                        SmartLog.e(StillCutPhotoActivity.TAG, e.getMessage());
                    }
                } else {
                    ImageUtils imageUtils = new ImageUtils(StillCutPhotoActivity.this.getApplicationContext());
                    imageUtils.saveToAlbum(StillCutPhotoActivity.this.processedImage);
                    Toast.makeText(StillCutPhotoActivity.this.getApplicationContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectLocalImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == this.REQUEST_CHOOSE_ORIGINPIC) && (resultCode == Activity.RESULT_OK)) {
            // In this case, imageUri is returned by the chooser, save it.
            this.imageUri = data.getData();
            this.loadOriginImage();
        }
    }

    private void changeBackground() {
        if (this.index < 0) {
            System.out.println("?????????index < 0???");
            Toast.makeText(this.getApplicationContext(), R.string.please_select_picture, Toast.LENGTH_SHORT).show();
        } else {
            int id = Constant.IMAGES[this.index];
            this.loadOriginImage();
            Pair<Integer, Integer> targetedSize = this.getTargetSize();
            this.backgroundBitmap = BitmapUtils.loadFromPath(StillCutPhotoActivity.this, id, targetedSize.first, targetedSize.second);
        }
        if((this.isChosen(this.foreground))){
            System.out.println("foreground??????");
        }
        if(this.isChosen(backgroundBitmap)){
            System.out.println("backgroundBitmap??????");
        }
        //foreground????????????????????????????????????
        if (this.isChosen(this.foreground) &&this.isChosen(backgroundBitmap)) {
            System.out.println("???????????????");
            BitmapDrawable drawable = new BitmapDrawable(backgroundBitmap);
            this.preview.setDrawingCacheEnabled(true);
            this.preview.setBackground(drawable);
            this.preview.setImageBitmap(this.foreground);
            this.processedImage = Bitmap.createBitmap(this.preview.getDrawingCache());
            this.preview.setDrawingCacheEnabled(false);
        } else {
            Toast.makeText(this.getApplicationContext(), R.string.please_select_picture, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private MLImageSegmentationAnalyzer analyzer;

    private void createImageTransactor() {
        MLImageSegmentationSetting setting = new MLImageSegmentationSetting.Factory()
                .setAnalyzerType(MLImageSegmentationSetting.BODY_SEG)
                .setExact(true)
                .create();
        this.analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting);
        if (this.isChosen(this.originBitmap)) {
            System.out.println("??????????????????????????????");
            MLFrame mlFrame = new MLFrame.Creator().setBitmap(this.originBitmap).create();
            Task<MLImageSegmentation> task = this.analyzer.asyncAnalyseFrame(mlFrame);
            task.addOnSuccessListener(new OnSuccessListener<MLImageSegmentation>() {
                @Override
                public void onSuccess(MLImageSegmentation mlImageSegmentationResults) {
                    // Transacting logic for segment success.
                    if (mlImageSegmentationResults != null) {
                        System.out.println("????????????");
                        StillCutPhotoActivity.this.foreground = mlImageSegmentationResults.getForeground();
                        StillCutPhotoActivity.this.preview.setImageBitmap(StillCutPhotoActivity.this.foreground);
                        StillCutPhotoActivity.this.processedImage = ((BitmapDrawable) ((ImageView) StillCutPhotoActivity.this.preview).getDrawable()).getBitmap();
                        StillCutPhotoActivity.this.changeBackground();
                    } else {
                        StillCutPhotoActivity.this.displayFailure();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    // Transacting logic for segment failure.
                    StillCutPhotoActivity.this.displayFailure();
                    return;
                }
            });
        } else {
            Toast.makeText(this.getApplicationContext(), R.string.please_select_picture, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void displayFailure() {
        Toast.makeText(this.getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
    }

    private boolean isChosen(Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        } else {
            return true;
        }
    }

    private void loadOriginImage() {
        if (this.imageUri == null) {
            return;
        }
        Pair<Integer, Integer> targetedSize = this.getTargetSize();
        int targetWidth = targetedSize.first;
        int maxHeightOfImage = targetedSize.second;
        this.originBitmap = BitmapUtils.loadFromPath(StillCutPhotoActivity.this, this.imageUri, targetWidth, maxHeightOfImage);
        // Determine how much to scale down the image.
        SmartLog.i(StillCutPhotoActivity.TAG, "resized image size width:" + this.originBitmap.getWidth() + ",height: " + this.originBitmap.getHeight());
        this.preview.setImageBitmap(this.originBitmap);
    }

    // Returns max width of image.
    private Integer getMaxWidthOfImage() {
        if (this.maxWidthOfImage == null) {
            if (this.isLandScape) {
                this.maxWidthOfImage = ((View) this.preview.getParent()).getHeight();
            } else {
                this.maxWidthOfImage = ((View) this.preview.getParent()).getWidth();
            }
        }
        return this.maxWidthOfImage;
    }

    // Returns max height of image.
    private Integer getMaxHeightOfImage() {
        if (this.maxHeightOfImage == null) {
            if (this.isLandScape) {
                this.maxHeightOfImage = ((View) this.preview.getParent()).getWidth();
            } else {
                this.maxHeightOfImage = ((View) this.preview.getParent()).getHeight();
            }
        }
        return this.maxHeightOfImage;
    }

    // Gets the targeted size(width / height).
    private Pair<Integer, Integer> getTargetSize() {
        Integer targetWidth;
        Integer targetHeight;
        Integer maxWidthOfImage = this.getMaxWidthOfImage();
        Integer maxHeightOfImage = this.getMaxHeightOfImage();
        targetWidth = this.isLandScape ? maxHeightOfImage : maxWidthOfImage;
        targetHeight = this.isLandScape ? maxWidthOfImage : maxHeightOfImage;
        SmartLog.i(StillCutPhotoActivity.TAG, "height:" + targetHeight + ",width:" + targetWidth);
        return new Pair<>(targetWidth, targetHeight);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
                SmartLog.e(StillCutPhotoActivity.TAG, "Stop analyzer failed: " + e.getMessage());
            }
        }
        this.imageUri = null;
        this.index = -1;
        BitmapUtils.recycleBitmap(this.originBitmap, this.backgroundBitmap, this.foreground, this.processedImage);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(StillCutPhotoActivity.KEY_IMAGE_URI, this.imageUri);
        if (this.maxWidthOfImage != null) {
            outState.putInt(StillCutPhotoActivity.KEY_IMAGE_MAX_WIDTH, this.maxWidthOfImage);
        }
        if (this.maxHeightOfImage != null) {
            outState.putInt(StillCutPhotoActivity.KEY_IMAGE_MAX_HEIGHT, this.maxHeightOfImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

