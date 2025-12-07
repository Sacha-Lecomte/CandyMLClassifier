package com.acs;

import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.RotateImageTransform;
import org.datavec.image.transform.WarpImageTransform;

import com.acs.utils.Utils;

import org.datavec.image.data.ImageWritable;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ImageAugmenter {

    public static void augmentFolder(String inputDir, String outputDir, int height) throws Exception {
        File in = new File(inputDir);
        File out = new File(outputDir);
        if (!out.exists()) out.mkdirs();

        NativeImageLoader loader = new NativeImageLoader();

        List<ImageTransform> transforms = Arrays.asList(
        		new FlipImageTransform(1),
        	    new FlipImageTransform(0),
        	    new RotateImageTransform(15),
        	    new RotateImageTransform(-15),
        	    new RotateImageTransform(30),
        	    new RotateImageTransform(-30),
        	    new RotateImageTransform(90),
        	    new RotateImageTransform(-90),
        	    new WarpImageTransform(10)
        );

        Java2DFrameConverter converter = new Java2DFrameConverter(); 

        for (File imgFile : in.listFiles()) {
            if (imgFile == null) continue;
            String name = imgFile.getName().toLowerCase();
            if (!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png")) continue;

            ImageWritable original = loader.asWritable(imgFile);
            int count = 0;

            for (ImageTransform t : transforms) {
                ImageWritable transformed = t.transform(original);

                BufferedImage bi = converter.getBufferedImage(transformed.getFrame());

                // Crop + Resize                
                bi = Utils.centerCropAndResize(bi, height);
                
                String base = imgFile.getName();
                int dot = base.lastIndexOf('.');
                String outName = (dot > 0) ? base.substring(0, dot) + "_" + count + ".png" : base + "_" + count + ".png";
                File newFile = new File(out, outName);

                ImageIO.write(bi, "png", newFile);
                count++;
            }
        }

        System.out.println("✔ Augmentation terminée !");
    }
    
}