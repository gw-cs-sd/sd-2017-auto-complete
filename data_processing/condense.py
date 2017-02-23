import gzip
import os


def condense(input_fn, output_fn):
    """
    Takes raw gzipped file from Google of format
        NGRAM\tYEAR\tAPPEARENCES\tVOLUMES\n
    and collapses all consecutive identical ngrams, removing year field and
    summing appearances and volumes fields, and saves an uncompressed file of
    format
        NGRAM\tAPPEARENCES\tVOLUMES\n
    """
    if os.path.exists(output_fn):
        print("Already Condensed: " + output_fn)
    else:
        current_ngram = ""
        appearances = 0
        volumes = 0

        with gzip.open(input_fn, "rt", newline='', encoding="utf8") as input_f:
            with open(
                    output_fn + "_condensing", "wt", encoding="utf8"
                    ) as output_f:
                for line in input_f:
                    l = line.split("\t")
                    if l[0] == current_ngram:
                        appearances += int(l[2])
                        volumes += int(l[3])
                    else:
                        if current_ngram != "":
                            output_f.write(
                                current_ngram + "\t" +
                                str(appearances) + "\t" + str(volumes) + "\n")
                        current_ngram = l[0]
                        appearances = int(l[2])
                        volumes = int(l[3])

                output_f.write(
                    current_ngram + "\t" +
                    str(appearances) + "\t" + str(volumes) + "\n")

        os.rename(output_fn + "_condensing", output_fn)
