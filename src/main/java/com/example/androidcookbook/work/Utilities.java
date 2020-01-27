package com.example.androidcookbook.work;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Utilities {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // shrink bitmap
    Bitmap ShrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }


    /**
     * helper to retrieve the path of an image URI
     *
     * @param activity
     */
    public String getPath(Context context, Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }

        cursor.close();
        return res;
    }


    public String TakePicture(Context context, Intent data, int nextid) {

        Uri selectedImageUri = data.getData();

        String selectedImagePath = getPath(context, selectedImageUri);

        //Log.d("selectedImagePath Utilities Take Picture",selectedImagePath);

        File folder = new File(Environment.getExternalStorageDirectory() + "/recipeimage");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        //int nextid = Integer.parseInt(dbRecept.getLastId()) + 1; //uzmi poslednji id recepta i dodaj 1
        //zamena putanje
        File from = new File(selectedImagePath);
        File oldfile = new File(selectedImagePath);
        File to = new File(Environment.getExternalStorageDirectory() + "/recipeimage/" + Integer.toString(nextid) + "recipe.jpg");

        from.renameTo(to);

        //delete oldfile from gallery
        try {

            context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA
                            + "='"
                            + oldfile.getPath()
                            + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectedImagePath = Environment.getExternalStorageDirectory() + "/recipeimage/" + Integer.toString(nextid) + "recipe.jpg";

        return selectedImagePath;
    }
}
