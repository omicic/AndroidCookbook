package com.example.androidcookbook.work;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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
    public Bitmap ShrinkBitmap(Context context, Bitmap bitmap) throws IOException {

        //ContentResolver cr = context.getContentResolver();
       // Bitmap bitmap = MediaStore.Images.Media
      //          .getBitmap(cr, Uri.fromFile(new File(selectedImagePath)));

        float density = context.getResources().getDisplayMetrics().density;
        int bounding = Math.round((float)250 * density);
        float xScale = ((float) bounding) / bitmap.getWidth();
        float yScale = ((float) bounding) / bitmap.getHeight();
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scale, scale);


        // RECREATE THE NEW BITMap
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return scaledBitmap;
    }

    public Bitmap ShrinkBitmap(Context context, String selectedImagePath) throws IOException {

        ContentResolver cr = context.getContentResolver();
         Bitmap bitmap = MediaStore.Images.Media
                  .getBitmap(cr, Uri.fromFile(new File(selectedImagePath)));

        float density = context.getResources().getDisplayMetrics().density;
        int bounding = Math.round((float)250 * density);
        float xScale = ((float) bounding) / bitmap.getWidth();
        float yScale = ((float) bounding) / bitmap.getHeight();
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scale, scale);


        // RECREATE THE NEW BITMap
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return scaledBitmap;
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



    public String TakePicture(int nextid) {

        //Uri selectedImageUri = data.getData();

        String selectedImagePath = "";

        //Log.d("selectedImagePath Utilities Take Picture",selectedImagePath);

        File folder = new File(Environment.getExternalStorageDirectory() + "/recipeimage");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        File to = new File(Environment.getExternalStorageDirectory() + "/recipeimage/" + Integer.toString(nextid) + "recipe.jpg");

        selectedImagePath = Environment.getExternalStorageDirectory() + "/recipeimage/" + Integer.toString(nextid) + "recipe.jpg";

        return selectedImagePath;
    }

}