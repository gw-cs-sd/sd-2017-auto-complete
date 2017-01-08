# Data Processing

Create a folder where you want to store the data files. Create a subfolder
named `ngrams`.

In the folder with `data_processing.py`, run
```
python data_processing.py aa /path/to/folder
```
Replace `aa` with the two letter sequence you want to process, and replace
`/path/to/folder` with the path to the folder you created. Raw ngram data from
Google Books will be downloaded to the `ngrams` subfolder you created, while
the result files will be saved in the root of the folder you created.
