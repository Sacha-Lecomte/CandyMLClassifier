package com.acs;

import java.io.File;
import java.util.Scanner;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;

import com.acs.utils.Utils;

public class Main {

    public static void main(String[] args) throws Exception {

        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");
        Scanner scanner = new Scanner(System.in);

        int height = 128;
        int width = 128;
        int channels = 3; 
        int batchSize = 8;
        int numClasses = 19;

        File preprocessedTrain = new File("ACS/dataset_preprocessed/train");
        File preprocessedTest  = new File("ACS/dataset_preprocessed/test");

        // ================================
        //  TRAITEMENT TRAIN
        // ================================
        if (!preprocessedTrain.exists()) {
            // ➤ Le dossier n'existe pas → génération automatique
            System.out.println("📁 Le dossier dataset_preprocessed/train n'existe pas.");
            System.out.println("➡ Génération automatique de la banque d’images d’entraînement.");

            generateTrainDataset(height);

        } else {
            // ➤ Le dossier existe → demander si on régénère
            System.out.print("Le dossier dataset_preprocessed/train existe déjà. "
                    + "Voulez-vous le régénérer ? (o/n) : ");
            String rep = scanner.nextLine().trim().toLowerCase();

            if (rep.equals("o") || rep.equals("oui") || rep.equals("y")) {
                System.out.println("🔄 Régénération du dossier d’entraînement…");
                Utils.deleteDirectory(new File("ACS/dataset_preprocessed/train"));
                generateTrainDataset(height);
            } else {
                System.out.println("⏩ Dossier d’entraînement conservé.");
            }
        }

        // ================================
        //  TRAITEMENT TEST
        // ================================
        if (!preprocessedTest.exists()) {
            System.out.println("📁 Le dossier dataset_preprocessed/test n'existe pas.");
            System.out.println("➡ Prétraitement automatique des images test.");
            Utils.preprocessFolder("ACS/dataset/test", "ACS/dataset_preprocessed/test", height);

        } else {
            System.out.print("Le dossier ACS/dataset_preprocessed/test existe déjà. "
                    + "Voulez-vous le régénérer ? (o/n) : ");
            String rep = scanner.nextLine().trim().toLowerCase();

            if (rep.equals("o") || rep.equals("oui") || rep.equals("y")) {
                System.out.println("🔄 Régénération du prétraitement test…");
                Utils.deleteDirectory(new File("dataset_preprocessed/test"));
                Utils.preprocessFolder("ACS/dataset/test", "ACS/dataset_preprocessed/test", height);
            } else {
                System.out.println("⏩ Images test conservées.");
            }
        }

        scanner.close();

        // ================================
        //  CHARGEMENT DES DONNÉES
        // ================================
        DataSetIterator trainIter = CandyDataLoader.loadData("ACS/dataset_preprocessed/train",
                height, width, channels, batchSize, numClasses);

        DataSetIterator testIter = CandyDataLoader.loadData("ACS/dataset_preprocessed/test",
                height, width, channels, batchSize, numClasses);

        // ================================
        //  MODÈLE
        // ================================
        MultiLayerNetwork model = CandyModel.createModel(height, width, channels, numClasses);
        model.setListeners(new ScoreIterationListener(10));
        
        System.out.println("Démarrage de l'entraînement...");
        for (int epoch = 0; epoch < 10; epoch++) {
            model.fit(trainIter);
            System.out.println("Époque " + epoch + " terminée");
        }

        // ================================
        //  ÉVALUATION
        // ================================
        Evaluation eval = model.evaluate(testIter);
        System.out.println(eval.stats());

        model.save(new java.io.File("model_bonbons.zip"), true);

        System.out.println("✅ Entraînement terminé !");
    }

    // Fonction utilitaire pour générer TOUTE la banque training
    private static void generateTrainDataset(int height) throws Exception {
        File trainDir = new File("ACS/dataset/train");
        File[] classes = trainDir.listFiles(File::isDirectory);

        for (File classe : classes) {
            String src = classe.getAbsolutePath();
            String dst = "ACS/dataset_preprocessed/train/" + classe.getName();

            System.out.println("🔄 Augmentation : " + classe.getName());
            ImageAugmenter.augmentFolder(src, dst, height);
        }
    }
}
