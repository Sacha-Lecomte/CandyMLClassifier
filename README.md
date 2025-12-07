# Reconnaissance Bonbons IA (Deep Learning)

**Modèle DL pour reconnaître 18 types de bonbons Haribo sur photos, développé en Java 11 LTS avec DeepLearning4J dans le cadre du cours Intelligence Artificielle à Grenoble INP-ESISAR.** Classification multi-classe (Dragibus, Schtroumpf, Crocodile, Ourson...) avec dataset augmenté. [![Licence MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 Table des matières
- [À propos](#à-propos)
- [Bonbons reconnus](#bonbons-reconnus)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Dataset](#dataset)
- [Technologies](#technologies)
- [Auteurs](#auteurs)
- [Licence](#licence)

## À propos
**Projet Deep Learning du cours IA Grenoble INP-ESISAR** : Modèle CNN entraîné pour classifier **18 variétés de bonbons Haribo** sur images 128x128px. Pipeline complet : **prétraitement + data augmentation** (rotations, symétries) + entraînement + évaluation. Modèle sauvegardé : `model_bonbons.zip`.

## Bonbons reconnus
| Famille | Variétés |
|---------|----------|
| **Dragibus** | Noir, Vert, Bleu, Rose, Jaune, Rouge |
| **Schtroumpf** | Schtroumpfette (Jaune), Grand Schtroumpf (Rouge), Schtroumpf (Blanc) |
| **Crocodile** | Vert, Bleu, Orange, Jaune |
| **Ourson** | Vert, Jaune, Blanc, Orange, Rouge |
| **Autres** | Œuf, Fraise Tagada, Multiple |

## Prérequis
- **Java 11 LTS** (vérifié : 11.0.29)
- **Maven 3.6+**
- IDE : **VSCode** (extension Java/Maven) ou **Eclipse**
- **4GB RAM minimum** (entraînement DL)

## Installation
1. Cloner le repo :
```
git clone [https://github.com/Sacha-Lecomte/CandyMLClassifier](https://github.com/Sacha-Lecomte/CandyMLClassifier)
cd CandyMLClassifier
```
2. Dépendances Maven :
```
mvn clean install
```
3. Ajouter des images dans les dossier dataset/train et dataset/test

## Utilisation

### 1. Entraînement complet
1. Ouvre dans **VSCode** ou **Eclipse**
2. Lance le fichier principal :
```
src/main/java/com/acs/Main.java
```

- VSCode : `Ctrl+F5`
- Eclipse : Clic droit → **Run As → Java Application**

**Étapes automatiques** :
🔄 Prétraitement + augmentation dataset train (rotations, symétries)
🎯 Entraînement CNN sur images 128x128px
📊 Évaluation sur dataset test → Résultats console
💾 Sauvegarde model_bonbons.zip


**Sortie console exemple** :
```
========================Evaluation Metrics========================
# of classes: 17
Accuracy: 0,9362
Precision: 0,9271 (1 class excluded from average)
Recall: 0,9098
F1 Score: 0,9389 (1 class excluded from average)
Precision, recall & F1: macro-averaged (equally weighted avg. of 17 classes)
Warning: 1 class was never predicted by the model and was excluded from average precision Classes excluded from average precision: [2]
=========================Confusion Matrix=========================
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16
----------------------------------------------------
2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 0 = Multiple
0 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 1 = crocodile_orange
0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 | 2 = crocodile_rouge
0 0 0 2 0 0 0 0 0 0 0 0 0 0 0 0 0 | 3 = crocodile_vert
0 0 0 0 3 0 0 0 0 0 0 0 0 0 0 0 0 | 4 = dragibus_bleu
0 0 0 0 0 4 0 0 0 0 0 0 0 0 0 0 0 | 5 = dragibus_jaune
0 0 0 0 0 0 3 0 0 0 0 0 0 0 0 0 0 | 6 = dragibus_noir
0 0 0 0 0 0 0 4 0 0 0 1 0 0 0 0 0 | 7 = dragibus_rose
0 0 0 0 0 0 0 0 3 0 0 0 0 0 0 0 0 | 8 = dragibus_rouge
0 0 0 0 0 0 0 0 0 5 0 0 0 0 0 0 0 | 9 = dragibus_vert
0 0 0 0 0 0 0 0 0 0 2 0 0 0 0 0 0 | 10 = grand_schtroumpf
0 1 0 0 0 0 0 0 0 0 0 2 0 0 0 0 0 | 11 = oeuf
0 0 0 0 0 0 0 0 0 0 0 0 3 0 0 0 0 | 12 = ourson_blanc
0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 | 13 = ourson_rouge
0 0 0 0 0 0 0 0 0 0 0 0 0 0 2 0 0 | 14 = ourson_vert
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 2 0 | 15 = schtroumpfette
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 4 | 16 = tagada
Confusion matrix format: Actual (rowClass) predicted as (columnClass) N times
==================================================================
✅ Entraînement terminé !
```

### 2. Prédiction sur nouvelle image ⭐
**Après entraînement**, lance :
src/main/java/com/acs/Predict.java

**Fonctionnement** :
1. Charge `model_bonbons.zip` (racine projet)
2. **Sélectionner image** via dialogue fichier
3. **Prétraitement auto** (128x128px)
4. **Résultat immédiat** :

🎯 Classe prédite : 3
🍬 Bonbon reconnu : crocodile_vert

## Dataset
- **Train** : `/dataset/train/` → Augmentation automatique dans /dataset_processed/train
- **Test** : `/dataset/test/` → Prétraitement dans /dataset_processed/test et Évaluation

## Technologies
- **Java 11 LTS + Maven**
- **DeepLearning4J (DL4J) - CNN**
- **Data Augmentation** : Rotations, symétries
- **Outils** : VSCode, Eclipse, Git

## Auteurs
- **Sacha Lecomte** - [LinkedIn](https://www.linkedin.com/in/sachalecomte/) - Élève-ingénieur 2e année Grenoble INP-ESISAR [memory:3]

## Licence
Licence [MIT](LICENSE) - voir [LICENSE](LICENSE).
