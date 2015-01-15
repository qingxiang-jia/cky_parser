# CKY Parser
This parser is written in Java using CKY algorithm. The training data (from Penn Tree Bank) and testing scripts are provided by Professor Michael Collins at Columbia University.

## Principles
Basically, it first learns from the training data, and then generate parameters for CKY algorithm. It then runs CKY algorithm to recover parse for a given English sentence.

## How to run
You can either run the grade.sh script or take a look at the script and figure out how to run it in your way.

To run the script, first give it execution permission by typing in terminal: **chmod 777 grade.sh**

Then run the script: **./grade.sh**

## Accuracy
**Found 5804 NEs. Expected 5931 NEs; Correct: 4341.**
**precision recall F1-Score **
Total: 0.747932 0.731917 0.739838 
PER: 0.810658 0.778020 0.794003
ORG: 0.542753 0.668909 0.599263
LOC: 0.845872 0.754089 0.797348 
MISC: 0.837302 0.687296 0.754919

Qingxiang Jia

Oct. 24, 2014