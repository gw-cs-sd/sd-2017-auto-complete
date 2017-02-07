from contextlib import ExitStack
import gzip
import heapq
import json
import os
import re
import sys
import time
import urllib.request


def download_ngram_file(
    url_suffix, save_location=None, gram_size=2,
    url_prefix="http://storage.googleapis.com/books/ngrams/books/googlebooks-eng-all-GRAM_SIZE-20120701-"):  # noqa: E501
    """
    Defaults:
    Downloads the 2gram file.
    Downloads files from the English corpus version 20120701.

    WARNING: Does not validate HTTPS certificates if download is via HTTPS.
    """

    url = (
        url_prefix.replace("GRAM_SIZE", str(gram_size) + "gram")
        + url_suffix + ".gz")

    if save_location is None:
        os.makedirs("data", exist_ok=True)
        save_location = "data/" + str(gram_size) + "gram_" + url_suffix

    if os.path.exists(save_location):
        print("There is already a file or directory at " + save_location)
    else:
        urllib.request.urlretrieve(url, save_location)

    return save_location


def condense(input_fn, output_fn):
    current_ngram = ""
    appearances = 0
    volumes = 0

    with gzip.open(input_fn, "rt", newline='', encoding="utf8") as input_f:
        with open(output_fn, "wt", encoding="utf8") as output_f:
            for line in input_f:
                l = line.split("\t")
                if l[0] == current_ngram:
                    appearances += int(l[2])
                    volumes += int(l[3])
                else:
                    if current_ngram != "":
                        # Dummy year value for now.
                        output_f.write(
                            current_ngram + "\t0000\t" +
                            str(appearances) + "\t" + str(volumes) + "\n")
                    current_ngram = l[0]
                    appearances = int(l[2])
                    volumes = int(l[3])

            output_f.write(
                current_ngram + "\t0000\t" +
                str(appearances) + "\t" + str(volumes) + "\n")


def clean(line):
    """
    For version 2 (2012) of the ngram corpus:
    line[0] - ngram
    line[1] - year
    line[2] - appearances
    line[3] - volume appearances
    """
    words = line[0].split(" ")

    for i in range(0, len(words)):
        # Remove part of speech labels. TODO improve detection.
        if words[i].find("_") != -1:
            words[i] = words[i][:(words[i].find("_"))]

        # Make lowercase
        words[i] = words[i].lower()

        if re.search('[a-z]', words[i]) is None:
            return False

    return " ".join(words + line[2:])


def clean_and_sort(list_of_files, output_fn):
    chunk_size = 1000000
    temp_files = []

    t = time.perf_counter()
    for f in list_of_files:

        with open(f, "rt", newline='', encoding="utf8") as input_f:
            lines = []
            line_count = 0

            for line in input_f:
                l = line.split("\t")
                c_l = clean(l)

                if c_l:
                    lines.append(c_l)
                    line_count += 1

                    if line_count % chunk_size == 0:
                        temp_fn = f + "_temp" + str(line_count // chunk_size)
                        with open(temp_fn, "wt", encoding="utf8") as temp_f:
                            for s_l in sorted(lines):
                                temp_f.write(s_l)
                        temp_files.append(temp_fn)
                        lines = []

            if len(lines) > 0:
                temp_fn = f + "_temp" + str((line_count // chunk_size) + 1)
                with open(temp_fn, "wt", encoding="utf8") as temp_f:
                    for s_l in sorted(lines):
                        temp_f.write(s_l)
                temp_files.append(temp_fn)
    print(
        "     Cleaning and sorting within chunks: " +
        str(time.perf_counter() - t) + " seconds.")

    t = time.perf_counter()
    with ExitStack() as stack, gzip.open(
            output_fn, "wt", encoding="utf8") as output_file:
        file_iters = [
            stack.enter_context(
                open(f, "rt", encoding="utf8")) for f in temp_files]
        output_file.writelines(heapq.merge(*file_iters))
    print(
        "     Temp file merging and sorting: " +
        str(time.perf_counter() - t) + " seconds.")

    t = time.perf_counter()
    for temp_f in temp_files:
        os.remove(temp_f)
    print(
        "     Temp file deletion: " +
        str(time.perf_counter() - t) + " seconds.")


def export(temp_file_name, export_file_name):
    with gzip.open(
            temp_file_name, "rt", newline='', encoding="utf8") as temp_f:
        with gzip.open(export_file_name, "wt", encoding="utf8") as export_f:

            part1 = None
            part2 = None

            appearances = 0
            following_frequency = []

            export_f.write("[")

            for line in temp_f:
                l = line.split()

                if len(l) >= 4:
                    l_part1 = l[0]

                    # All but the first and the last two.
                    l_part2 = " ".join(l[1:-2])

                    # If same ngram
                    if part1 == l_part1 and part2 == l_part2:
                        appearances += int(l[-2])
                    # If same part 1
                    elif part1 == l_part1:
                        following_frequency.append(
                            {"appearances": appearances, "text": part2})

                        part2 = l_part2
                        appearances = int(l[-2])
                    else:
                        if part1 is not None and part2 is not None:
                            following_frequency.append(
                                {"appearances": appearances, "text": part2})

                            following_frequency.sort(
                                key=lambda x: x["appearances"], reverse=True)

                            if len(following_frequency) > 10:
                                following_frequency = following_frequency[:10]

                            json_to_write = {
                                "following_frequency": following_frequency,
                                "word": part1
                            }

                            export_f.write(json.dumps(json_to_write))
                            export_f.write(",")

                        part1 = l_part1
                        part2 = l_part2
                        appearances = int(l[-2])
                        following_frequency = []

            # Write json for last part1
            following_frequency.append(
                {"appearances": appearances, "text": part2})

            following_frequency.sort(
                key=lambda x: x["appearances"], reverse=True)

            if len(following_frequency) > 10:
                following_frequency = following_frequency[:10]

            json_to_write = {
                "following_frequency": following_frequency,
                "word": part1
            }

            export_f.write(json.dumps(json_to_write))
            export_f.write("]")


def process(selection, path):
    if len(selection) is not 2:
        print("Selection must be two characters.")
        sys.exit()

    if path[-1] is not "/":
        path += "/"

    ngram_files = []

    for i in range(2, 6):
        data_file = path + "ngrams/" + selection + str(i) + ".gz"

        t = time.perf_counter()
        download_ngram_file(selection, save_location=data_file, gram_size=i)
        if time.perf_counter() - t > 0.001:
            print(
                data_file + " download: " + str(time.perf_counter() - t) +
                " seconds.")

        t = time.perf_counter()
        condense(data_file, data_file + "_condensed")
        print(
            data_file + " condense: " + str(time.perf_counter() - t) +
            " seconds.")

        ngram_files.append(data_file + "_condensed")

    t = time.perf_counter()
    clean_and_sort(ngram_files, path + selection + "_sorted.gz")
    print(
        "Data cleaning and sorting: " + str(time.perf_counter() - t) +
        " seconds.")

    t = time.perf_counter()
    export(path + selection + "_sorted.gz", path + selection + ".json.gz")
    print("Data export: " + str(time.perf_counter() - t) + " seconds.")


if __name__ == '__main__':
    process(sys.argv[1], sys.argv[2])
