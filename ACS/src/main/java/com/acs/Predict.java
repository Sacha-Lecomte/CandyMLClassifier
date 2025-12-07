package com.acs;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import org.datavec.image.loader.NativeImageLoader;

public class Predict {

    public static void main(String[] args) throws Exception {

        // === Charger le modèle ===
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("model_bonbons.zip"), true);
        System.out.println("📦 Modèle chargé !");

        // === Liste des labels dans le même ordre que les dossiers ===
        String[] labels = {
            "Multiple",
            "crocodile_orange",
            "crocodile_rouge",
            "crocodile_vert",
            "dragibus_bleu",
            "dragibus_jaune",
            "dragibus_noir",
            "dragibus_rose",
            "dragibus_rouge",
            "dragibus_vert",
            "grand_schtroumpf",
            "oeuf",
            "ourson_blanc",
            "ourson_rouge",
            "ourson_vert",
            "schtroumpfette",
            "tagada"
        };

        // === Fenêtre sélection d'image ===
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("📷 Sélectionnez une image à tester");
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));

        int result = chooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("❗ Aucune image sélectionnée. Fin du programme.");
            return;
        }

        File file = chooser.getSelectedFile();
        System.out.println("🖼 Image sélectionnée : " + file.getAbsolutePath());

        // === Paramètres du modèle ===
        int height = 128;
        int width = 128;
        int channels = 3;

        NativeImageLoader loader = new NativeImageLoader(height, width, channels);
        INDArray image = loader.asMatrix(file);

        // === Normalisation 0–1 (comme à l'entraînement) ===
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);

        // === Prédiction ===
        INDArray output = model.output(image, false);
        int predictedClass = output.argMax(1).getInt(0);

        System.out.println("🎯 Classe prédite : " + predictedClass);
        System.out.println("🍬 Bonbon reconnu : " + labels[predictedClass]);
    }
}
