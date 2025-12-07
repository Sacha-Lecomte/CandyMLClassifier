package com.acs.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Utils {

    public static BufferedImage centerCropAndResize(BufferedImage src, int size) {
        int width = src.getWidth();
        int height = src.getHeight();

        // 1. Crop centré
        int cropSize = Math.min(width, height);
        int x = (width - cropSize) / 2;
        int y = (height - cropSize) / 2;
        
        // 2. Resize
        BufferedImage cropped = src.getSubimage(x, y, cropSize, cropSize);
        BufferedImage resized = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(cropped, 0, 0, size, size, null);
        g.dispose();

        return resized;
    }

    public static void deleteDirectory(File dir) {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) deleteDirectory(f);
                else f.delete();
            }
        }
        dir.delete();
    }

    public static void preprocessFolder(String inputDir, String outputDir, int size) throws Exception {

        File in = new File(inputDir);
        File out = new File(outputDir);

        if (!out.exists()) out.mkdirs();

        File[] classFolders = in.listFiles(File::isDirectory);

        if (classFolders == null || classFolders.length == 0) {
            System.out.println("⚠ Aucun dossier de classe trouvé dans : " + inputDir);
            return;
        }

        for (File classFolder : classFolders) {

            File outClass = new File(out, classFolder.getName());
            outClass.mkdirs();

            File[] images = classFolder.listFiles(f -> {
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".png");
            });

            if (images == null || images.length == 0) {
                System.out.println("⚠ Aucun fichier image dans : " + classFolder.getAbsolutePath());
                continue;
            }

            for (File imgFile : images) {
                BufferedImage img = ImageIO.read(imgFile);
                img = centerCropAndResize(img, size);
                ImageIO.write(img, "png", new File(outClass, imgFile.getName()));
            }
        }

        System.out.println("✔ Prétraitement des images test terminé !");
    }
}
