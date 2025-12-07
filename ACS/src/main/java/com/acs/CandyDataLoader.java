package com.acs;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.util.Random;

public class CandyDataLoader {

    public static DataSetIterator loadData(String dataDir, int height, int width, int channels, int batchSize, int numClasses) throws Exception {
        File parentDir = new File(dataDir);
        FileSplit fileSplit = new FileSplit(parentDir, NativeImageLoader.ALLOWED_FORMATS, new Random(123));

        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator(); // le nom du dossier = label
        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);
        recordReader.initialize(fileSplit);

        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numClasses);

        // Normalisation des pixels (0-255 -> 0-1)
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        return dataIter;
    }
}
